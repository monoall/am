package ua.org.javatraining.automessenger.app.entityes;

/**
 * Created by berkut on 06.06.15.
 */
public class Photo {

    private long id;
    private String photoLink;
    private int idPost;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhotoLink() {
        return photoLink;
    }

    public void setPhotoLink(String photoLink) {
        this.photoLink = photoLink;
    }

    public int getIdPost() {
        return idPost;
    }

    public void setIdPost(int idPost) {
        this.idPost = idPost;
    }
}
