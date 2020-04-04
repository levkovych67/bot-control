package control.center.bot.thunder;


import control.center.bot.object.Board;
import control.center.bot.object.Thread;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class CoreService {

    private static RestTemplate template = new RestTemplate();

    private static boolean filterOnlyWebmThread(String name) {
        return StringUtils.containsIgnoreCase(name, "mp4") ||
                StringUtils.containsIgnoreCase(name, "webm") ||
                StringUtils.containsIgnoreCase(name, "mp3") ||
                StringUtils.containsIgnoreCase(name, "шебм") ||
//                StringUtils.containsIgnoreCase(name, "фап") ||
//                StringUtils.containsIgnoreCase(name, "прон") ||
//                StringUtils.containsIgnoreCase(name, "fap") ||
                StringUtils.containsIgnoreCase(name, "обосрался") ||
                StringUtils.containsIgnoreCase(name, "жпег") ||
                StringUtils.containsIgnoreCase(name, "засмеялся");
    }

    private static boolean filterBlack(String name) {
        return !StringUtils.containsIgnoreCase(name, "black");
    }

    private static boolean filterAnime(String name) {
        return !StringUtils.containsIgnoreCase(name, "аниме") ||
                !StringUtils.containsIgnoreCase(name, "anime") ||
                !StringUtils.containsIgnoreCase(name, "ониме");
    }

    static Set<String> getAllLinks() {
        return template.getForEntity("https://2ch.hk/b/catalog.json", Board.class)
                .getBody()
                .getThreads()
                .stream()
                .filter(e -> filterBlack(Utils.html2text(e.getText())))
                .filter(e -> filterAnime(Utils.html2text(e.getText())))
                .filter(e -> filterOnlyWebmThread(Utils.html2text(e.getText())))
                .map(e -> getLinksFromPosts(Long.valueOf(e.getId())))
                .flatMap(Collection::stream)
                .filter(LinkUtils::isVideo)
                .collect(Collectors.toCollection(HashSet::new));

    }

    private static Set<String> getLinksFromPosts(Long id) {
        final String url = Configuration.DVACH_URL + "/b/res/" + id + ".json";
        return template.getForEntity(url, Thread.class).getBody()
                .getMetaThreads()
                .get(0)
                .getPosts()
                .stream()
                .map(post -> post.getFiles().stream().map(e -> Configuration.DVACH_URL + e.getLink()).collect(Collectors.toSet()))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }


}
