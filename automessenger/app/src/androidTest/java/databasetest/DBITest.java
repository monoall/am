package databasetest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLog;
import ua.org.javatraining.automessenger.app.database.SQLiteAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import static junit.framework.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.internal.util.reflection.Whitebox.setInternalState;

//@Ignore
@RunWith(RobolectricTestRunner.class)
public abstract class DBITest {
    private static final File TMP_DB_FILE = new File(String.format("%s%stest.db", System.getProperty("java.io.tmpdir"), File.separator));
    private static final File TMP_DB_JOURNAL_FILE = new File(String.format("%s%stest.db-journal", System.getProperty("java.io.tmpdir"), File.separator));

    protected Context context;
    protected SQLiteDatabase db;
    protected SQLiteAdapter sqLiteAdapter;

    @Before
    public void setUp() throws Exception {
        ShadowLog.stream = System.out;
        context = Robolectric.application;
        db = SQLiteDatabase.openDatabase(TMP_DB_FILE.getAbsolutePath(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
        db.execSQL("PRAGMA foreign_keys = ON;");

        SQLiteAdapter mocked = mock(SQLiteAdapter.class);
        when(mocked.getReadableDatabase()).thenReturn(db);
        when(mocked.getWritableDatabase()).thenReturn(db);

        sqLiteAdapter = SQLiteAdapter.initInstance(context);
        setInternalState(sqLiteAdapter, "sQLiteAdapter", mocked);
        sqLiteAdapter.onCreate(db);

        loadDataSet();
    }

    abstract String getDataSetFileName();

    private void loadDataSet() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(getDataSetFileName()), "UTF-8"));
            while (reader.ready()) {
                String line = reader.readLine();
                if (line.isEmpty()) {
                    continue;
                }
                db.execSQL(line);
            }
        } catch (IOException e) {
            fail("IOException");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    fail("IOException");
                }
            }
        }
    }

    @After
    public void tearDown() throws Exception {
        setInternalState(sqLiteAdapter, "sQLiteAdapter", sqLiteAdapter);
        db.close();
        TMP_DB_FILE.delete();
        TMP_DB_JOURNAL_FILE.delete();
    }
}
