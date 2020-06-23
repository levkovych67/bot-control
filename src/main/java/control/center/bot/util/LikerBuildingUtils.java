package control.center.bot.util;

import control.center.bot.object.db.TGUser;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class LikerBuildingUtils {

    public static Optional<SendVideo> buildSendVideo(Update update) {
        try {
            if (TYPE.VIDEO.name().equals(Util.getType(update))) {
                String contentId = Util.getContentId(update);
                SendVideo sendVideo = new SendVideo()
                        .setChatId(update.getChannelPost().getChatId())
                        .setVideo(contentId)
                        .setReplyMarkup(Util.createThunderLikeMenu(
                                0, 0, 0, 0
                        ));
                return Optional.of(sendVideo);

            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            return Optional.empty();
        }

    }



    public static Optional<SendPhoto> buildSendPhoto(Update update) {
        try {
            if (update.getChannelPost().getPhoto() != null) {


                SendPhoto sendPhoto = new SendPhoto()
                        .setChatId(update.getChannelPost().getChatId())
                        .setCaption("<a href=\"https://t.me/joinchat/AAAAAETS87l2Y-yRt1iOXg\">Блискавка 2.0</a>" + "\n "
                                + (update.getChannelPost().getCaption() == null ? "" : update.getChannelPost().getCaption()))
                        .setPhoto(update.getChannelPost().getPhoto().get(0).getFileId())
                        .setParseMode("HTML")
                        .setReplyMarkup(Util.createThunderLikeMenu(
                                new Random().nextInt(25),
                                new Random().nextInt(10),
                                new Random().nextInt(14),
                                new Random().nextInt(18)));
                return Optional.of(sendPhoto);

            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            return Optional.empty();
        }

    }

    public static String buildTopReactionGuys(List<TGUser> list){
        StringBuilder message = new StringBuilder("");
        message.append("TOP 10 САМИХ АКТІВНИХ \n");
        message.append("------------------------\n");
        int count = 1;
        for (TGUser e : list) {
            message.append(count).append(") ")
                    .append(e.getFirstName()).append(" ")
                    .append(e.getLikes()).append("\uD83D\uDC4D ")
                    .append(e.getDislike()).append("\uD83D\uDC4E ")
                    .append(e.getRofl()).append("\uD83D\uDE2D ")
                    .append(e.getDislike() + e.getLikes() + e.getRofl()).append("⚡️ \n");
            count = count + 1;
        }
        message.append("------------------------\n");
        return message.toString();
    }

}
