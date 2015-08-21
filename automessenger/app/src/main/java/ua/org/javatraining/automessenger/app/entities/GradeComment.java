package ua.org.javatraining.automessenger.app.entities;

import ua.org.javatraining.automessenger.app.database.UploadQueueService;

public class GradeComment implements UploadQueueItemInterface {

    private long id;
    private String userId;
    private int commentId;
    private int grade = 0;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade){
        this.grade = grade;
    }

    public void increaseGrade(){
        grade++;
    }

    public void decreaseGrade(){
        grade--;
    }

    @Override
    public String getIdentifier() {
        return String.valueOf(id);
    }

    @Override
    public int getDataTpe() {
        return UploadQueueService.COMMENT_GRADE;
    }
}
