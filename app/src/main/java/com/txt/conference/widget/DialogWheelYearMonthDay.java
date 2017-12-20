package com.txt.conference.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tofu.conference.utils.DensityUtils;
import com.txt.conference.R;
import com.txt.conference.widget.dialogWheel.DateArrayAdapter;
import com.txt.conference.widget.dialogWheel.NumericWheelAdapter;
import com.txt.conference.widget.dialogWheel.OnWheelChangedListener;
import com.txt.conference.widget.dialogWheel.WheelView;

import java.util.Calendar;

public class DialogWheelYearMonthDay extends Dialog {

    private  Context mContext;
    private WheelView mYearView;
    private WheelView mMonthView;
    private WheelView mDayView;
    private WheelView mHourView;
    private WheelView mMinuteView;
    private int curYear;
    private int curMonth;
    private int curDay;
    private int curHour;
    private int curMinute;
    private TextView mTvSure;
    private TextView mTvCancle;
    private CheckBox mCheckBoxDay;
    private Calendar mCalendar;
    private String mYears[] = new String[]{"2010", "2011", "2012",
            "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020", "2021"};

    private String mMonths[] = new String[]{"01", "02", "03",
            "04", "05", "06", "07", "08", "09", "10", "11", "12"};


    private String mHours[] = new String[]{"0","1", "2", "3",
            "4", "5", "6", "7", "8", "9", "10", "11", "12", "13",
            "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};

    private String mMinutes[] = new String[]{"00","01", "02", "03", "04", "05", "06", "07",
            "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
            "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31",
            "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42",
            "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54",
            "55", "56", "57", "58", "59"};


    private String mDays[] = new String[]{"01", "02", "03", "04", "05", "06", "07",
            "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
            "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};

    private int beginYear = 0;
    private int endYear = 0;
    private int divideYear = endYear - beginYear;

    public DialogWheelYearMonthDay(Context mContext) {
        super(mContext);
        // TODO Auto-generated constructor stub
        this.mContext = mContext;
        build();
    }

    public DialogWheelYearMonthDay(Context mContext, int beginYear) {
        super(mContext);
        // TODO Auto-generated constructor stub
        this.mContext = mContext;
        this.beginYear = beginYear;
        build();
    }

    public DialogWheelYearMonthDay(Context mContext, int beginYear, int endYear) {
        super(mContext);
        // TODO Auto-generated constructor stub
        this.mContext = mContext;
        this.beginYear = beginYear;
        this.endYear = endYear;
        build();
    }

    public DialogWheelYearMonthDay(Context mContext, TextView tv_time) {
        super(mContext);
        // TODO Auto-generated constructor stub
        this.mContext = mContext;
        build();
        tv_time.setText(curYear + "年" + mMonths[curMonth] + "月");
    }

    public int getBeginYear() {
        return beginYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public int getDivideYear() {
        return divideYear;
    }

    private void build() {
        mCalendar = Calendar.getInstance();
        final View dialogView1 = LayoutInflater.from(mContext).inflate(R.layout.dialog_year_month_day, null);

        OnWheelChangedListener listener = new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updateDays(mYearView, mMonthView, mDayView);
            }
        };

        curYear = mCalendar.get(Calendar.YEAR);
        if (beginYear == 0) {
            beginYear = curYear - 5;
        }
        if (endYear == 0) {
            endYear = curYear;
        }
        if (beginYear > endYear) {
            endYear = beginYear;
        }

        //mYearView
        mYearView = (WheelView) dialogView1.findViewById(R.id.wheelView_year);
        mYearView.setBackgroundResource(R.drawable.transparent_bg);
        mYearView.setWheelBackground(R.drawable.transparent_bg);
        mYearView.setWheelForeground(R.drawable.wheel_val_holo);
        mYearView.setShadowColor(0xFFDADCDB, 0x88DADCDB, 0x00DADCDB);
        mYearView.setViewAdapter(new DateArrayAdapter(mContext, mYears,curYear));
        for (int i = 0; i < mYears.length ; i++) {
            if(mYears[i].equals(curYear+"")){
                mYearView.setCurrentItem(i);
            }
        }
        mYearView.addChangingListener(listener);

        // mMonthView
        mMonthView = (WheelView) dialogView1
                .findViewById(R.id.wheelView_month);
        mMonthView.setBackgroundResource(R.drawable.transparent_bg);
        mMonthView.setWheelBackground(R.drawable.transparent_bg);
        mMonthView.setWheelForeground(R.drawable.wheel_val_holo);
        mMonthView.setShadowColor(0xFFDADCDB, 0x88DADCDB, 0x00DADCDB);
        curMonth = mCalendar.get(Calendar.MONTH);
        mMonthView.setViewAdapter(new DateArrayAdapter(mContext, mMonths, curMonth));
        mMonthView.setCurrentItem(curMonth);
        mMonthView.addChangingListener(listener);


        //mDayView
        mDayView = (WheelView) dialogView1.findViewById(R.id.wheelView_day);
        updateDays(mYearView, mMonthView, mDayView);
        curDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        mDayView.setCurrentItem(curDay - 1);
        mDayView.setBackgroundResource(R.drawable.transparent_bg);
        mDayView.setWheelBackground(R.drawable.transparent_bg);
        mDayView.setWheelForeground(R.drawable.wheel_val_holo);
        mDayView.setShadowColor(0xFFDADCDB, 0x88DADCDB, 0x00DADCDB);


        // mMonthView
        mHourView = (WheelView) dialogView1
                .findViewById(R.id.wheelView_hour);
        mHourView.setBackgroundResource(R.drawable.transparent_bg);
        mHourView.setWheelBackground(R.drawable.transparent_bg);
        mHourView.setWheelForeground(R.drawable.wheel_val_holo);
        mHourView.setShadowColor(0xFFDADCDB, 0x88DADCDB, 0x00DADCDB);
        curHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        mHourView.setViewAdapter(new DateArrayAdapter(mContext, mHours, curHour));
        mHourView.setCurrentItem(curHour-1);


        // mMonthView
        mMinuteView = (WheelView) dialogView1
                .findViewById(R.id.wheelView_minute);
        mMinuteView.setBackgroundResource(R.drawable.transparent_bg);
        mMinuteView.setWheelBackground(R.drawable.transparent_bg);
        mMinuteView.setWheelForeground(R.drawable.wheel_val_holo);
        mMinuteView.setShadowColor(0xFFDADCDB, 0x88DADCDB, 0x00DADCDB);
        curMinute = mCalendar.get(Calendar.MINUTE);
        mMinuteView.setViewAdapter(new DateArrayAdapter(mContext, mMinutes, curMinute));
        mMinuteView.setCurrentItem(curMinute-1);

        mTvSure = (TextView) dialogView1.findViewById(R.id.tv_sure);
        mTvCancle = (TextView) dialogView1.findViewById(R.id.tv_cancel);
        mTvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        setContentView(dialogView1);
        getLayoutParams();
    }


    public WindowManager.LayoutParams getLayoutParams(){
        WindowManager.LayoutParams layoutParams=getWindow().getAttributes();
        layoutParams.width= (int) (DensityUtils.INSTANCE.getWidth(mContext));
        layoutParams.height=  WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.alpha=1f;
        getWindow().setAttributes(layoutParams);
        return layoutParams;
    }


    public WheelView getDayView() {
        return mDayView;
    }
    public WheelView getHourView() {
        return mHourView;
    }
    public WheelView getMinuteView() {
        return mMinuteView;
    }

    public int getCurDay() {
        return curDay;
    }

    public CheckBox getCheckBoxDay() {
        return mCheckBoxDay;
    }

    public WheelView getYearView() {
        return mYearView;
    }

    public WheelView getMonthView() {
        return mMonthView;
    }

    private int getCurYear() {
        return curYear;
    }

    private int getCurMonth() {
        return curMonth;
    }

    public TextView getSureView() {
        return mTvSure;
    }

    public TextView getCancleView() {
        return mTvCancle;
    }

    private String[] getMonths() {
        return mMonths;
    }
    private String[] getYears() {
        return mYears;
    }

    private String[] getDays() {
        return mDays;
    }

    private String[] getMinutes(){
        return mMinutes;
    }

    public String getSelectorYear() {
        return  getYears()[getYearView().getCurrentItem()];
    }

    public String getSelectorMonth() {
        return getMonths()[getMonthView().getCurrentItem()];
    }

    public String getSelectorDay() {
        return getDays()[getDayView().getCurrentItem()];
    }
    public String getSelectorHour() {
        return getDays()[getHourView().getCurrentItem()];
    }

    public String getSelectorMinute() {
        return getMinutes()[getMinuteView().getCurrentItem()];
    }

    /**
     * Updates mDayView wheel. Sets max mDays according to selected mMonthView and mYearView
     */
    private void updateDays(WheelView year, WheelView month, WheelView day) {
        mCalendar.set(Calendar.YEAR, beginYear + year.getCurrentItem());
        mCalendar.set(Calendar.MONTH, month.getCurrentItem());
        int maxDay = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int maxDays = getDaysByYearMonth(beginYear + year.getCurrentItem(), month.getCurrentItem() + 1);
        day.setViewAdapter(new DateNumericAdapter(mContext, 1, maxDays, mCalendar.get(Calendar.DAY_OF_MONTH) - 1));
        int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
        day.setCurrentItem(curDay - 1, true);
    }

    /**
     * Adapter for numeric wheels. Highlights the current value.
     */
    private class DateNumericAdapter extends NumericWheelAdapter {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;

        /**
         * Constructor
         */
        public DateNumericAdapter(Context mContext, int minValue, int maxValue, int current) {
            super(mContext, minValue, maxValue);
            this.currentValue = current;
            setTextSize(16);
        }

        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            /*if (currentItem == currentValue) {
                view.setTextColor(0xFF0000F0);
			}*/
            view.setTypeface(Typeface.SANS_SERIF);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            return super.getItem(index, cachedView, parent);
        }
    }

    /**
     * 根据年 月 获取对应的月份 天数
     */
    public int getDaysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }
}
