package com.reconova.faceid.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.media.FaceDetector;
import android.util.Log;

import java.io.File;


public class CheckFaceUtil {

    private static CheckFaceUtil checkFaceUtil;
    private static Float FACE_THR = 0.3f;

    private CheckFaceUtil() {
  
    }  

    public static CheckFaceUtil getInstance() {  
        if (null == checkFaceUtil) {  
            checkFaceUtil = new CheckFaceUtil();  
        }  
        return checkFaceUtil;  
    }  
  
    private static final String TAG = "checkFaceUtil";

    public boolean isFaceBitMap(Bitmap bitmap)
    {

        //设定最大可查的人脸数量
        int MAX_FACES = 1;
        FaceDetector faceDet = new FaceDetector(bitmap.getWidth(), bitmap.getHeight(), MAX_FACES);
        //将人脸数据存储到facelist中
        FaceDetector.Face[] faceList = new FaceDetector.Face[MAX_FACES];
        faceDet.findFaces(bitmap, faceList);

        //  FaceDetector API文档我们发现，它查找人脸的原理是：找眼睛。
        //  它返回的人脸数据face，通过调用public float eyesDistance ()，public void getMidPoint (PointF point)，
        //  我们可以得到探测到的两眼间距，以及两眼中心点位置（MidPoint）。
        //  public float confidence () 可以返回该人脸数据的可信度(0~1)，这个值越大，该人脸数据的准确度也就越高。
        RectF[] faceRects=new RectF[faceList.length];
        for (int i=0; i < faceList.length; i++) {
            FaceDetector.Face face = faceList[i];
            Log.d("FaceDet", "Face ["+face+"]");
            if (face != null) {
                //confidence标识一个匹配度，在0~1区间，越接近1，表示匹配越高。如果需要可以加上这个判断条件
                //这里不做判断
                Log.d("FaceDet", "Face ["+i+"] - Confidence bitmap ["+face.confidence()+"]");
                if (face.confidence() > FACE_THR){
                    return true;
                }

            }
        }
        return false;

    }

    public boolean isFaceFile(String strBitMap)
    {

        File file = new File(strBitMap);
        if (!file.exists()){
            Log.d("FaceDet", "file not exists");
            return false;
        }

        Bitmap sampleBmp= BitmapFactory.decodeFile(strBitMap);
        Bitmap bitmap = sampleBmp.copy(Bitmap.Config.RGB_565, true);

        //设定最大可查的人脸数量
        int MAX_FACES = 1;
        FaceDetector faceDet = new FaceDetector(bitmap.getWidth(), bitmap.getHeight(), MAX_FACES);
        //将人脸数据存储到facelist中
        FaceDetector.Face[] faceList = new FaceDetector.Face[MAX_FACES];
        faceDet.findFaces(bitmap, faceList);

        //  FaceDetector API文档我们发现，它查找人脸的原理是：找眼睛。
        //  它返回的人脸数据face，通过调用public float eyesDistance ()，public void getMidPoint (PointF point)，
        //  我们可以得到探测到的两眼间距，以及两眼中心点位置（MidPoint）。
        //  public float confidence () 可以返回该人脸数据的可信度(0~1)，这个值越大，该人脸数据的准确度也就越高。
        //RectF[] faceRects=new RectF[faceList.length];
        for (int i=0; i < faceList.length; i++) {
            FaceDetector.Face face = faceList[i];
            Log.d("FaceDet", "Face ["+face+"]");
            if (face != null) {
                //confidence标识一个匹配度，在0~1区间，越接近1，表示匹配越高。如果需要可以加上这个判断条件
                //这里不做判断
                Log.d("FaceDet", "Face ["+i+"] - Confidence ["+face.confidence()+"]");
                if (face.confidence() > FACE_THR){
                    return true;
                }

            }
        }
        return false;

    }

}  