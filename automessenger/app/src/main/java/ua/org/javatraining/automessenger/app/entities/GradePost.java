package ua.org.javatraining.automessenger.app.entities;

import ua.org.javatraining.automessenger.app.database.UploadQueueService;

public class GradePost implements UploadQueueItemInterface {

    private long id;
    private String nameUser;
    private int idPost;
    private int grade;

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

    public int getIdPost() {
        return idPost;
    }

    public void setIdPost(int idPost) {
        this.idPost = idPost;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
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
        return UploadQueueService.POST_GRADE;
    }
}
