package ua.org.javatraining.automessenger.app.entityes;

public class GradeComment {

    private long id;
    private int idUser;
    private int idComment;
    private int grade = 0;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
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

}
