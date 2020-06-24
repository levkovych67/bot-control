package control.center.bot.holder;

import control.center.bot.service.ContentService;
import control.center.bot.service.SentService;

import java.util.*;

public class Holder {

    private List<String> searchWords;
    private List<String> exceptionalWords;

    private Integer totalVideos;
    private Integer totalPics;

    private LinkedList<String> videos = new LinkedList<>();
    private LinkedList<String> pics = new LinkedList<>();

    private Holder() {
    }

    public Holder(List<String> searchWords, List<String> exceptionalWords) {
        this.searchWords = searchWords;
        this.exceptionalWords = exceptionalWords;
    }


    private void refreshVideos() {
        this.videos.addAll(ContentService.getVideos(searchWords, exceptionalWords));
        this.totalVideos = this.videos.size();
    }

    private void refreshPictures() {
        this.pics.addAll(ContentService.getPics(searchWords, exceptionalWords));
        this.totalPics = this.pics.size();
    }

    public Optional<String> getVideoLink() {
        String poll = videos.poll();
        if (poll == null) {
            refreshVideos();
        } else {
            return Optional.of(poll);
        }
        return Optional.empty();
    }

    public Optional<String> getVideo() {
        Optional<String> videoLink = getVideoLink();
        if (videoLink.isPresent()) {
            return verifyIfSent(videoLink.get());
        } else {
            return videoLink;
        }
    }

    public Optional<String> verifyIfSent(String link) {
        if (SentService.isSent(link)) {
            return Optional.empty();
        } else {
            SentService.addToSent(link);
            return Optional.of(link);
        }
    }

    public Optional<String> getPicture() {
        String poll = pics.poll();
        if (poll == null) {
            refreshPictures();
        } else {
            return Optional.of(poll);
        }
        return Optional.empty();
    }

}
