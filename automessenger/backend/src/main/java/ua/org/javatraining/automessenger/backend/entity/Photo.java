package ua.org.javatraining.automessenger.backend.entity;

import javax.persistence.*;
/**
 * Created by fisher on 28.07.15.
 */

@Entity
@Table(name = "photo")
@NamedQuery(name = "Photo.findByPostId",
        query = "SELECT p FROM Photo p WHERE p.postId = ?1")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User userId;

    @JoinColumn(name = "post_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Post postId;

    private String photoLink;

    public Photo() {
    }

    public Photo(User userId, Post postId, String photoLink) {
        this.userId = userId;
        this.postId = postId;
        this.photoLink = photoLink;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public Post getPostId() {
        return postId;
    }

    public void setPostId(Post postId) {
        this.postId = postId;
    }

    public String getPhotoLink() {
        return photoLink;
    }

    public void setPhotoLink(String photoLink) {
        this.photoLink = photoLink;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "id=" + id +
                ", userId=" + userId +
                ", postId=" + postId +
                ", photoLink='" + photoLink + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Photo photo = (Photo) o;

        if (!id.equals(photo.id)) return false;
        if (userId != null ? !userId.equals(photo.userId) : photo.userId != null) return false;
        if (postId != null ? !postId.equals(photo.postId) : photo.postId != null) return false;
        return !(photoLink != null ? !photoLink.equals(photo.photoLink) : photo.photoLink != null);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (postId != null ? postId.hashCode() : 0);
        result = 31 * result + (photoLink != null ? photoLink.hashCode() : 0);
        return result;
    }
}
