package ua.org.javatraining.automessenger.app.vo;

import android.util.Log;
import ua.org.javatraining.automessenger.app.entities.Photo;
import ua.org.javatraining.automessenger.app.entities.Post;

import java.util.ArrayList;
import java.util.List;

public class FullPost {

    private long postID;
    private String text;
    private long date;
    private String postLocation;
    private String author;
    private String tag;
    private String locCountry;
    private String locAdminArea;
    private String locRegion;
    private List<String> photos;
    private int commentCount;

    public FullPost(Post post) {
        if (post != null) {
            this.postID = post.getId();
            this.author = post.getNameUser();
            this.date = post.getPostDate();
            this.postLocation = post.getPostLocation();
            this.tag = post.getNameTag();
            this.text = post.getPostText();
            this.locCountry = post.getLocCountry();
            this.locAdminArea = post.getLocAdminArea();
            this.locRegion = post.getLocRegion();
            photos = new ArrayList<String>();
        }
    }

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

    public void separate(Post post, Photo photo) {
        if (post != null) {
            post.setId(this.postID);
            post.setPostText(this.text);
            post.setPostDate(this.date);
            post.setPostLocation(this.postLocation);
            post.setNameUser(this.author);
            post.setNameTag(this.tag);
            post.setLocCountry(this.locCountry);
            post.setLocAdminArea(this.locAdminArea);
            post.setLocRegion(this.locRegion);
        }

        if (photo != null) {
            photo.setPhotoLink(this.photos.get(0));
        }
    }

    public FullPost() {
        photos = new ArrayList<String>();
    }

    public long getPostID() {
        return postID;
    }

    public void setPostID(long postID) {
        this.postID = postID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getPostLocation() {
        return postLocation;
    }

    public void setPostLocation(String postLocation) {
        this.postLocation = postLocation;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}
