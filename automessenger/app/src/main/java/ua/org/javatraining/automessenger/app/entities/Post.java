package ua.org.javatraining.automessenger.app.entities;

import ua.org.javatraining.automessenger.app.database.UploadQueueService;

public class Post implements UploadQueueItemInterface{

    public static final String POST_ID = "ua.org.javatraining.automessenger.app.entityes.POST_ID";

    private long id;
    private String postText;
    private long postDate;
    private String postLocation;
    private String nameUser;
    private String nameTag;
    private String locCountry;
    private String locAdminArea;
    private String locRegion;

    public String getLocCountry() {
        return locCountry;
    }

    public void setLocCountry(String locCountry) {
        this.locCountry = locCountry;
    }

    public String getLocAdminArea() {
        return locAdminArea;
    }

    public void setLocAdminArea(String locAdminArea) {
        this.locAdminArea = locAdminArea;
    }

    public String getLocRegion() {
        return locRegion;
    }

    public void setLocRegion(String locRegion) {
        this.locRegion = locRegion;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getNameTag() {
        return nameTag;
    }

    public void setNameTag(String nameTag) {
        this.nameTag = nameTag;
    }

    public String getPostLocation() {
        return postLocation;
    }

    public void setPostLocation(String postLocation) {
        this.postLocation = postLocation;
    }

    public long getPostDate() {
        return postDate;
    }

    public void setPostDate(long postDate) {
        this.postDate = postDate;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    @Override
    public String getIdentifier() {
        return String.valueOf(id);
    }

    @Override
    public int getDataTpe() {
        return UploadQueueService.POST;
    }
}