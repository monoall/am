package ua.org.javatraining.automessenger.app.entities;

import ua.org.javatraining.automessenger.app.database.UploadQueueService;

public class Comment implements UploadQueueItemInterface {

    private long id;
    private long commentDate;
    private String commentText;
    private String nameUser;
    private long idPost;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(long commentDate) {
        this.commentDate = commentDate;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public long getIdPost() {
        return idPost;
    }

    public void setIdPost(long idPost) {
        this.idPost = idPost;
    }

    @Override
    public String getIdentifier() {
        return String.valueOf(id);
    }

    @Override
    public int getDataTpe() {
        return UploadQueueService.COMMENT;
    }
}
