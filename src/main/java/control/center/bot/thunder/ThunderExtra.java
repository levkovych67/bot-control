package control.center.bot.thunder;


import org.apache.commons.lang3.tuple.Pair;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class ThunderExtra extends TelegramLongPollingBot {

    private static final String TOKEN = "908441475:AAFfXEQtobvzYkZTTGl68zcSjl--tkmb5yE";
    private static final Integer vid_pause = 10;
    private static final Integer pic_pause = 10;
    private static final String NAME = "PepsiCPBot";


    @Override
    public void onUpdateReceived(Update update) {

    }

    @Scheduled(cron = "0 */10 * * * *")
    private void sendContent() {
        try {
            Pair<String, String> path = getPath();
            if (LinkUtils.isVideo(path.getValue())) {
                System.out.println("SENDING " + path.getValue() + " AND KEY IS " + path.getKey());
                execute(LinkService.createVideoMessage(path.getValue()));
                FileService.deleteFile(path.getValue());
            } else {
                System.out.println("SENDING " + path.getValue() + " AND KEY IS " + path.getKey());
                execute(LinkService.createPhotoMessage(path.getValue()));
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    public String getBotUsername() {
        return NAME;
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }

    private Pair<String, String> getPath() {
        System.out.println("GETTING PATH");
        String link = LinkService.getLink();
        String pathToFile = FileService.process(link);
        if (pathToFile == null) {
            return getPath();
        } else {
            return Pair.of(link, pathToFile);
        }
    }


}
