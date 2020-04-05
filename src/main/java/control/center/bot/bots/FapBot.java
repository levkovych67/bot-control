package control.center.bot.bots;

import control.center.bot.object.SendVideoFileHolder;
import control.center.bot.service.ContentGetter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@Component
public class FapBot extends TelegramLongPollingBot {

    private static final Long FAP_ID = -1001482709087L;
    private static final String FAP_CRON = "1 */24 * * * *";
    private List<String> searchWords = Arrays.asList("fap", "фап", "porn", "sex", "прон");
    private ContentGetter contentGetter = new ContentGetter(searchWords, Collections.emptyList());

    @Scheduled(cron = FAP_CRON)
    public void send() {
        SendVideoFileHolder video = contentGetter.getVideo();
        if (video != null) {
            send(video.getSendVideo().setChatId(FAP_ID));
            video.deleteFiles();
        }
    }

    private void send(SendVideo sendVideo) {
        try {
            execute(sendVideo);
        } catch (TelegramApiException e) {
            System.out.println("can't send fap " + sendVideo.toString());
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
    }

    @Override
    public String getBotUsername() {
        return "ElephantFapBot";
    }

    @Override
    public String getBotToken() {
        return "903409974:AAE4BChPdHuDVmY5YJWyz-f18WN4CSOlzeg";
    }
}
