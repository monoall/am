package ua.org.javatraining.automessenger.app.entityes;

/**
 * Created by berkut on 17.06.15.
 */
public class Cash {

    private long id;
    private String cash;
    private int idPost;

    public int getIdPost() {
        return idPost;
    }

    public void setIdPost(int idPost) {
        this.idPost = idPost;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }
}
