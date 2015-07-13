package ua.org.javatraining.automessenger.app.vo;

public class PostGrades {

    private int sumGrade;
    private int userGrade = 0;
    private long postID;
    private long userID;

    public int getSumGrade() {
        return sumGrade;
    }

    public int getUserGrade() {
        return userGrade;
    }

    public long getPostID() {
        return postID;
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

    public void setPostID(long postID) {
        this.postID = postID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }
}
