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

import static control.center.bot.configuration.Configuration.DARK_CRON;

@Component
public class DarkBot extends TelegramLongPollingBot {

    private Long DARK_ID = -1001483764823L;
    private List<String> searchWords = Arrays.asList("dark", "black", "дарк", "блек", "military", "милитари");
    private ContentGetter contentGetter = new ContentGetter(searchWords, Collections.emptyList());


    @Override
    public void onUpdateReceived(Update update) {

    }

    @Override
    public String getBotUsername() {
        return "ElephantDark";
    }

    @Override
    public String getBotToken() {
        return "1066288750:AAEo2RXrgICINHI1UVqltfkUIy0i5rZ1j1c";
    }

    @Scheduled(cron = DARK_CRON)
    public void send() {
        SendVideoFileHolder video = contentGetter.getVideo();
        if (video != null) {
            send(video.getSendVideo().setChatId(DARK_ID));
            video.deleteFiles();
        }


    }

    private void send(SendVideo sendVideo) {
        try {
            execute(sendVideo);
        } catch (TelegramApiException e) {
            System.out.println("Could not send video " + sendVideo.toString());
        }
    }
}
