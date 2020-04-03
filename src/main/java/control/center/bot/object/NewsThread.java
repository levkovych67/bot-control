package control.center.bot.object;

import control.center.bot.configuration.Configuration;
import control.center.bot.util.Util;


public class NewsThread {

    private String text;
    private Media media;
    private String link;

    public NewsThread(ThreadHead threadHead) {

        String text = "<i>" + threadHead.getText() +
                "</i>\n<b>Постов : </b>" +
                threadHead.getPostCount();

        this.setText(text);

        final String link =
                threadHead.getFiles().stream()
                        .map(Files::getLink)
                        .filter(e -> Util.isPic(e) || Util.isVideo(e))
                        .findAny().orElse("https://www.meme-arsenal.com/memes/12b0a39ecbe9e0802ff778f8f5fb31cd.jpg");

        if (Util.isVideo(link)) {
            this.setMedia(Media.video(link));
        }

        if (Util.isPic(link)) {
            this.setMedia(Media.photo(link));
        }

        final String url = Configuration.TWO_CH_URL + "/b" + "/res/" + threadHead.getId() + ".html";
        this.setLink(url);


    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }
}
