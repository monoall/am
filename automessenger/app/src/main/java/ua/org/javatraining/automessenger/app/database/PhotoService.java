package ua.org.javatraining.automessenger.app.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ua.org.javatraining.automessenger.app.entityes.Photo;

public class PhotoService implements DbConstants {


    private SQLiteAdapter sqLiteAdapter;

    public PhotoService(SQLiteAdapter sqLiteAdapter) {
        this.sqLiteAdapter = sqLiteAdapter;
    }

    private long id;
    private String photoLink;
    private int idPost;

   public Photo insertPhoto(Photo photo){
       SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getWritableDatabase();
       ContentValues cv = new ContentValues();
       cv.put(LINK, photo.getPhotoLink());
       cv.put(ID_POST, photo.getIdPost());
       long id = sqLiteDatabase.insert(PHOTO_TABLE, null, cv);
       photo.setId(id);
       return photo;
   }

   public Photo getPhoto(int idPost){
       SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
       Cursor cursor = sqLiteDatabase.rawQuery(QUERY_PHOTO_BY_ID_POST, new String[]{String.valueOf(idPost)});
       int indexId = cursor.getColumnIndex(ID);
       int indexPhotoLink = cursor.getColumnIndex(LINK);
       int indexIdPost = cursor.getColumnIndex(ID_POST);
       Photo photo = new Photo();
       photo.setId(cursor.getInt(indexId));
       photo.setPhotoLink(cursor.getString(indexPhotoLink));
       photo.setIdPost(cursor.getInt(indexIdPost));
       return photo;
   }
}
