package control.center.bot.object;

import java.util.List;

public class GoogleNewsBlock {

    private String status;
    private Integer totalResult;
    private List<GoogleNews> articles;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTotalResult() {
        return totalResult;
    }

    public void setTotalResult(Integer totalResult) {
        this.totalResult = totalResult;
    }

    public List<GoogleNews> getArticles() {
        return articles;
    }

    public void setArticles(List<GoogleNews> articles) {
        this.articles = articles;
    }
}
