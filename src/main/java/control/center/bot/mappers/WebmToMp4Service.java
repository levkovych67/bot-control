package control.center.bot.mappers;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class WebmToMp4Service {

    public static String mapToMp4(String webm) {
        try {
            File file = downloadFromUrl(webm);
            return (convertWebmToMp4(file.getPath()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static String convertWebmToMp4(String oldPath) {
        String command = String.format("ffmpeg -i %s -max_muxing_queue_size 1024 -movflags faststart -profile:v " +
                        "high -level 4.2 %s",
                oldPath, oldPath.replace(".webm", ".mp4"));


        try {
            Process exec = Runtime.getRuntime().exec(command);
            exec.waitFor();
            return oldPath.replace(".webm", ".mp4");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("cant convert");

    }

    public static File downloadFromUrl(String link) {
        if (!link.contains("https")) {
            link = "https://" + link;
        }
        String fileName = link.substring(link.lastIndexOf("/") + 1);
        final File file = new File("content/" + fileName);
        if (!file.exists()) {
            try {
                FileUtils.copyURLToFile(
                        new URL(link),
                        file,
                        10000,
                        10000);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

}
