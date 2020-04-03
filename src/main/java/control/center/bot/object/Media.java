package control.center.bot.object;


import control.center.bot.util.TYPE;

public class Media {

    private TYPE type;
    private String link;

    public static Media photo(String link) {
        Media media = new Media();
        media.setType(TYPE.PICTURE);
        media.setLink(link);
        return media;
    }

    public static Media video(String link) {
        Media media = new Media();
        media.setType(TYPE.VIDEO);
        media.setLink(link);
        return media;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
