package control.center.bot.bots;


import control.center.bot.util.LikerBuildingUtils;
import control.center.bot.util.Util;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

public class BotLikeConsumer extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {
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
            System.out.println(e.getMessage());
        }
        return null;
    }
}
