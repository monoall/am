package ua.org.javatraining.automessenger.app.services;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.util.Log;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.SecurityUtils;
import com.google.api.services.drive.Drive;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.util.Arrays;

/**
 * Created by berkut on 25.07.15.
 */
public class GoogleDriveAuth {

    String serviceAccountId = "220329645556-blmru1dajb76fqkic4ei45p58pq5d8s8@developer.gserviceaccount.com";
    String clientId = "220329645556-blmru1dajb76fqkic4ei45p58pq5d8s8.apps.googleusercontent.com";

    /**
     * initializes drive according to context
     * @param context activity context
     * @return initialized drive
     */
    public Drive init(Context context) {

        HttpTransport httpTransport = new NetHttpTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        AssetManager am = context.getAssets();
        InputStream is = null;
        try {
            is = am.open("file.p12");
        } catch (IOException e) {
            e.printStackTrace();
        }

        PrivateKey privateKey = null;
        try {
            privateKey = SecurityUtils.loadPrivateKeyFromKeyStore(SecurityUtils.getPkcs12KeyStore(), is, "notasecret", "privatekey", "notasecret");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }


        GoogleCredential googleCredential = new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(jsonFactory)
                .setServiceAccountId(serviceAccountId)
                .setServiceAccountPrivateKey(privateKey)
                .setServiceAccountScopes(Arrays.asList(
                        "https://spreadsheets.google.com/feeds/",
                        "https://docs.google.com/feeds/default/private/full/"))
                .build();

        Drive mService = new Drive.Builder(
                httpTransport, jsonFactory, googleCredential)
                .setApplicationName("Drive API Android Quickstart")
                .build();
        return mService;
    }

    /**
     * inserts photo to google drive
     * @param filePath path of the photo
     * @param mService service to upload
     * @return url of the inserted file
     */
    public String insertPhoto(Uri filePath, final Drive mService){
        final com.google.api.services.drive.model.File body = new com.google.api.services.drive.model.File();
        body.setTitle("Title");
        body.setDescription("Photo");

        Log.i("log", "realPath " + filePath.getPath());
        java.io.File fileContent = new java.io.File(filePath.getPath());
        final FileContent mediaContent = new FileContent(null, fileContent);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    com.google.api.services.drive.model.File file = mService.files().insert(body, mediaContent).execute();
                    System.out.println("downloadURL " + file.getDownloadUrl());
                } catch (IOException e) {
                    System.out.println("Error " + e);
                }
            }
        });
        t.start();
        /*try {
            System.out.println("downloadURL " + file.getDownloadUrl());
        } catch (NullPointerException e) {
        }*/

       /* Permission newPermission = new Permission();
        newPermission.setValue("domain");
        newPermission.setType("group");
        newPermission.setRole("reader");
        try {
            mService.permissions().insert(file.getId(), newPermission).execute();
            System.out.println("Done Shared successfully!!!!!!");
        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
        }*/

        //return file.getDownloadUrl();
        return "string";
    }


}
