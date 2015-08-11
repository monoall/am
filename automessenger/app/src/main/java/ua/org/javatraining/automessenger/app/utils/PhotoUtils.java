package ua.org.javatraining.automessenger.app.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PhotoUtils {

    /**
     * Take filepath of original image and create new resized image in cache dir
     *
     * @param context  Context
     * @param filepath path to original image
     * @return path to new scaled image
     */
    public static String getResizedPhotoFile(Context context, String filepath) {
        Log.i("myTag", "PhotoUtils, getResizedPhotoFile");

        if (context != null && !filepath.equals("")) {
            Bitmap originalImage = BitmapFactory.decodeFile(filepath);

            //Calculating new size for resized image
            float oIW = originalImage.getWidth();
            float oIH = originalImage.getHeight();
            float ratio = oIW / oIH;
            int iW = 1560;
            int iH = (int) (iW / ratio);
            Log.i("myTag", "PhotoUtils, getResizedPhotoFile, original size: " +
                    oIW + " x " +
                    oIH);

            Log.i("myTag", "PhotoUtils, getResizedPhotoFile, calculated size: " +
                    "ratio = " + ratio + ", " +
                    "width = " + iW + ", " +
                    "height = " + iH);

            //if old image has smaller or similar size, skip resizing and return original image filepath
            if(oIW > iW) {
                Bitmap newImage = Bitmap.createScaledBitmap(originalImage, iW, iH, true);
                File dir = context.getCacheDir();
                String newFileName = Uri.parse(filepath).getLastPathSegment();
                Log.i("myTag", "PhotoUtils, getResizedPhotoFile, new file name: " + newFileName);
                File file = new File(dir, newFileName);
                FileOutputStream fos;

                try {
                    fos = new FileOutputStream(file);
                    newImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                    originalImage.recycle();
                    newImage.recycle();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.i("myTag", "PhotoUtils, getResizedPhotoFile, new file path: " + file.getAbsolutePath());

                return file.getAbsolutePath();
            }else {
                return filepath;
            }
        }
        Log.i("myTag", "PhotoUtils, getResizedPhotoFile, INCORRECT INPUT DATA!!!");

        throw new IllegalArgumentException("Illegal Arguments!");
    }
}
