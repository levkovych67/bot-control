package control.center.bot.util;

import control.center.bot.mappers.WebmToMp4Service;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;

import java.io.File;

public class SendVideoPreprocessor {

    public static SendVideo process(String link) {
        if (link.contains(".webm")) {
            return processWebm(link);
        } else {
            return buildDefault(link);
        }
    }

    private static SendVideo processWebm(String link) {
        String path = WebmToMp4Service.mapToMp4(link);
        return path != null ? new SendVideo().setVideo(new File(path)) : null;

    }

    private static SendVideo buildDefault(String link) {
        return new SendVideo().setVideo(link);
    }

}
