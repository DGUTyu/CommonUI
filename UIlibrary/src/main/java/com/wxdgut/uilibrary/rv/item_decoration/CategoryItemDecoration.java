package com.wxdgut.uilibrary.rv.item_decoration;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * RecyclerView 分割线定制
 */
public class CategoryItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDivider;
    private Paint mPaint;
    private int splitHeight;

    public CategoryItemDecoration(Drawable divider) {
        // 利用Drawable绘制分割线
        mDivider = divider;
    }

    public CategoryItemDecoration(int color, int splitHeight) {
        mDivider = null;
        // 直接绘制颜色  只是用来测试
        this.splitHeight = splitHeight;
        mPaint = new Paint();
        mPaint.setColor(color);
        mPaint.setAntiAlias(true);
    }

    public CategoryItemDecoration(int color) {
        this(color, 20);
    }

    //因为有Canvas，绘制什么都可以，但一般都是绘制Drawable
    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        // 计算需要绘制的区域
        Rect rect = new Rect();
        rect.left = parent.getPaddingLeft();
        rect.right = parent.getWidth() - parent.getPaddingRight();
        for (int i = 0; i < childCount; i++) {
            View childView = parent.getChildAt(i);
            rect.top = childView.getBottom();
            if (mDivider != null) {
                rect.bottom = rect.top + mDivider.getIntrinsicHeight();
                mDivider.setBounds(rect);
                mDivider.draw(canvas); // 直接利用Canvas去绘制
            } else {
                rect.bottom = rect.top + splitHeight;
                canvas.drawRect(rect, mPaint); // 或直接利用Canvas去绘制一个矩形 在留出来的地方
            }
        }
    }

    //指定偏移量即分割线占多少高度。或者说是画在什么位置
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mDivider != null) {
            // 在每个子View的下面留出来画分割线
            outRect.bottom += mDivider.getIntrinsicHeight();
        } else {
            // 在每个子View的下面留出**px来画分割线
            outRect.bottom += splitHeight;
        }
    }
}
