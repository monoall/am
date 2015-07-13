package ua.org.javatraining.automessenger.app.vo;

public class CommentGrades {

    private int sumGrade;
    private int userGrade = 0;
    private long commentID;
    private long userID;

    public int getSumGrade() {
        return sumGrade;
    }

    public int getUserGrade() {
        return userGrade;
    }

    public long getCommentID() {
        return commentID;
    }

    public long getUserID() {
        return userID;
    }

    public void setSumGrade(int sumGrade) {
        this.sumGrade = sumGrade;
    }

    public void setUserGrade(int userGrade) {
        this.userGrade = userGrade;
    }

    public void setCommentID(long commentID) {
        this.commentID = commentID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }
}
