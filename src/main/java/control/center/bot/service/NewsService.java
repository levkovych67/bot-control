package control.center.bot.service;

import control.center.bot.object.CoronaInfo;
import control.center.bot.object.NewsThread;
import control.center.bot.object.ThreadHead;
import control.center.bot.scrapers.ThreadService;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class NewsService {


    public static Set<NewsThread> getThreadsForMenu(Integer limit) {
        final Set<ThreadHead> news = ThreadService.getNews(25);
        return news.stream().map(NewsThread::new).collect(Collectors.toSet());
    }


    private static List<CoronaInfo> getCoronaDailyStats() {
        List<Map<String, Object>> body = new RestTemplate().getForEntity(
                "https://coronavirus.zone/data.json",
                List.class).getBody();
        List<CoronaInfo> coronaInfos = new ArrayList<>();
        for (Map<String, Object> map : body) {
            coronaInfos.add(new CoronaInfo((String) map.get("region"), (Integer) map.get("cases"), (Integer) map.get("death")));
        }
        return coronaInfos;
    }


    public static SendPhoto getCoronaPost(String dayTime) {
        List<CoronaInfo> coronaDailyStats = getCoronaDailyStats();

        List<CoronaInfo> top10 = coronaDailyStats.subList(0, 10);

        String top10String = dayTime + " сводка по вірусу" +
                "\nТоп 10 стран яким пізда вид короні\n---------------------\nСтрана : Заболiли : УмерлиНахуй";
        for (CoronaInfo info : top10) {
            top10String = top10String + "\n" +
                    info.getRegion() + " : " +
                    info.getCases() + " : " +
                    info.getDeath() + "";
        }

        CoronaInfo ukraine = coronaDailyStats.stream().filter(e -> e.getRegion().equals("Ukraine")).findFirst().get();
        CoronaInfo russia = coronaDailyStats.stream().filter(e -> e.getRegion().equals("Russia")).findFirst().get();

        top10String = "*" + top10String
                + "\n---------------------\n" +
                ukraine.getRegion() + " : " + ukraine.getCases() + " : " + ukraine.getDeath() + "\n" +
                russia.getRegion() + " : " + russia.getCases() + " : " + russia.getDeath()
                + "\n---------------------\n*" +
                "_Дай бог здоровля всю цю залупу пережити_";

        File file = null;

        try {
            file = new File("./src/main/resources/corona.png");
        } catch (Exception e) {
            throw new RuntimeException("Could not get file ebat");
        }

        SendPhoto sendPhoto = new SendPhoto()
                .setCaption(top10String)
                .setPhoto(file)
                .setParseMode("MARKDOWN");
        return sendPhoto;
    }


}
