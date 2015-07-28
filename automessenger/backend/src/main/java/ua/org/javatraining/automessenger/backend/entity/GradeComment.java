package ua.org.javatraining.automessenger.backend.entity;

import javax.persistence.*;

/**
 * Created by fisher on 28.07.15.
 */
@Entity
@Table(name = "grade_comment")
public class GradeComment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JoinColumn(name = "comment_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Comment commentId;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User userId;

    public GradeComment() {
    }

    public GradeComment(Comment commentId, User userId) {
        this.commentId = commentId;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Comment getCommentId() {
        return commentId;
    }

    public void setCommentId(Comment commentId) {
        this.commentId = commentId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "GradeComment{" +
                "id=" + id +
                ", commentId=" + commentId +
                ", userId=" + userId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GradeComment that = (GradeComment) o;

        if (!id.equals(that.id)) return false;
        if (commentId != null ? !commentId.equals(that.commentId) : that.commentId != null) return false;
        return !(userId != null ? !userId.equals(that.userId) : that.userId != null);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (commentId != null ? commentId.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }
}
