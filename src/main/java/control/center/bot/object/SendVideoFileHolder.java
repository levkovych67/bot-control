package control.center.bot.object;

import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.methods.send.SendVideoNote;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class SendVideoFileHolder {

    private SendVideo sendVideo;
    private String path;

    private SendVideoFileHolder() {

    }

    public String getPath() {
        return path;
    }

    public static SendVideoFileHolder init(SendVideo sendVideo, String path) {
        SendVideoFileHolder sendVideoFileHolder = new SendVideoFileHolder();



        sendVideoFileHolder.sendVideo = sendVideo;
        sendVideoFileHolder.path = path;
        return sendVideoFileHolder;
    }

    public SendVideo getSendVideo() {
        return sendVideo;
    }

    public void setSendVideo(SendVideo sendVideo) {
        this.sendVideo = sendVideo;
    }

    public void deleteFiles() {
        try {
            String mp4Path = sendVideo.getVideo().getNewMediaFile().getAbsolutePath();
            Files.deleteIfExists(new File(mp4Path).toPath());
            String mp4 = mp4Path.replace("_w.mp4", ".mp4");
            String webm = mp4.replace(".mp4", ".webm");
            Files.deleteIfExists(new File(mp4).toPath());
            Files.deleteIfExists(new File(webm).toPath());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException ex) {

        }


    }
}
