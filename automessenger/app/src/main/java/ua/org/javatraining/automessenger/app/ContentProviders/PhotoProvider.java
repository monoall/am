package ua.org.javatraining.automessenger.app.contentProviders;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PhotoProvider extends ContentProvider {

    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        Log.i("myTag", "PhotoProvider, openFile, uri = " + uri);

        File f = new File(getContext().getCacheDir(), "temp.jpg");

        Bitmap b = BitmapFactory.decodeFile(uri.getPath());

        FileOutputStream fos;

        try {
            fos = new FileOutputStream(f);
            b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (f.exists()) {
            Log.i("myTag", "PhotoProvider, openFile, filepath = " + f.getAbsolutePath());
            return ParcelFileDescriptor.open(f, ParcelFileDescriptor.MODE_READ_ONLY);
        } else {
            Log.i("myTag", "PhotoProvider, openFile, FILE NOT FOUND filepath = " + f.getAbsolutePath());

            throw new FileNotFoundException(uri.getPath());
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        return "image/jpeg";
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        return 0;
    }
}
