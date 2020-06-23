package control.center.bot.videoprocessor;

import control.center.bot.mappers.WebmToMp4Service;
import control.center.bot.watermark.WatermarkService;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static control.center.bot.mappers.WebmToMp4Service.downloadFromUrl;
import static control.center.bot.mappers.WebmToMp4Service.validateSize;
import static control.center.bot.watermark.WatermarkService.addWaterMark;

public class SendVideoPreprocessor {

    public static SendVideo process(String link) {
        String path = "";
        if (link.contains(".webm")) {
            path = processWebm(link);
        } else {
            path = buildDefault(link);
        }

        return new SendVideo().setVideo(new File(path));
    }

    private static String processWebm(String link) {
        String path = WebmToMp4Service.mapToMp4(link);
        if(path == null){
            return null;
        } else {
            String mp4WithWaterMark = addWaterMark(path);
            return  mp4WithWaterMark;
        }

    }

    private static String  buildDefault(String link) {
        validateSize(link);
        File file = downloadFromUrl(link);
        String path = WatermarkService.addWaterMark(file.getAbsolutePath());
        try {
            Files.deleteIfExists(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return   path;
    }



}
