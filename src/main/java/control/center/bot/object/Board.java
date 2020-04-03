package control.center.bot.object;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedList;

public class Board {

    @JsonProperty("threads")
    private LinkedList<ThreadHead> threads;

    public LinkedList<ThreadHead> getThreads() {
        return threads;
    }

    public void setThreads(LinkedList<ThreadHead> threads) {
        this.threads = threads;
    }
}
