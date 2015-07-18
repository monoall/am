package ua.org.javatraining.automessenger.app.vo;

public class CommentGrades {

    private int sumGrade;
    private int userGrade = 0;
    private long commentID;
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getSumGrade() {
        return sumGrade;
    }

    public int getUserGrade() {
        return userGrade;
    }

    public long getCommentID() {
        return commentID;
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

}
