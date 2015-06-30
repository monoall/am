package ua.org.javatraining.automessenger.app.vo;

import ua.org.javatraining.automessenger.app.entityes.Post;

import java.util.ArrayList;
import java.util.List;

public class FullPost {

    private long postID;
    private String text;
    private long date;
    private String postLocation;
    private String author;
    private String tag;
    private List<String> photos;

    public FullPost(Post post) {
        this.postID = post.getId();
        this.author = post.getNameUser();
        this.date = post.getPostDate();
        this.postLocation = post.getPostLocation();
        this.tag = post.getNameTag();
        this.text = post.getPostText();
        photos = new ArrayList<String>();
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


}
