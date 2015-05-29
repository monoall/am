package ua.org.javatraining.automessenger.app.database;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by berkut on 29.05.15.
 */
public class User {

    private String nameUser;
    private SQLiteAdapter sqLiteAdapter;
    private Context context;

    public User(Context context, String nameUser){
        this.context = context;
        this.nameUser = nameUser;
        sqLiteAdapter = SQLiteAdapter.initInstance(context);
        sqLiteAdapter.openToWrite();
        sqLiteAdapter.insertToUser(nameUser);
    }

    public void insertPost(String postText, String tagName){
        sqLiteAdapter.openToRead();
        int idUser = Integer.parseInt(sqLiteAdapter.querryIdFromUser(nameUser));
        int idTag = Integer.parseInt(sqLiteAdapter.querryIdFromTag(tagName));
        new Post(context, postText, 1, "location", idUser, idTag);
    }

    public ArrayList<String[]> querryPosts(){
        sqLiteAdapter.openToRead();
        return sqLiteAdapter.queryPosts();
    }

    public void insertTag(String nameTag){
        new Tag(context, nameTag);
    }

}
