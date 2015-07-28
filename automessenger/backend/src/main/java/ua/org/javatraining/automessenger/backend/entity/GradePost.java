package ua.org.javatraining.automessenger.backend.entity;

import javax.persistence.*;

/**
 * Created by fisher on 28.07.15.
 */

@Entity
@Table(name = "grade_post")
public class GradePost {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JoinColumn(name = "comment_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Comment commentId;

    @JoinColumn(name = "username_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User userId;

    public GradePost() {
    }

    public GradePost(Comment commentId, User userId) {
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
        return "GradePost{" +
                "id=" + id +
                ", commentId=" + commentId +
                ", userId=" + userId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GradePost gradePost = (GradePost) o;

        if (!id.equals(gradePost.id)) return false;
        if (commentId != null ? !commentId.equals(gradePost.commentId) : gradePost.commentId != null) return false;
        return !(userId != null ? !userId.equals(gradePost.userId) : gradePost.userId != null);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (commentId != null ? commentId.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }
}
