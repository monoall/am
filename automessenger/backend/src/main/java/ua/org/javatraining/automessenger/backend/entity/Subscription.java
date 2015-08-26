package ua.org.javatraining.automessenger.backend.entity;

import javax.persistence.*;

/**
 * Created by fisher on 26.07.15.
 */
@Entity
@Table(name = "subscriptions")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private User userId;

    @JoinColumn(name = "tag_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Tag tagId;

    public Subscription() {
    }

    public Subscription(User userId, Tag tagId) {
        this.userId = userId;
        this.tagId = tagId;
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

    public Tag getTagId() {
        return tagId;
    }

    public void setTagId(Tag tagId) {
        this.tagId = tagId;
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "id=" + id +
                ", userId=" + userId +
                ", tagId=" + tagId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Subscription that = (Subscription) o;

        if (!id.equals(that.id)) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        return !(tagId != null ? !tagId.equals(that.tagId) : that.tagId != null);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (tagId != null ? tagId.hashCode() : 0);
        return result;
    }
}
