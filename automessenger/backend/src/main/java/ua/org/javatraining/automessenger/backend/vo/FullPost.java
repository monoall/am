package ua.org.javatraining.automessenger.backend.vo;

import ua.org.javatraining.automessenger.backend.entity.Photo;
import ua.org.javatraining.automessenger.backend.entity.Post;
import ua.org.javatraining.automessenger.backend.entity.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fisher on 05.08.15.
 */

public class FullPost {

    private long postID;
    private String text;
    private Date date;
    private String postLocation;
    private User author;
    private String tag;
    private String locCountry;
    private String locAdminArea;
    private String locRegion;
    private List<String> photos;
    private int commentCount;

    public FullPost(Post post) {
        if (post != null) {
            this.postID = post.getId();
            this.author = post.getUserId();
            this.date = post.getPostDate();
            this.postLocation = post.getPostLocation();
            this.tag = post.getTagName();
            this.text = post.getPostText();
            this.locCountry = post.getLocationCountry();
            this.locAdminArea = post.getLocationAdminArea();
            this.locRegion = post.getLocationRegion();
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
            post.setUserId(this.author);
            post.setTagName(this.tag);
            post.setLocationCountry(this.locCountry);
            post.setLocationAdminArea(this.locAdminArea);
            post.setLocationRegion(this.locRegion);
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPostLocation() {
        return postLocation;
    }

    public void setPostLocation(String postLocation) {
        this.postLocation = postLocation;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
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
