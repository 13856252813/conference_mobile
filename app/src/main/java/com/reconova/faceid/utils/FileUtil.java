package com.reconova.faceid.utils;

import java.io.BufferedOutputStream;  
import java.io.File;  
import java.io.FileOutputStream;  
import java.io.IOException;  
  
import android.graphics.Bitmap;  
import android.os.Environment;  
import android.util.Log;  
  
public class FileUtil {  
    private static final  String TAG = "FileUtil";  
    private static final File parentPath = Environment.getExternalStorageDirectory();  
    private static   String storagePath = "";  
    private static final String DST_FOLDER_NAME = "ConferenceCamera";
    private static final String DST_FILE_NAME = "phonecheck.jpg";
    private static final String DST_FACE_ALIVE = "face.jpg";
    public static final String picFullPathFileName = parentPath.getAbsolutePath()+"/" + DST_FOLDER_NAME + "/" + DST_FILE_NAME;
    public static final String picFullPathFaceFileName = parentPath.getAbsolutePath()+"/" + DST_FOLDER_NAME  + "/" + DST_FACE_ALIVE;

    private static String initPath(){
        if(storagePath.equals("")){  
            storagePath = parentPath.getAbsolutePath()+"/" + DST_FOLDER_NAME;  
            File f = new File(storagePath);  
            if(!f.exists()){  
                f.mkdir();  
            }  
        }  
        return storagePath;  
    }  
  
    public static void saveBitmap(Bitmap b){
  
        String path = initPath();  
        long dataTake = System.currentTimeMillis();  
        //String jpegName = path + "/" + dataTake +".jpg";
        String jpegName = picFullPathFileName;
        File checkfile = new File(jpegName);
        if (checkfile.exists()){
            checkfile.delete();
            Log.i(TAG, "delete old file jpegName = " + jpegName);
        }

        Log.i(TAG, "saveBitmap:jpegName = " + jpegName);  
        try {  
            FileOutputStream fout = new FileOutputStream(jpegName);  
            BufferedOutputStream bos = new BufferedOutputStream(fout);  
            b.compress(Bitmap.CompressFormat.JPEG, 100, bos);  
            bos.flush();  
            bos.close();  
            Log.i(TAG, "saveBitmap");
        } catch (IOException e) {
            Log.i(TAG, "saveBitmap null");
            e.printStackTrace();  
        }  
  
    }

    public static void saveBitmap(Bitmap b, int typeNo){

        String path = initPath();
        long dataTake = System.currentTimeMillis();
        //String jpegName = path + "/" + dataTake +".jpg";
        String jpegName = picFullPathFaceFileName;// + typeNo + ".jpg";
        File checkfile = new File(jpegName);
        if (checkfile.exists()){
            checkfile.delete();
            Log.i(TAG, "delete old file jpegName = " + jpegName);
        }

        Log.i(TAG, "saveBitmap:jpegName = " + jpegName);
        try {
            FileOutputStream fout = new FileOutputStream(jpegName);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
            b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            Log.i(TAG, "saveBitmap");
        } catch (IOException e) {
            Log.i(TAG, "saveBitmap null");
            e.printStackTrace();
        }

    }

}  