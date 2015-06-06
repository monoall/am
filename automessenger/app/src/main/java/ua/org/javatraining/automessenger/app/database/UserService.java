package ua.org.javatraining.automessenger.app.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ua.org.javatraining.automessenger.app.entityes.User;

/**
 * Created by berkut on 04.06.15.
 */
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
        ContentValues cv = new ContentValues();
        cv.put(USER_NAME, user.getName());
        long id = sqLiteDatabase.insert(USER_TABLE, null, cv);
        user.setId(id);
        return user;
    }

    public User getUserById(long id){
          /*SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        Cursor cursor = sqLiteDatabase
                .rawQuery("SELECT USER_NAME from USER where ID = ?", new String[]{String.valueOf(id)});
        int indexUser = cursor.getColumnIndex(USER_NAME);
        User user = new User();
        user.setId(id);
        user.setName(cursor.getString(indexUser));*/
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(USER_TABLE, null,
                null, null, null, null, null);
        int indexUser = cursor.getColumnIndex(USER_NAME);
        cursor.move((int) id);
        User user = new User();
        user.setId(id);
        user.setName(cursor.getString(indexUser));
        return user;
    };

    protected User querryIdFromUser(String userName) {
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        Cursor cursor = sqLiteDatabase
                .rawQuery(QUERY_ID_USER_BY_NAME, new String[]{userName});
        int indexId = cursor.getColumnIndex(ID);
        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
            User u = new User();
            u.setId(cursor.getLong(indexId));
            return u;
        }
        return new User();
    }

    public void deleteUser(User user){
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        sqLiteDatabase.delete(USER_TABLE, USER_NAME + " = " + user.getName(), null);
    }

}
