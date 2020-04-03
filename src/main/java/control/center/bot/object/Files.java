package control.center.bot.object;

import com.fasterxml.jackson.annotation.JsonProperty;

import static control.center.bot.configuration.Configuration.TWO_CH_URL;

public class Files {


    @JsonProperty("path")
    private String link;

    @JsonProperty("thumbnail")
    private String thumbnail;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = TWO_CH_URL + link;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = TWO_CH_URL + thumbnail;
    }
}
