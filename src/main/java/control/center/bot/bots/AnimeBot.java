package control.center.bot.bots;


import control.center.bot.object.SendVideoFileHolder;
import control.center.bot.service.ContentGetter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Component
public class AnimeBot extends TelegramLongPollingBot {

    private static final String ANIME_CRON = "0 */5 * * * *";
    private static final String ANIME_PIC_CRON = "0 */20 * * * *";
    private static Long channelId = -1001435511559L;
    private List<String> searchWords = Arrays.asList("mp4", "webm", "mp3", "шебм", "обосрался", "жпег", "засмеялся");
    private List<String> exceptions = Collections.emptyList();
    private ContentGetter contentGetter = new ContentGetter(searchWords, exceptions);

    @Scheduled(cron = ANIME_CRON)
    public void send() {
        SendVideoFileHolder video = contentGetter.getVideo();
        if (video != null) {
            send(video.getSendVideo().setChatId(channelId));
            video.deleteFiles();
        }
    }

    @Scheduled(cron = ANIME_PIC_CRON)
    public void sendPic() {
        Optional<String> pic = contentGetter.getPic();
        pic.ifPresent(this::sendPic);
    }

    private void send(SendVideo sendVideo) {
        try {
            execute(sendVideo);
        } catch (TelegramApiException e) {
            System.out.println("Could not send video " + sendVideo.toString());
        }
    }

    private void sendPic(String link) {
        try {
            execute(new SendPhoto().setChatId(channelId).setPhoto(link));
        } catch (TelegramApiException ignored) {
        }
    }

    @Override
    public String getBotUsername() {
        return "onime_thunder_bot";
    }

    @Override
    public String getBotToken() {
        return "1238690589:AAErxUmMiqx2J2tILdrUmG18n8PZtu_zd5k";
    }

    @Override
    public void onUpdateReceived(Update update) {

    }
}
