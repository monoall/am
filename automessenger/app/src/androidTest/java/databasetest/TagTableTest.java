package databasetest;

import android.database.Cursor;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import ua.org.javatraining.automessenger.app.database.DbConstants;
import ua.org.javatraining.automessenger.app.database.SQLiteAdapter;
import ua.org.javatraining.automessenger.app.database.TagService;
import ua.org.javatraining.automessenger.app.entities.Tag;

import static junit.framework.Assert.*;

@Ignore
@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class TagTableTest extends DBITest implements DbConstants {

    private TagService tagService;

    @Override
    String getDataSetFileName() {
        return "/db.sql";
    }

    @Before
    public void init(){
        tagService = new TagService(SQLiteAdapter.initInstance(Robolectric.application));
    }

    @Test
    public void testInsertTag(){
        Tag tag = new Tag();
        tag.setTagName("BE 1111");
        tagService.insertTag(tag);
        Cursor c = db.query(TAG_TABLE, null,
                TAG_NAME + " = ?", new String[]{"BE 1111"}, null, null, null);
        assertTrue(c.moveToFirst());
        assertEquals("BE 1111", c.getString(c.getColumnIndex(TAG_NAME)));
        c.close();
    }

    @Test
    public void testGetTag(){
        Tag checkTag = tagService.getTag("BE 0101");
        assertNotNull(checkTag);
        assertEquals("BE 0101", checkTag.getTagName());
    }

    @Test
    public void testDeleteTag(){
        Tag checkTag = new Tag();
        checkTag.setTagName("BE 0101");
        tagService.deleteTag(checkTag);
        Cursor c = db.query(TAG_TABLE, null,
                TAG_NAME + " = ?", new String[]{"BE 0101"}, null, null, null);
        assertFalse(c.moveToFirst());
        c.close();
    }
}
