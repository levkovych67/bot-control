package control.center.bot.object;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Thread {

    @JsonProperty("Board")
    private String board;

    @JsonProperty("BoardInfo")
    private String boardInfo;

    @JsonProperty("current_thread")
    private String id;

    @JsonProperty("posts_count")
    private Integer postCount;

    @JsonProperty("threads")
    private List<MetaThread> metaThreads;

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }


    public String getBoardInfo() {
        return boardInfo;
    }

    public void setBoardInfo(String boardInfo) {
        this.boardInfo = boardInfo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getPostCount() {
        return postCount;
    }

    public void setPostCount(Integer postCount) {
        this.postCount = postCount;
    }

    public List<MetaThread> getMetaThreads() {
        return metaThreads;
    }

    public void setMetaThreads(List<MetaThread> metaThreads) {
        this.metaThreads = metaThreads;
    }
}
