package control.center.bot.mappers;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class WebmToMp4Service {

    public static String mapToMp4(String webm) {
        try {
            validateSize(webm);
            File file = downloadFromUrl(webm);
            return (convertWebmToMp4(file.getPath()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static Double getFileSizeByLink(String link) {
        if (!link.contains("https://")) {
            link = "https://" + link;
        }
        URLConnection conn = null;
        URL url = null;
        try {
            url = new URL(link);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            conn = url.openConnection();
            if (conn instanceof HttpURLConnection) {
                ((HttpURLConnection) conn).setRequestMethod("HEAD");
            }
            conn.getInputStream();
            int size = conn.getContentLength();
            return BigDecimal.valueOf(size)
                    .divide(BigDecimal.valueOf(1048576))
                    .setScale(2, 2)
                    .doubleValue();
        } catch (IOException e) {
            System.out.println("error on " + url);
            e.printStackTrace();
            return 3.0;
        } finally {
            if (conn instanceof HttpURLConnection) {
                ((HttpURLConnection) conn).disconnect();
            }
        }
    }

    public static void validateSize(String link) {
        Double fileSizeByLink = getFileSizeByLink(link);
        if (fileSizeByLink > 20) {
            throw new RuntimeException();
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
