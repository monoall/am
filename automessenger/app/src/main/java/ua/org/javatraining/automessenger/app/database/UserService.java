package ua.org.javatraining.automessenger.app.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ua.org.javatraining.automessenger.app.entityes.User;

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
            id = sqLiteDatabase.insert(USER_TABLE, null, cv);
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }
        user.setId(id);
        return user;
    }

    public User getUserById(long id){
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(USER_TABLE, null,
                "ID = ?", new String[]{String.valueOf(id)}, null, null, null);
        User user = null;
        if(cursor.moveToFirst()){
            user =  buildUser(cursor);
        }
        return user;
    }

    public void deleteUser(User user){
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getWritableDatabase();
        sqLiteDatabase.delete(USER_TABLE, "ID = ?", new String[]{String.valueOf(user.getId())});
    }

    private User buildUser(Cursor c){
        User u = new User();
        u.setId(c.getLong(c.getColumnIndex(ID)));
        u.setName(c.getString(c.getColumnIndex(USER_NAME)));
        return u;
    }
}
