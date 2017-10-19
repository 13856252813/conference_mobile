package com.txt.conference.utils;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.txt.conference.R;

public class CostTimePickDialogUtil  implements NumberPicker.OnValueChangeListener {

    private NumberPicker costTimePicker;
    private AlertDialog ad;
    private Activity activity;
    private String costtime;

    public CostTimePickDialogUtil(Activity activity ) {
        this.activity = activity;

    }

    public void init() {

    }




    public AlertDialog costTimePicKDialog() {
        LinearLayout costTimeLayout = (LinearLayout) activity
                .getLayoutInflater().inflate(R.layout.common_costtime, null);
        costTimePicker = (NumberPicker) costTimeLayout.findViewById(R.id.numberPicker1);
        init();

        costTimePicker.setMaxValue(120);
        costTimePicker.setMinValue(10);

        ad = new AlertDialog.Builder(activity)
                .setTitle("预计会议时长")
                .setView(costTimeLayout)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //inputDate.setText(dateTime);
                        dialogListener.onCostTimeConfirm(costtime);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                       // inputDate.setText("");
                    }
                }).show();

        //onDateChanged(null, 0, 0, 0);
        costTimePicker.setOnValueChangedListener(this);
        return ad;
    }


    /**
     * 实现将初始日期时间2012年07月02日 16:45 拆分成年 月 日 时 分 秒,并赋值给calendar
     *
     * @param initDateTime
     *            初始日期时间值 字符串型
     * @return Calendar
     */
    private Calendar getCalendarByInintData(String initDateTime) {
        Calendar calendar = Calendar.getInstance();

        // 将初始日期时间2012年07月02日 16:45 拆分成年 月 日 时 分 秒
        String date = spliteString(initDateTime, "日", "index", "front"); // 日期
        String time = spliteString(initDateTime, "日", "index", "back"); // 时间

        String yearStr = spliteString(date, "年", "index", "front"); // 年份
        String monthAndDay = spliteString(date, "年", "index", "back"); // 月日

        String monthStr = spliteString(monthAndDay, "月", "index", "front"); // 月
        String dayStr = spliteString(monthAndDay, "月", "index", "back"); // 日

        String hourStr = spliteString(time, ":", "index", "front"); // 时
        String minuteStr = spliteString(time, ":", "index", "back"); // 分

        int currentYear = Integer.valueOf(yearStr.trim()).intValue();
        int currentMonth = Integer.valueOf(monthStr.trim()).intValue() - 1;
        int currentDay = Integer.valueOf(dayStr.trim()).intValue();
        int currentHour = Integer.valueOf(hourStr.trim()).intValue();
        int currentMinute = Integer.valueOf(minuteStr.trim()).intValue();

        calendar.set(currentYear, currentMonth, currentDay, currentHour,
                currentMinute);
        return calendar;
    }

    /**
     * 截取子串
     *
     * @param srcStr
     *            源串
     * @param pattern
     *            匹配模式
     * @param indexOrLast
     * @param frontOrBack
     * @return
     */
    public static String spliteString(String srcStr, String pattern,
                                      String indexOrLast, String frontOrBack) {
        String result = "";
        int loc = -1;
        if (indexOrLast.equalsIgnoreCase("index")) {
            loc = srcStr.indexOf(pattern); // 取得字符串第一次出现的位置
        } else {
            loc = srcStr.lastIndexOf(pattern); // 最后一个匹配串的位置
        }
        if (frontOrBack.equalsIgnoreCase("front")) {
            if (loc != -1)
                result = srcStr.substring(0, loc); // 截取子串
        } else {
            if (loc != -1)
                result = srcStr.substring(loc + 1, srcStr.length()); // 截取子串
        }
        return result;
    }

    public ICostTimePickDialogClick dialogListener;
    public void setCostTimePickeristener(ICostTimePickDialogClick dialogListener) {
        this.dialogListener = dialogListener;
    }

    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
        costtime = i1 + "";
        Log.i("test:", i1 +"");
    }

    public interface ICostTimePickDialogClick {
        public void onCostTimeConfirm(String str);
        public void onCostTimeCancel();
    }
}