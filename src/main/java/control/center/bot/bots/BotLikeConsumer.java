package control.center.bot.bots;


import control.center.bot.configuration.Configuration;
import control.center.bot.object.db.TGUser;
import control.center.bot.service.TGUserService;
import control.center.bot.util.LikerBuildingUtils;
import control.center.bot.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

import static control.center.bot.util.Util.getReaction;

@Component
public class BotLikeConsumer extends TelegramLongPollingBot {

    @Autowired
    private TGUserService service;

    public BotLikeConsumer(){}


    @PostConstruct
    public void init(){
        TelegramBotsApi botsApi = new TelegramBotsApi();
        try {
            botsApi.registerBot(this);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }

    @Autowired
    public BotLikeConsumer(TGUserService service) {
        this.service = service;
     }


    @Override
    public void onUpdateReceived(Update update) {
        if (!Util.isForwardedFromFriendlyChat(update)) {
            if (Util.forLikerPost(update) && !Util.isNews(update)) {
                final Optional<SendPhoto> sendPhoto = LikerBuildingUtils.buildSendPhoto(update);
                if (sendPhoto.isPresent()) {
                    processPhoto(sendPhoto.get(), update);
                } else {
                    LikerBuildingUtils.buildSendVideo(update).ifPresent(sendVideo -> processVideo(sendVideo, update));
                }

            }
            if (Util.isReaction(update)) {
                processLiked(update);
            }
        }

        List<TGUser> top10 = service.getTop10();
        String s = LikerBuildingUtils.buildTopReactionGuys(top10);
        new NewsBot().send(new SendMessage().setChatId(Configuration.BOHDAN_ADMIN_ID).setText(s));
    }

    private void processPhoto(SendPhoto sendPhoto, Update update) {
        deleteMessage(
                update.getChannelPost().getMessageId(),
                String.valueOf(update.getChannelPost().getChatId())
        );
        sendPhoto(sendPhoto);

    }

    private void processVideo(SendVideo sendVideo, Update update) {
        deleteMessage(
                update.getChannelPost().getMessageId(),
                String.valueOf(update.getChannelPost().getChatId())
        );
        sendVideo(sendVideo);
    }


    @Override
    public String getBotUsername() {
        return "BotLikerThunderBot";
    }

    @Override
    public String getBotToken() {
        return "1146373994:AAH3avSmV-qdLscDHJSW2b-XWDbs6neGdQs";
    }


    private void processLiked(Update update) {
        InlineKeyboardMarkup inlineKeyboardMarkup = Util.rebuildReplyMarkup(update);
        Integer messageId = Util.getMessageId(update);
        Long chatId = Util.getChatId(update);
        updateMessage(messageId, chatId, inlineKeyboardMarkup);

        User from = update.getCallbackQuery().getFrom();
        String reaction = getReaction(update.getCallbackQuery().getData()).getKey();
        service.processReaction(reaction, from);
     }

    void updateMessage(Integer messageId, Long chatId, InlineKeyboardMarkup inlineKeyboardMarkup) {
        try {
            execute(new EditMessageReplyMarkup()
                    .setChatId(chatId)
                    .setMessageId(messageId)
                    .setReplyMarkup(inlineKeyboardMarkup));
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }

    void deleteMessage(Integer messageId, String chatId) {
        try {
            execute(
                    new DeleteMessage()
                            .setChatId(chatId)
                            .setMessageId(messageId));
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }

    Message sendPhoto(SendPhoto sendPhoto) {
        try {
            return execute(sendPhoto);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }

    Message sendVideo(SendVideo sendVideo) {
        try {
            return execute(sendVideo);
        } catch (TelegramApiException e) {
            System.out.println("Couldnt send video" + sendVideo.toString());
        }
        return null;
    }
}
