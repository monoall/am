package ua.org.javatraining.automessenger.app.entityes;

public class GradePost {

    private long id;
    private int idUser;
    private int idPost;
    private int grade;

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
}
