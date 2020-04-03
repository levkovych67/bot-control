package control.center.bot.util;

import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;
import java.util.Random;

public class LikerBuildingUtils {

    public static Optional<SendVideo> buildSendVideo(Update update) {
        try {
            if (TYPE.VIDEO.name().equals(Util.getType(update))) {
                String contentId = Util.getContentId(update);
                SendVideo sendVideo = new SendVideo()
                        .setChatId(update.getChannelPost().getChatId())
                        .setCaption(update.getChannelPost().getCaption())
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
                        .setCaption(update.getChannelPost().getCaption())
                        .setPhoto(update.getChannelPost().getPhoto().get(0).getFileId())
                        .setReplyMarkup(Util.createThunderLikeMenu(
                                new Random().nextInt(25),
                                new Random().nextInt(10),
                                new Random().nextInt(14),
                                new Random().nextInt(18)
                        ));
                return Optional.of(sendPhoto);

            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            return Optional.empty();
        }

    }

}
