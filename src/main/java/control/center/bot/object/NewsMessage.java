package control.center.bot.object;

import org.apache.commons.lang3.StringUtils;

import java.util.Set;

public class NewsMessage {

    private String text;
    private Set<String> links;
    private Integer replyTo;
    private Integer id;
    private Double postNumber;

    public NewsMessage(Integer id, Integer postNumber, String text, Integer replyTo, Set<String> links) {
        this.text = text;
        this.links = links;
        this.replyTo = replyTo;
        this.id = id;
        this.postNumber = Double.valueOf(postNumber);
    }

    public static Boolean isValid(NewsMessage message) {
        return !message.getLinks().isEmpty() || !StringUtils.isBlank(message.getText());
    }

    public Double getPostNumber() {
        return postNumber;
    }

    public void setPostNumber(Double postNumber) {
        this.postNumber = postNumber;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Set<String> getLinks() {
        return links;
    }

    public void setLinks(Set<String> links) {
        this.links = links;
    }

    public Integer getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(Integer replyTo) {
        this.replyTo = replyTo;
    }


}
