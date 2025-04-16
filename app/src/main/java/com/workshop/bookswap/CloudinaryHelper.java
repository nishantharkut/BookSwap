package com.workshop.bookswap;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Map;

public class CloudinaryHelper {
    private static final String CLOUDINARY_URL = "cloudinary://113173128145936:NkoQOfNzWyt9SVz9ERc2bx_S0og@dbdo6g0hl";

    public interface UploadCallback {
        void onUploadComplete(String imageUrl);
    }

    public static void uploadImage(Uri imageUri, Context context, UploadCallback callback) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    // Convert Uri to File
                    File file = convertUriToFile(imageUri, context);
                    if (file == null) return null;

                    // Initialize Cloudinary
                    Cloudinary cloudinary = new Cloudinary(CLOUDINARY_URL);
                    cloudinary.config.secure = true;

                    // Upload the image file
                    Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());

                    // Delete temporary file after upload
                    file.delete();

                    return (String) uploadResult.get("secure_url");
                } catch (Exception e) {
                    Log.e("CloudinaryUpload", "Error uploading image", e);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                if (result != null) {
                    callback.onUploadComplete(result);
                } else {
                    Toast.makeText(context, "Image upload failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    private static File convertUriToFile(Uri uri, Context context) {
        try {
            // Get file name from Uri
            String fileName = getFileNameFromUri(uri, context);
            if (fileName == null) fileName = "temp_image.jpg";

            File tempFile = new File(context.getCacheDir(), fileName);
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(tempFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();

            return tempFile;
        } catch (Exception e) {
            Log.e("CloudinaryUpload", "Error converting Uri to File", e);
            return null;
        }
    }

    private static String getFileNameFromUri(Uri uri, Context context) {
        String result = null;
        try {
            String[] projection = {OpenableColumns.DISPLAY_NAME};
            android.database.Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
                }
                cursor.close();
            }
        } catch (Exception e) {
            Log.e("CloudinaryUpload", "Error getting file name from Uri", e);
        }
        return result;
    }
}


