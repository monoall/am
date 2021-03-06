package ua.org.javatraining.automessenger.app.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import ua.org.javatraining.automessenger.app.entities.Photo;

public class PhotoService implements DbConstants {


    private SQLiteAdapter sqLiteAdapter;

    public PhotoService(SQLiteAdapter sqLiteAdapter) {
        this.sqLiteAdapter = sqLiteAdapter;
    }

    /**
     * Вставляет в таблицу Photo
     * @param photo объект Photo
     * @return вставленный объект
     */
    public Photo insertPhoto(Photo photo){
       SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getWritableDatabase();
       long id;
       sqLiteDatabase.beginTransaction();
       try{
           ContentValues cv = new ContentValues();
           cv.put(LINK, photo.getPhotoLink());
           cv.put(ID_POST, photo.getPostId());
           id = sqLiteDatabase.insert(PHOTO_TABLE, null, cv);
           sqLiteDatabase.setTransactionSuccessful();
       }finally {
           sqLiteDatabase.endTransaction();
       }
       photo.setId(id);
       return photo;
    }

    /**
     * Возвращает объект Photo для поста
     * @param idPost id поста
     * @return объект Post
     */
     public Photo getPhoto(int idPost){
       SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getWritableDatabase();
       Cursor cursor = sqLiteDatabase.rawQuery(QUERY_PHOTO_BY_ID_POST, new String[]{String.valueOf(idPost)});
       Photo photo = null;
       if(cursor.moveToFirst()){
           photo = buildPhoto(cursor);
       }else {
           Log.i("mytag", "getPhoto, photo = null ");
       }

       return photo;
     }

     private Photo buildPhoto(Cursor c){
       Photo p = new Photo();
       p.setId(c.getInt(c.getColumnIndex(ID)));
       p.setPhotoLink(c.getString(c.getColumnIndex(LINK)));
       p.setPostId(c.getInt(c.getColumnIndex(ID_POST)));
       return p;
     }
}
