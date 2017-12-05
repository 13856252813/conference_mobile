package com.reconova.faceid.utils;

import android.graphics.PixelFormat;
import android.util.Log;
import android.view.SurfaceHolder;

public class SFHRectCallback implements SurfaceHolder.Callback {

	private SurfaceHolder holder = null;
	public SFHRectCallback(SurfaceHolder holder) {
		this.holder = holder;
		this.holder.addCallback(this);
	}
	
	public void initHolder() {
		if (holder != null) {
			this.holder.setFormat(PixelFormat.TRANSPARENT);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		Log.i("SFHRect", "surfaceChanged");
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		Log.i("SFHRect", "surfaceCreated");

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		Log.i("SFHRect", "surfaceDestroyed");

	}
}