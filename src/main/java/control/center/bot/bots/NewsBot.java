package control.center.bot.bots;


import control.center.bot.configuration.Configuration;
import control.center.bot.object.NewsThread;
import control.center.bot.object.ThreadHead;
import control.center.bot.service.NewsService;
import control.center.bot.util.NewsUtil;
import control.center.bot.util.TYPE;
import control.center.bot.util.Util;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMembersCount;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static control.center.bot.util.Util.*;


@Component
public class NewsBot extends TelegramLongPollingBot {

    private static final String MORNING_CORONA_POST = "0 30 9 * * *";
    private static final String CHECK_CHAT = "0 0 * * * *";
    private static final String EVENING_CORONA_POST = "0 30 22 * * *";

    private Integer THUNDER_COUNT = 0;
    private Integer FAP_COUNT = 0;
    private Integer DARK_COUNT = 0;
    private Integer MUSIC_COUNT = 0;


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

    public static void main(String[] args) {
        GetChatMembersCount chat = new GetChatMembersCount().setChatId(Configuration.THUNDER_ID);
        System.out.println(chat.getMethod());
    }

    @Scheduled(cron = CHECK_CHAT)
    public void sendThread() {
        Integer thunder = sendCheckChat(Configuration.THUNDER_ID);
        if (sendChannelUpdate(thunder, THUNDER_COUNT, "Bliskavka 2.0")) {
            THUNDER_COUNT = thunder;
        }
        Integer fap = sendCheckChat(Configuration.FAP_ID);
        if (sendChannelUpdate(fap, FAP_COUNT, "/fap")) {
            FAP_COUNT = fap;
        }
        Integer dark = sendCheckChat(Configuration.DARK_ID);
        if (sendChannelUpdate(dark, DARK_COUNT, "/dark")) {
            DARK_COUNT = dark;
        }
        Integer musci = sendCheckChat(Configuration.MUS_ID);
        if (sendChannelUpdate(musci, MUSIC_COUNT, "/music")) {
            MUSIC_COUNT = musci;
        }

    }

    public Boolean sendChannelUpdate(Integer newCount, Integer lastCount, String channelName) {
        if (!newCount.equals(lastCount)) {
            Integer diff = newCount - lastCount;

            send(new SendMessage().setChatId(Configuration.BOHDAN_ADMIN_ID)
                    .setText(String.format("Channel %s has diff %d, now subscription count is %s",
                            channelName, diff, newCount)));
            return true;
        } else {
            return false;
        }
    }

    Integer sendCheckChat(Long chatId) {
        try {
            return execute(new GetChatMembersCount().setChatId(chatId));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        throw new RuntimeException();
    }

    void send(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
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
            System.out.println("Couldnt send video" + sendVideo.toString());
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
