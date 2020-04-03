package control.center.bot.bots;


import control.center.bot.configuration.Configuration;
import control.center.bot.object.NewsThread;
import control.center.bot.service.NewsService;
import control.center.bot.util.NewsUtil;
import control.center.bot.util.TYPE;
import control.center.bot.util.Util;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static control.center.bot.util.Util.*;


@Component
public class NewsBot extends TelegramLongPollingBot {

    private static final String MORNING_CORONA_POST = "0 0 8 * * *";
    private static final String EVENING_CORONA_POST = "0 30 23 * * *";

    @Override
    public void onUpdateReceived(Update update) {
        if (!Util.isChannelPost(update)) {
            final Long chatId = Util.getChatId(update);
            final String command = Util.getCommand(update);

            if ("/start".equals(command) && chatId.equals(Configuration.BOHDAN_ADMIN_ID)) {
                final Set<NewsThread> threadsForMenu = NewsService.getThreadsForMenu(25);
                threadsForMenu.forEach(e -> send(e, chatId));
            } else {
                if (NewsUtil.isPhotoCallback(update)) {
                    send(NewsUtil.buildPhotoPost(update, Configuration.THUNDER_ID));
                }
                if (NewsUtil.isVideoCallback(update)) {
                    send(NewsUtil.buildVideoPost(update, Configuration.THUNDER_ID));
                }

            }
        }

    }

    private void send(NewsThread newsThread, Long chatId) {

        final List<InlineKeyboardButton> inlineKeyboardButtons =
                Arrays.asList(
                        createInlineButtonText("\uD83D\uDC4D️", "/like"),
                        createInlineButtonLink("В тред", newsThread.getLink()));

        if (newsThread.getMedia().getType().equals(TYPE.PICTURE)) {
            SendPhoto sendPhoto = new SendPhoto()
                    .setChatId(chatId)
                    .setCaption(newsThread.getText())
                    .setPhoto(newsThread.getMedia().getLink())
                    .setReplyMarkup(createKeyBoardRow(inlineKeyboardButtons));
            send(sendPhoto);
        } else {
            SendVideo sendVideo = new SendVideo()
                    .setChatId(chatId)
                    .setCaption(newsThread.getText())
                    .setVideo(newsThread.getMedia().getLink())
                    .setReplyMarkup(createKeyBoardRow(inlineKeyboardButtons));
            send(sendVideo);
        }


    }

    @Scheduled(cron = MORNING_CORONA_POST)
    public void sentCoronaMorningUpdate() {
        SendPhoto sendPhoto = NewsService.getCoronaPost("Утрення").setChatId(Configuration.THUNDER_ID);
        send(sendPhoto);
    }


    @Scheduled(cron = EVENING_CORONA_POST)
    public void sentCoronaEveningUpdate() {
        SendPhoto sendPhoto = NewsService.getCoronaPost("Вечерня ").setChatId(Configuration.THUNDER_ID);
        send(sendPhoto);
    }

    private void send(SendVideo sendVideo) {
        try {
            execute(sendVideo);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void send(SendPhoto sendPhoto) {
        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String getBotUsername() {
        return "ElephantFapBot";
    }

    @Override
    public String getBotToken() {
        return "825441785:AAH0KTVX0ias5EraSIhO1WOs7liW8GhsUu4";
    }

}
