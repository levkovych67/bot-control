package control.center.bot.scrapers;


import control.center.bot.object.Board;
import control.center.bot.object.NewsThread;
import control.center.bot.object.ThreadHead;
import control.center.bot.util.TYPE;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;
import java.util.stream.Collectors;

import static control.center.bot.util.Util.*;

public class ThreadService {

    public static Set<ThreadHead> getOpPostsFromB() {
        try {
            HashSet<ThreadHead> collect =
                    new RestTemplate().getForEntity("https://2ch.hk/b/catalog.json", Board.class)
                            .getBody()
                            .getThreads()
                            .stream()
                            .collect(Collectors.toCollection(HashSet::new));
            return collect;
        } catch (Exception e) {
            System.out.println("Не зміг потянути треди " + e.getMessage());
            return new HashSet<>();
        }
    }

    public static void main(String[] args) {
        Set<ThreadHead> all = getOpPostsFromB();
        System.out.println(all);
    }

    public static Set<ThreadHead> getNews(Integer limit) {
        return getOpPostsFromB().stream()
                .sorted(Comparator.comparingInt(ThreadHead::getPostCount).reversed())
                .limit(limit)
                .collect(Collectors.toSet());
    }

    public static Set<ThreadHead> searchByWords(List<String> searchWords) {
        Set<ThreadHead> all = getOpPostsFromB();
        return all.stream().filter(e -> containsInName(e, searchWords)).collect(Collectors.toSet());
    }

    public static Set<ThreadHead> searchByWordsWithExceptions(List<String> searchWords, List<String> exceptions) {
        Set<ThreadHead> all = getOpPostsFromB();
        return all.stream()
                .filter(e -> containsInName(e, searchWords))
                .filter(e -> !containsInName(e, exceptions))
                .collect(Collectors.toSet());
    }

    private static Boolean containsInName(ThreadHead threadHead, List<String> words) {
        boolean contains = false;
        String threadName = threadHead.getText();
        for (String word : words) {
            if (StringUtils.containsIgnoreCase(threadName, word)) {
                contains = true;
                break;
            }
        }
        return contains;
    }




    }
