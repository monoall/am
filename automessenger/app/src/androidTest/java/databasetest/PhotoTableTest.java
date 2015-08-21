package databasetest;

import android.database.Cursor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import ua.org.javatraining.automessenger.app.database.DbConstants;
import ua.org.javatraining.automessenger.app.database.PhotoService;
import ua.org.javatraining.automessenger.app.database.SQLiteAdapter;
import ua.org.javatraining.automessenger.app.entities.Photo;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by berkut on 21.06.15.
 */
@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class PhotoTableTest extends DBITest implements DbConstants {

    private PhotoService photoService;

    @Override
    String getDataSetFileName() {
        return "/db.sql";
    }

    @Before
    public void init(){
        photoService = new PhotoService(SQLiteAdapter.initInstance(Robolectric.application));
    }

    @Test
    public void testInsertPhoto(){
        Photo photo = new Photo();
        photo.setPhotoLink("link");
        photo.setPostId(2);
        photoService.insertPhoto(photo);
        Cursor c = db.query(PHOTO_TABLE, null,
                ID_POST + " = ?", new String[]{String.valueOf(2)}, null, null, null);
        assertTrue(c.moveToFirst());
        assertEquals("link", c.getString(c.getColumnIndex(LINK)));
        c.close();
    }

    @Test
    public void testGetPhoto(){
        Photo photo = photoService.getPhoto(1);
        assertEquals("link1", photo.getPhotoLink());
    }
}
