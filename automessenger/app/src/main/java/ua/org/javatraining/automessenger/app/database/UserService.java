package ua.org.javatraining.automessenger.app.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ua.org.javatraining.automessenger.app.entities.User;

public class UserService implements DbConstants{
    private SQLiteAdapter sqLiteAdapter;

    public UserService(SQLiteAdapter sqLiteAdapter) {
        this.sqLiteAdapter = sqLiteAdapter;
    }

    /**
     * Вставляет в таблицу User нового юзера
     * @param user объект User
     * @return возвращает этот же объект
     */
    public User insertUser(User user) {
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getWritableDatabase();
        long id;
        sqLiteDatabase.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put(USER_NAME, user.getName());
            sqLiteDatabase.insert(USER_TABLE, null, cv);
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }
        return user;
    }

    /**
     * Возвращает юзера по его имени
     * @param userName - имя юзера
     * @return объект User
     */
    public User getUser(String userName){
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(USER_TABLE, null,
                USER_NAME + " = ?", new String[]{userName}, null, null, null);
        User user = null;
        if(cursor.moveToFirst()){
            user =  buildUser(cursor);
        }
        cursor.close();
        return user;
    }

    /**
     * Удаляет юзера
     */
    public void deleteUser(User user){
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        try{
            sqLiteDatabase.delete(USER_TABLE, USER_NAME + " = ?", new String[]{user.getName()});
            sqLiteDatabase.setTransactionSuccessful();
        }finally {
            sqLiteDatabase.endTransaction();
        }
       }

    private User buildUser(Cursor c){
        User u = new User();
        u.setName(c.getString(c.getColumnIndex(USER_NAME)));
        return u;
    }
}
