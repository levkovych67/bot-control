package control.center.bot.thunder;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileService {

    public static File downloadFromUrl(String link) {
        if (!link.contains("https")) {
            link = "https://" + link;
        }
        String fileName = link.substring(link.lastIndexOf("/") + 1);
        final File file = new File(Configuration.chanelFolderPath + fileName);
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

    public static Boolean isSentByCheckSum(String checkSum) {
        return PropertyService.getCheckSums().contains(checkSum);
    }

    public static Boolean isSentByLink(String link) {
        boolean isSent = PropertyService.getLinks().contains(link.replace("https://", ""));
        if (isSent) {
            System.out.println(link + " is sent");
        }
        return isSent;
    }

    public static String convertWebmToMp4(String oldPath) {
        String command = String.format("ffmpeg -i %s -c:v libx264 -c:a aac -strict experimental -b:a 192k %s",
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

    public static String getFileCheckSum(String path) {
        try (InputStream is = Files.newInputStream(Paths.get(path))) {
            return org.apache.commons.codec.digest.DigestUtils.md5Hex(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean deleteFile(String path) {
        return new File(path).delete();
    }

    public static String process(String link) {
        if (link.contains(".webm")) {
            String path = processWebm(link);
            if (path == null) {
                return null;
            } else {
                PropertyService.addCheckSum(getFileCheckSum(path));
                return path;
            }
        }

        if (link.contains(".mp4")) {
            String path = processFile(link);
            if (path == null) {
                return null;
            } else {
                PropertyService.addCheckSum(getFileCheckSum(path));
                return path;
            }
        } else {
            return link;
        }
    }

    private static String processFile(String link) {
        File file = downloadFromUrl(link);
        String fileCheckSum = getFileCheckSum(file.getAbsolutePath());
        if (isSentByCheckSum(fileCheckSum)) {
            return null;
        }
        return file.getAbsolutePath();
    }

    private static String processWebm(String link) {
        File file = downloadFromUrl(link);
        String fileCheckSum = getFileCheckSum(file.getAbsolutePath());
        if (isSentByCheckSum(fileCheckSum)) {
            System.out.println(link + " already sent removing");
            deleteFile(file.getAbsolutePath());
            return null;
        }
        String path = convertWebmToMp4(file.getAbsolutePath());
        deleteFile(file.getAbsolutePath());
        return path;
    }
}
