package control.center.bot.util;

import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Collections;

import static control.center.bot.util.Util.createKeyBoardRow;


public class NewsUtil {

    public static SendPhoto buildPhotoPost(Update update, Long chatId) {
        final String text = update.getCallbackQuery().getMessage().getCaption();
        String photo = update.getCallbackQuery().getMessage().getPhoto().get(0).getFileId();
        final InlineKeyboardButton inlineKeyboardButton =
                update.getCallbackQuery().getMessage().getReplyMarkup().getKeyboard().get(0).get(1);
        return new SendPhoto()
                .setCaption(text)
                .setParseMode(ParseMode.HTML)
                .setPhoto(photo)
                .setReplyMarkup(createKeyBoardRow(Collections.singletonList(inlineKeyboardButton)))
                .setChatId(chatId);
    }

    public static SendVideo buildVideoPost(Update update, Long chatId) {
        final String text = update.getCallbackQuery().getMessage().getCaption();
        String video = update.getCallbackQuery().getMessage().getVideo().getFileId();

        final InlineKeyboardButton inlineKeyboardButton = update.getCallbackQuery().getMessage().getReplyMarkup().getKeyboard().get(0).get(1);
        return new SendVideo()
                .setCaption(text)
                .setParseMode(ParseMode.HTML)
                .setVideo(video)
                .setReplyMarkup(createKeyBoardRow(Collections.singletonList(inlineKeyboardButton)))
                .setChatId(chatId);
    }

    public static Boolean isPhotoCallback(Update update) {
        return update.getCallbackQuery().getMessage().getPhoto() != null;
    }

    public static Boolean isVideoCallback(Update update) {
        return update.getCallbackQuery().getMessage().getVideo() != null;
    }
}
