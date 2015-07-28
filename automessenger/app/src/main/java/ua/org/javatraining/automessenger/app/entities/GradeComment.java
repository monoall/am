package ua.org.javatraining.automessenger.app.entities;

import ua.org.javatraining.automessenger.app.database.UploadQueueService;

public class GradeComment implements UploadQueueItemInterface {

    private long id;
    private String nameUser;
    private int idComment;
    private int grade = 0;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public int getIdComment() {
        return idComment;
    }

    public void setIdComment(int idComment) {
        this.idComment = idComment;
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
