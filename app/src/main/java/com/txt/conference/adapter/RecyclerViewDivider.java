package com.txt.conference.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.txt.conference.R;

public class RecyclerViewDivider extends RecyclerView.ItemDecoration {

    private Drawable mDrawable;
    private Context context;
    private int marging_left = 0;
    private int marging_right = 0;
    public RecyclerViewDivider(Context context) {
        this.context=context;
        mDrawable = context.getResources().getDrawable(R.drawable.divider_recycler);
    }

    /**
     *
     * @param context
     * @param dividerRes R.drawable.divider_recycler
     */
    public RecyclerViewDivider(Context context, int dividerRes) {
        this.context=context;
        mDrawable = context.getResources().getDrawable(dividerRes);
    }

    /**
     *
     * @param context
     * @param marging_left
     * @param marging_right
     */
    public RecyclerViewDivider(Context context, int marging_left, int marging_right) {
        this.context=context;
        mDrawable = context.getResources().getDrawable(R.drawable.divider_recycler);
        this.marging_left = marging_left;
        this.marging_right = marging_right;
    }

    /**
     *
     * @param context
     * @param dividerRes R.drawable.divider_recycler
     * @param marging_left
     * @param marging_right
     */
    public RecyclerViewDivider(Context context,int dividerRes, int marging_left, int marging_right) {
        this.context=context;
        mDrawable = context.getResources().getDrawable(dividerRes);
        this.marging_left = marging_left;
        this.marging_right = marging_right;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            // 以下计算主要用来确定绘制的位置
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDrawable.getIntrinsicHeight();
            //这里的四个值分别代表 left,top,right,bottom,可以指定分割线在布局中的位置
            mDrawable.setBounds(left + marging_left, top, right - marging_right, bottom);
            mDrawable.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //一下四个参数表示控件内边距（padding），分别是：left,top,right,bottom
        outRect.set(0, 0, 0, mDrawable.getIntrinsicHeight());
    }}