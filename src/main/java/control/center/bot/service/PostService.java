package control.center.bot.service;


import control.center.bot.object.Files;
import control.center.bot.object.Post;
import control.center.bot.object.Thread;
import control.center.bot.util.Util;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostService {

    public static Set<Post> getAllPostsFromThread(String id) {
        final String url = "https://2ch.hk/b/res/" + id + ".json";
        try {
            return new HashSet<>(
                    new RestTemplate().getForEntity(url, Thread.class)
                            .getBody()
                            .getMetaThreads()
                            .get(0)
                            .getPosts());
        } catch (Exception e) {
            System.out.println("Не зміг стянути пости з треда " + url + " " + e.getMessage());
            return new HashSet<>();
        }
    }
    public static Set<Post> getAllPostsFromThreadByLink(String threadUrl) {
        final String url = threadUrl.replace(".html",".json");
        try {
            return new HashSet<>(
                    new RestTemplate().getForEntity(url, Thread.class)
                            .getBody()
                            .getMetaThreads()
                            .get(0)
                            .getPosts());
        } catch (Exception e) {
            System.out.println("Не зміг стянути пости з треда " + url + " " + e.getMessage());
            return new HashSet<>();
        }
    }

    public static Set<String> getVideosFromPosts(Set<Post> posts) {
        return posts.stream().flatMap(e -> e.getFiles().stream()).map(Files::getLink).filter(Util::isVideo).collect(Collectors.toSet());
    }

    public static Set<String> getPicsFromPosts(Set<Post> posts) {
        return posts.stream().flatMap(e -> e.getFiles().stream()).map(Files::getLink).filter(Util::isPic).collect(Collectors.toSet());
    }
}
