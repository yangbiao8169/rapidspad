package com.example.ylstest01;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;

import java.io.OutputStream;

public class SaveImageHelper {
    public  void saveImageToGallery(Context context, Bitmap bitmap, String title, String description) {
        // 首先确保有存储写入权限

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, title);
        values.put(MediaStore.Images.Media.DESCRIPTION, description);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        // 插入图片信息，获取图片URI
        Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        try {
            if (uri != null) {
                // 获取输出流
                OutputStream outStream = context.getContentResolver().openOutputStream(uri);
                // 用JPEG格式保存Bitmap
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                outStream.close();

                // 提醒媒体扫描器扫描文件
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public  String getFileNameFromUri(Context context, Uri uri) {
        String fileName = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    // 获取文件名列的索引
                    int index = cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME);
                    // 获取文件名
                    fileName = cursor.getString(index);
                }
            }
        }
        return fileName;
    }


}
