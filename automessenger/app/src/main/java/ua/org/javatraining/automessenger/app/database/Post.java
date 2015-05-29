package ua.org.javatraining.automessenger.app.database;

// не завершено

import android.content.Context;

public class Post {

    private String postText;
    private int postDate;
    private String postLocation;
    private int idUser;
    private int idTag;

    private SQLiteAdapter sqLiteAdapter;

    public Post(Context context, String postText, int postDate, String postLocation, int idUser, int idTag){
        this.postText = postText;
        this.postDate = postDate;
        this.postLocation = postLocation;
        this.idUser = idUser;
        this.idTag = idTag;
        sqLiteAdapter = SQLiteAdapter.initInstance(context);
        sqLiteAdapter.openToWrite();
        sqLiteAdapter.insertToPost(idUser, postText, postDate, postLocation, idTag);
    }



}