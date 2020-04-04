package control.center.bot.thunder;


import java.net.HttpURLConnection;
import java.net.URL;

public class LinkUtils {

    public static Boolean valid(String url) {
        try {
            URL u = new URL(url);
            HttpURLConnection huc = (HttpURLConnection) u.openConnection();
            huc.setRequestMethod("GET");
            huc.connect();
            int code = huc.getResponseCode();
            return code <= 300;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return true;

    }

    public static boolean isSentByLink(String link) {
        return FileService.isSentByLink(link);
    }

    public static boolean isGoodWeight(String link) {
        Double size = FileService.getFileSizeByLink(link);
        return size < 5;
    }

    public static boolean isVideo(String link) {
        return link.contains(".mp4") || link.contains(".webm") || link.contains(".gif");
    }

    public static boolean linkIsGoodForTelegram(String link) {
        return LinkUtils.valid(link) && LinkUtils.isGoodWeight(link) && !LinkUtils.isSentByLink(link);
    }
}
