package ua.org.javatraining.automessenger.backend.vo;

import ua.org.javatraining.automessenger.backend.entity.Comment;
import ua.org.javatraining.automessenger.backend.entity.Post;
import ua.org.javatraining.automessenger.backend.entity.User;

import java.util.Date;

/**
 * Created by fisher on 05.08.15.
 */

public class SuperComment {

    private long id;
    private Date commentDate;
    private String commentText;
    private User author;
    private Post postID;
    private int gradeNumber;
    private int userGrade;

    public SuperComment(Comment c) {
        this.id = c.getId();
        this.commentDate = c.getCommentDate();
        this.commentText = c.getCommentText();
        this.author = c.getUserId();
        this.postID = c.getPostId();
    }

    public SuperComment() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Post getPostID() {
        return postID;
    }

    public void setPostID(Post postID) {
        this.postID = postID;
    }

    public int getGradeNumber() {
        return gradeNumber;
    }

    public void setGradeNumber(int gradeNumber) {
        this.gradeNumber = gradeNumber;
    }

    public int getUserGrade() {
        return userGrade;
    }

    public void setUserGrade(int userGrade) {
        this.userGrade = userGrade;
    }
}
