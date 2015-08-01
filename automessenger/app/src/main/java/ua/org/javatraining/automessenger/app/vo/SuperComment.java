package ua.org.javatraining.automessenger.app.vo;

import ua.org.javatraining.automessenger.app.entities.Comment;

public class SuperComment {

    private long id;
    private long commentDate;
    private String commentText;
    private String author;
    private long postID;
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

    public long getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(long commentDate) {
        this.commentDate = commentDate;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getPostID() {
        return postID;
    }

    public void setPostID(long postID) {
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
