package control.center.bot.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import control.center.bot.util.Util;

import java.util.List;

public class ThreadHead {

    @JsonProperty("comment")
    private String text;

    @JsonProperty("files")
    private List<Files> files;

    @JsonProperty("num")
    private String id;

    @JsonProperty("posts_count")
    private Integer postCount;

    public Integer getPostCount() {
        return postCount;
    }

    public void setPostCount(Integer postCount) {
        this.postCount = postCount;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = Util.form(text);
    }

    public List<Files> getFiles() {
        return files;
    }

    public void setFiles(List<Files> files) {
        this.files = files;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
