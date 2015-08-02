package ua.org.javatraining.automessenger.app.services;

import android.os.AsyncTask;
import android.util.Log;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.Permission;

import java.io.IOException;

/**
 * Created by berkut on 27.07.15.
 */
public class InsertTask extends AsyncTask<Void, Void, String> {

    com.google.api.services.drive.Drive mService;
    private String filePath;

    public InsertTask(com.google.api.services.drive.Drive mService, String path) {
        this.mService = mService;
        this.filePath = path;
    }

    @Override
    protected String doInBackground(Void... voids) {
        return insertPhoto(filePath, mService);
    }

    /**
     * inserts photo to google drive
     *
     * @param filePath path of the photo
     * @param mService service to upload
     * @return url of the inserted file
     */
    private String insertPhoto(String filePath, Drive mService) {
        com.google.api.services.drive.model.File body = new com.google.api.services.drive.model.File();
        body.setTitle("Title");
        body.setDescription("Photo");

        Log.i("myTag", "insertTask, filePath: " + filePath);

        java.io.File fileContent = new java.io.File(filePath);
        FileContent mediaContent = new FileContent(null, fileContent);
        com.google.api.services.drive.model.File file = null;

        try {
            file = mService.files().insert(body, mediaContent).execute();

            Log.i("myTag","insertTask, downloadURL: " + file.getWebContentLink());

            Permission newPermission = new Permission();
            newPermission.setType("anyone");
            newPermission.setRole("reader");
            mService.permissions().insert(file.getId(), newPermission).execute();
            String[] res = file.getWebContentLink().split("&");
            return res[0];
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
