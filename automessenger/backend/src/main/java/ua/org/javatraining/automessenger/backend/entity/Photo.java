package ua.org.javatraining.automessenger.backend.entity;

import javax.persistence.*;
/**
 * Created by fisher on 28.07.15.
 */

@Entity
@Table(name = "photo")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "username_user")
    @ManyToOne(fetch = FetchType.LAZY)
    private User userId;

    @Column(name = "id_post")
    @ManyToOne(fetch = FetchType.LAZY)
    private Post postId;

    public Photo() {
    }

    public Photo(User userId, Post postId) {
        this.userId = userId;
        this.postId = postId;
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

    @Override
    public String toString() {
        return "Photo{" +
                "id=" + id +
                ", userId=" + userId +
                ", postId=" + postId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Photo photo = (Photo) o;

        if (!id.equals(photo.id)) return false;
        if (userId != null ? !userId.equals(photo.userId) : photo.userId != null) return false;
        return !(postId != null ? !postId.equals(photo.postId) : photo.postId != null);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (postId != null ? postId.hashCode() : 0);
        return result;
    }
}
