package ua.org.javatraining.automessenger.app.vo;

public class ExtTag {

    public ExtTag() {
    }

    public ExtTag(String tagName, boolean isSubscribed) {
        this.tagName = tagName;
        this.isSubscribed = isSubscribed;
    }

    private String tagName;

    private boolean isSubscribed;

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public boolean isSubscribed() {
        return isSubscribed;
    }

    public void setIsSubscribed(boolean isSubscribed) {
        this.isSubscribed = isSubscribed;
    }
}
