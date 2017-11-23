package com.txt.conference.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.txt.conference.R;


/**就是自定义的Dialog*/
public class CustomDialog {

	public final static int SELECT_DIALOG = 1;
	public final static int RADIO_DIALOG = 2;
	public final static int CONFIRM_DIALOG = 3;

	/**
	 * 创建�?个单选对话框
	 * 
	 * @param context
	 * @param toast
	 *            提示消息
	 * @param dialogClickListener
	 *            点击监听
	 * @return
	 */
	public static android.app.Dialog showRadioDialog(Context context,
                   String toast, final DialogClickListener dialogClickListener) {
		return ShowDialog(context,
				toast,toast,
				dialogClickListener, RADIO_DIALOG);
	}

	/**
	 *
	 * 创建一个选择对话框
	 * @param context
	 * @param toast
	 *            提示消息
	 * @param dialogClickListener
	 *            点击监听
	 * @return
	 */
	public static android.app.Dialog showSelectDialog(Context context,
                                                      String toast, final DialogClickListener dialogClickListener) {
		return ShowDialog(context,
                toast, toast,
				dialogClickListener, SELECT_DIALOG);
	}

	/**
	 * 创建�?个�?�择对话�?
	 * 
	 * @param context
	 * @param title
	 *            提示标题
	 * @param toast
	 *            提示消息
	 * @param dialogClickListener
	 *            点击监听
	 * @return
	 */
	public static android.app.Dialog showSelectDialog(Context context,
                                                      String title, String toast,
                                                      final DialogClickListener dialogClickListener) {
		return ShowDialog(context, title, toast, dialogClickListener,
				SELECT_DIALOG);
	}

	public static android.app.Dialog showConfirmDialog(Context context,String title,
													  String toast,
													  final DialogClickListener dialogClickListener) {
		return ShowDialog(context, title, toast, dialogClickListener,
				CONFIRM_DIALOG);
	}

	private static android.app.Dialog ShowDialog(Context context, String title,
                                                 String toast, final DialogClickListener dialogClickListener,
                                                 int DialogType) {
		final android.app.Dialog dialog = new android.app.Dialog(context,
				R.style.DialogStyle);
		dialog.setCancelable(false);
		View view = LayoutInflater.from(context).inflate(R.layout.dialog, null);
		dialog.setContentView(view);
		((TextView) view.findViewById(R.id.point)).setText(title);
//		((TextView) view.findViewById(R.id.toast)).setText(toast);
		if (DialogType == RADIO_DIALOG) {
		} else if (DialogType == SELECT_DIALOG) {
			view.findViewById(R.id.ok).setVisibility(View.GONE);
			view.findViewById(R.id.divider).setVisibility(View.VISIBLE);
		} else if (DialogType == CONFIRM_DIALOG) {
			((TextView) view.findViewById(R.id.point_message)).setText(toast);
			view.findViewById(R.id.ok).setVisibility(View.GONE);
			view.findViewById(R.id.divider).setVisibility(View.GONE);
			view.findViewById(R.id.cancel).setVisibility(View.GONE);
		}
		view.findViewById(R.id.confirm).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						dialog.dismiss();
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								dialogClickListener.confirm();
							}
						}, 200);
					}

				});
		view.findViewById(R.id.cancel).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						dialog.dismiss();
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								dialogClickListener.cancel();
							}
						}, 200);
					}
				});
		view.findViewById(R.id.ok).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						dialogClickListener.confirm();
					}
				}, 200);
			}
		});
		Window mWindow = dialog.getWindow();
		WindowManager.LayoutParams lp = mWindow.getAttributes();
		if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {// 横屏
			lp.width = getScreenHeight(context) / 10 * 8;
		} else {
			lp.width = getScreenWidth(context) / 10 * 8;
		}
		mWindow.setAttributes(lp);
		dialog.show();

		return dialog;
	}


//	private static android.app.Dialog ShowDialog(Context context, String title,
//                                                 final String[] items, final DialogItemClickListener dialogClickListener) {
//		final android.app.Dialog dialog = new android.app.Dialog(context,
//				R.style.DialogStyle);
//		dialog.setCancelable(true);
//		dialog.setCanceledOnTouchOutside(true);
//		View view = LayoutInflater.from(context).inflate(R.layout.dialog_radio,
//				null);
//		dialog.setContentView(view);
//		((TextView) view.findViewById(R.id.title)).setText(title);
//		// 根据items动态创建对话框
//		LinearLayout parent = (LinearLayout) view
//				.findViewById(R.id.dialogLayout);
//		parent.removeAllViews();
//		int length = items.length;
//		for (int i = 0; i < items.length; i++) {
//			LayoutParams params1 = new LayoutParams(-1, -2);
//			params1.rightMargin = 1;
//			final TextView tv = new TextView(context);
//			tv.setLayoutParams(params1);
//			tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
//			tv.setId(i);
//			tv.setText(items[i]);
//			tv.setTextColor(context.getResources().getColor(
//					R.color.color_tab_text_selector));
//			int pad = DensityUtils.dp2px(10);
//			tv.setPadding(pad, pad, pad, pad);
//			tv.setSingleLine(true);
//			tv.setGravity(Gravity.CENTER);
//			if (i != length - 1)
//				tv.setBackgroundResource(R.drawable.menudialog_center_selector);
//			else
//				tv.setBackgroundResource(R.drawable.menudialog_bottom2_selector);
//
//			tv.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View view) {
//					dialog.dismiss();
//					int i=view.getId();
//					dialogClickListener.confirm(i);
//				}
//			});
//			parent.addView(tv);
//			if (i != length - 1) {
//				TextView divider = new TextView(context);
//				LayoutParams params = new LayoutParams(-1, (int) 1);
//				divider.setLayoutParams(params);
//				divider.setBackgroundResource(android.R.color.darker_gray);
//				parent.addView(divider);
//			}
//		}
//		view.findViewById(R.id.ok).setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				dialog.dismiss();
//			}
//		});
//		Window mWindow = dialog.getWindow();
//		WindowManager.LayoutParams lp = mWindow.getAttributes();
//		lp.width = getScreenWidth(context);
//		mWindow.setGravity(Gravity.BOTTOM);
//		// 添加动画
//		mWindow.setWindowAnimations(R.style.dialogAnim);
//		mWindow.setAttributes(lp);
//		dialog.show();
//		return dialog;
//	}

	/** 获取屏幕分辨率宽 */
	public static int getScreenWidth(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		return dm.widthPixels;
	}

	/** 获取屏幕分辨率高 */
	public static int getScreenHeight(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		return dm.heightPixels;
	}

	public interface DialogClickListener {
		  void confirm();
		  void cancel();
	}

	public interface DialogItemClickListener {
		void confirm(int position);
	}
}