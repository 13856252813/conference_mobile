package com.txt.conference.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;


/**
 * Created by pc on 2017/12/14.
 */

public class ToastUtils {

    private static Toast mToast;


    public static void init(Context context) {
        if (mToast == null) {
            mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        }
    }

    public static void shortShow(int res) {
        mToast.setText(res);
        mToast.show();
    }

    public static void shortShow(String res) {
        mToast.setText(res);
        mToast.show();
    }

    public static void topShow(String res) {
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.setText(res);
        mToast.setGravity(Gravity.TOP,0,30);
        mToast.show();
    }

    public static void longShow(int res) {
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.setText(res);
        mToast.show();
    }


}
