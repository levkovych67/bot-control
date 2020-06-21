package control.center.bot.bots;


import com.google.api.services.youtube.YouTube;
import control.center.bot.configuration.Configuration;
import control.center.bot.object.SendVideoFileHolder;
import control.center.bot.service.ContentGetter;
import control.center.bot.util.Util;
import control.center.bot.youtube.YouTubeUploaderService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.methods.send.SendVideoNote;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class VideoBot extends TelegramLongPollingBot {

    private static final String VIDEO_CRON = "1 */4 * * * *";
    private List<String> searchWords = Arrays.asList("mp4", "webm", "mp3", "шебм", "обосрался", "жпег", "засмеялся");
    private List<String> exceptions = Arrays.asList("аниме", "anime", "ониме", "black", "dark", "дарк");

    private ContentGetter contentGetter = new ContentGetter(searchWords, exceptions);

    @Override
    public void onUpdateReceived(Update update) {
        if (!Util.isChannelPost(update)) {
            try {
                processAdminRequest(update);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void processAdminRequest(Update update) throws IOException {
        String data = update.getCallbackQuery().getData();
        if (data.contains(".mp4")) {
            YouTubeUploaderService.upload(data);
        } else {
            System.out.println(data);
            Optional<String> videoLink = Util.getVideoLink(update);
            videoLink.ifPresent(s -> send(new SendVideo().setVideo(s), Long.valueOf(data), null));
        }

    }

    @Override
    public String getBotUsername() {
        return "ElephantVideoBot";
    }

    @Override
    public String getBotToken() {
        return "1087485873:AAEkSJqmTGHc9m9qob8PMvUKN1KqDFA6di4";
    }

    @Scheduled(cron = VIDEO_CRON)
    public void send() {
        SendVideoFileHolder video = contentGetter.getVideo();
        if (video != null) {
            Configuration.admins.forEach(adminId -> send(
                    video.getSendVideo(),
                    adminId,
                    Util.createLikeMenuWithUtube(video)));
                    video.deleteFiles();
         }

    }

    private void send(SendVideo sendVideo, Long chatId, InlineKeyboardMarkup menu) {
        try {
            execute(sendVideo.setChatId(chatId).setReplyMarkup(menu));
        } catch (TelegramApiException e) {
            System.out.println("Could not send video " + sendVideo.toString());
        }

    }

}

