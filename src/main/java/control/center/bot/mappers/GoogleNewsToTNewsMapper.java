package control.center.bot.mappers;

import control.center.bot.object.GoogleNews;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Arrays;
import java.util.List;

import static control.center.bot.util.Util.*;

public class GoogleNewsToTNewsMapper {

    public static SendPhoto map(GoogleNews news) {
        String content =
                "<b>" + news.getTitle() + "</b>\n<i>" + news.getContent() + "</i>";

        final List<InlineKeyboardButton> inlineKeyboardButtons =
                Arrays.asList(
                        createInlineButtonText("\uD83D\uDC4D️", "/like"),
                        createInlineButtonLink("В тред", news.getUrl()));

        return new SendPhoto()
                .setPhoto(news.getUrlToImage())
                .setCaption(content)
                .setReplyMarkup(createKeyBoardRow(inlineKeyboardButtons))
                .setParseMode("HTML");
    }

}
