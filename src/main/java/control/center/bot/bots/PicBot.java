package control.center.bot.bots;


import control.center.bot.configuration.Configuration;
import control.center.bot.service.ContentGetter;
import control.center.bot.util.Util;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Component
public class PicBot extends TelegramLongPollingBot {

    private static final String PIC_CRON = "1 */1 * * * *";
    private List<String> searchWords = Arrays.asList("картинко", "обосрался", "жпег", "засмеялся");
    private List<String> exceptions = Arrays.asList("аниме", "anime", "ониме", "black", "dark", "дарк");
    private ContentGetter contentGetter = new ContentGetter(searchWords, exceptions);

    @Override
    public void onUpdateReceived(Update update) {
        if (!Util.isChannelPost(update)) {
            final String fileId = update.getCallbackQuery().getMessage().getPhoto().get(0).getFileId();
            sendPhotoWithMenu(fileId, Configuration.THUNDER_ID, null);
        }
    }

    @Override
    public String getBotUsername() {
        return "ElephantPicBot";
    }

    @Override
    public String getBotToken() {
        return "870223480:AAEqCPq6JN0MtRyUfQuaCUcSZMfCb4M5JRo";
    }


    @Scheduled(cron = PIC_CRON)
    public void send() {
        Optional<String> video = contentGetter.getPic();
        video.ifPresent(s -> Configuration.admins.forEach(adminId -> sendPhotoWithMenu(s, adminId, Util.createLikeMenu())));
    }

    private void sendPhotoWithMenu(String link, Long chatId, InlineKeyboardMarkup menu) {
        try {
            execute(new SendPhoto()
                    .setChatId(chatId)
                    .setPhoto(link)
                    .setReplyMarkup(menu));
        } catch (TelegramApiException e) {
            System.out.println("Could not send video " + link + " " + e.getMessage());
        }
    }
}

