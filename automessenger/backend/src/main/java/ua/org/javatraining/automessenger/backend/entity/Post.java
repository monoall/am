package ua.org.javatraining.automessenger.backend.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Created by fisher on 26.07.15.
 */
@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "post_text")
    private String postText;

    @Column(name = "post_date")
    private Date postDate;

    @Column(name = "post_location")
    private String postLocation;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private User userId;

    @Column(name = "tag_name")
    private String tagName;

    @Column(name = "loc_country")
    private String locationCountry;

    @Column(name = "loc_admin_area")
    private String locationAdminArea;

    @Column(name = "loc_region")
    private String locationRegion;

    @OneToMany(mappedBy = "postId")
    private Set<Photo> photos;


    public Post() {
    }

    public Post(String postText, Date postDate, String postLocation, User userId, String tagName) {
        this.postText = postText;
        this.postDate = postDate;
        this.postLocation = postLocation;
        this.userId = userId;
        this.tagName = tagName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public String getPostLocation() {
        return postLocation;
    }

    public void setPostLocation(String postLocation) {
        this.postLocation = postLocation;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getLocationCountry() {
        return locationCountry;
    }

    public void setLocationCountry(String locationCountry) {
        this.locationCountry = locationCountry;
    }

    public String getLocationAdminArea() {
        return locationAdminArea;
    }

    public void setLocationAdminArea(String locationAdminArea) {
        this.locationAdminArea = locationAdminArea;
    }

    public String getLocationRegion() {
        return locationRegion;
    }

    public void setLocationRegion(String locationRegion) {
        this.locationRegion = locationRegion;
    }

    public Set<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(Set<Photo> photos) {
        this.photos = photos;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", postText='" + postText + '\'' +
                ", postDate=" + postDate +
                ", postLocation='" + postLocation + '\'' +
                ", userId=" + userId +
                ", tagName='" + tagName + '\'' +
                ", locationCountry='" + locationCountry + '\'' +
                ", locationAdminArea='" + locationAdminArea + '\'' +
                ", locationRegion='" + locationRegion + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Post post = (Post) o;

        if (!id.equals(post.id)) return false;
        if (postText != null ? !postText.equals(post.postText) : post.postText != null) return false;
        if (postDate != null ? !postDate.equals(post.postDate) : post.postDate != null) return false;
        if (postLocation != null ? !postLocation.equals(post.postLocation) : post.postLocation != null) return false;
        if (userId != null ? !userId.equals(post.userId) : post.userId != null) return false;
        if (tagName != null ? !tagName.equals(post.tagName) : post.tagName != null) return false;
        if (locationCountry != null ? !locationCountry.equals(post.locationCountry) : post.locationCountry != null)
            return false;
        if (locationAdminArea != null ? !locationAdminArea.equals(post.locationAdminArea) : post.locationAdminArea != null)
            return false;
        return !(locationRegion != null ? !locationRegion.equals(post.locationRegion) : post.locationRegion != null);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (postText != null ? postText.hashCode() : 0);
        result = 31 * result + (postDate != null ? postDate.hashCode() : 0);
        result = 31 * result + (postLocation != null ? postLocation.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (tagName != null ? tagName.hashCode() : 0);
        result = 31 * result + (locationCountry != null ? locationCountry.hashCode() : 0);
        result = 31 * result + (locationAdminArea != null ? locationAdminArea.hashCode() : 0);
        result = 31 * result + (locationRegion != null ? locationRegion.hashCode() : 0);
        return result;
    }
}
