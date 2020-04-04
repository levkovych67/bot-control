package control.center.bot.service;

import control.center.bot.object.Post;
import control.center.bot.object.ThreadHead;
import control.center.bot.scrapers.ThreadService;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static control.center.bot.service.PostService.getPicsFromPosts;
import static control.center.bot.service.PostService.getVideosFromPosts;


public interface ContentService {

    static Set<String> getVideos(List<String> searchWords, List<String> exceptionalWords) {
        Set<Post> posts = getPosts(searchWords, exceptionalWords);
        return getVideosFromPosts(posts);
    }

    static Set<String> getPics(List<String> searchWords, List<String> exceptionalWords) {
        Set<Post> posts = getPosts(searchWords, exceptionalWords);
        return getPicsFromPosts(posts);
    }

    static Set<Post> getPosts(List<String> searchWords, List<String> exceptionalWords) {
        Set<ThreadHead> threadHeads = ThreadService.searchByWordsWithExceptions(searchWords, exceptionalWords);
        return threadHeads.stream().map(e -> PostService.getAllPostsFromThread(e.getId()))
                .flatMap(Collection::stream).collect(Collectors.toSet());
    }


}
