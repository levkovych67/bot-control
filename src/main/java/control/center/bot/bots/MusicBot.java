package control.center.bot.bots;

import control.center.bot.scrapers.ThreadService;
import control.center.bot.service.ContentGetter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MusicBot extends TelegramLongPollingBot {

    private static final Long MUS_ID = -1001376648948L;
    private static final String MUSIC_CRON = "1 */10 * * * *";
    private List<String> searchWords = Collections.singletonList("муз");
    private ContentGetter contentGetter = new ContentGetter(searchWords, Collections.emptyList());

    @Scheduled(cron = MUSIC_CRON)
    public void send() {
//        SendVideoFileHolder video = contentGetter.getVideo();
//        if(video != null) {
//            send(video.getSendVideo().setChatId(MUS_ID));
//            video.deleteFiles();
//        }

        Set<String> collect = ThreadService.searchByWords(searchWords).stream()
                .map(e -> e.getText() + " " + e.getPostCount() + " постов ")
                .collect(Collectors.toSet());
        collect.forEach(this::sendText);
    }

    private void sendText(String text) {
        try {
            execute(new SendMessage().setText(text).setChatId(MUS_ID));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void send(SendVideo sendVideo) {
        try {
            execute(sendVideo);
        } catch (TelegramApiException e) {
            System.out.println("can't send fap");
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println(update);
    }

    @Override
    public String getBotUsername() {
        return "music_thread_bot";
    }

    @Override
    public String getBotToken() {
        return "1092171644:AAH1PALMxRJxGXEnrFPnZjoustM9cYIZDJk";
    }
}
