package com.reconova.faceid.utils;

import android.content.Context;
import android.widget.Toast;

public class CustomToast {
	
	public static void showShort(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
	
	public static void showLong(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}
	
	public static void showShort(Context context, int rid) {
		Toast.makeText(context, rid, Toast.LENGTH_SHORT).show();
	}
	
	public static void showLong(Context context, int rid) {
		Toast.makeText(context, rid, Toast.LENGTH_LONG).show();
	}

}
