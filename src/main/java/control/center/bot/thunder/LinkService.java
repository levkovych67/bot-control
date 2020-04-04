package control.center.bot.thunder;


import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Set;


public class LinkService {

    private static LinkedList<String> links = new LinkedList<>();

    private static void refreshList() {
        if (links.size() < 80) {
            Set<String> allLinks = CoreService.getAllLinks();
            allLinks.removeAll(PropertyService.getLinks());
            links.addAll(allLinks);
        }
    }

    public static SendPhoto createPhotoMessage(String link) {
        return new SendPhoto()
                .setPhoto(link.substring(link.lastIndexOf("/" + 1)), createSteam(link))
                .setCaption("Канал скоро умре нахуй, перекат канала в Блискавка 2.0 https://t.me/joinchat/AAAAAETS87l2Y-yRt1iOXg")
                .setChatId(Configuration.welcomebackdrug_id);
    }

    public static SendVideo createVideoMessage(String link) {
        if (link.contains("https://")) {
            return new SendVideo()
                    .setChatId(Configuration.welcomebackdrug_id)
                    .setCaption("Канал скоро умре нахуй, перекат канала в Блискавка 2.0 https://t.me/joinchat/AAAAAETS87l2Y-yRt1iOXg")
                    .setVideo(link.substring(link.lastIndexOf("/") + 1), createSteam(link));
        } else {
            return new SendVideo()
                    .setChatId(Configuration.welcomebackdrug_id)
                    .setCaption("Канал скоро умре нахуй, перекат канала в Блискавка 2.0 https://t.me/joinchat/AAAAAETS87l2Y-yRt1iOXg")
                    .setVideo(new File(link));
        }
    }

    public static InputStream createSteam(String link) {
        try {
            return new URL(link).openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String getLink() {
        refreshList();
        Collections.shuffle(links);
        String link = poll();
        if (LinkUtils.linkIsGoodForTelegram(link)) {
            System.out.println("CONTENT SIZE IS : " + links.size());
            PropertyService.addLink(link);
            return link;
        } else {
            return getLink();
        }
    }

    private static String poll() {
        final String poll = links.poll();
        if (poll == null) {
            refreshList();
            return poll();
        } else {
            return poll;
        }
    }

}
