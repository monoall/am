package ua.org.javatraining.automessenger.backend.vo;

/**
 * Created by fisher on 05.08.15.
 */

public class PostGrades {

    private int sumGrade;
    private int userGrade = 0;
    private long postID;
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

    public long getPostID() {
        return postID;
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


}
