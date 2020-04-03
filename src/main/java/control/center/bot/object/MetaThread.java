package control.center.bot.object;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MetaThread {

    @JsonProperty("posts")
    private List<Post> posts;


    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
