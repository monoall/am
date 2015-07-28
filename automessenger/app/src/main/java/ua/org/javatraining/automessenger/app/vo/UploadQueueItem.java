package ua.org.javatraining.automessenger.app.vo;

public class UploadQueueItem {
    private long id;
    private int contentType;
    private String contentIdentifier;
    private String extraText1;
    private String extraText2;

    public UploadQueueItem() {
    }

    public UploadQueueItem(int contentType, String contentIdentifier) {
        this.contentType = contentType;
        this.contentIdentifier = contentIdentifier;
    }

    public String getExtraText1() {
        return extraText1;
    }

    public void setExtraText1(String extraText1) {
        this.extraText1 = extraText1;
    }

    public String getExtraText2() {
        return extraText2;
    }

    public void setExtraText2(String extraText2) {
        this.extraText2 = extraText2;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public String getContentIdentifier() {
        return contentIdentifier;
    }

    public void setContentIdentifier(String contentIdentifier) {
        this.contentIdentifier = contentIdentifier;
    }
}
