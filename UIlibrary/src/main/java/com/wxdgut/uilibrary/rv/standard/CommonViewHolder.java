package com.wxdgut.uilibrary.rv.standard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

/**
 * FileName: CommonViewHolder
 * Founder: lu yao
 * User: Administrator
 * Date: 2022年11月
 * Copyright (c) 2022 http://wxdgut.com
 * Email: hello_luyao@163.com
 * Profile: 万能的RecyclerView.ViewHolder
 */
public class CommonViewHolder extends RecyclerView.ViewHolder {

    //子View的集合
    private SparseArray<View> mViews;
    private View mContentView;

    //处理点击事件的接口,isChange用于记录点击状态（按需采用）
    public interface MyListener {
        //isChange 能起到类似开关标志的作用
        void click(boolean isChange);
    }

    private CommonViewHolder(View itemView) {
        super(itemView);
        mViews = new SparseArray<>();
        mContentView = itemView;
    }

    /**
     * 提供给外部访问View的方法
     *
     * @param viewId
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(final int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mContentView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 获取CommonViewHolder实体
     *
     * @param parent
     * @param layoutId
     * @return
     */
    public static CommonViewHolder getViewHolder(ViewGroup parent, int layoutId) {
        View itemView = View.inflate(parent.getContext(), layoutId, null);
        return new CommonViewHolder(itemView);
    }

    //-------> 注意：以下方法都可以不写，可以先调用getView(R.id.exampleID)获取到控件再进行想要的操作 <-------

    /**
     * 设置控件的显示隐藏
     *
     * @param viewId
     * @param status -1:GONE, 0:INVISIBLE, 1:VISIBLE
     */
    public void setViewVisibility(final int viewId, int status) {
        if (status == -1 || status == 0 || status == 1) {
            View view = getView(viewId);
            view.setVisibility(status == -1 ? View.GONE : (status == 1 ? View.VISIBLE : View.INVISIBLE));
        }
    }

    /**
     * 根据标志把View 显示或gone掉
     *
     * @param isGone
     * @param views
     */
    public void goneView(boolean isGone, View... views) {
        if (views == null) return;
        for (View view : views) {
            view.setVisibility(isGone ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * 根据标志把View 显示或gone掉
     *
     * @param isGone
     * @param viewIds
     */
    public void goneView(boolean isGone, int... viewIds) {
        if (viewIds == null) return;
        for (int viewId : viewIds) {
            getView(viewId).setVisibility(isGone ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * 设置控件的点击事件
     * 也可以不用此方法，
     * 可以先调用getView(R.id.exampleID)获取到控件再setOnClickListener
     *
     * @param viewId
     * @param listener
     */
    public void setClick(final int viewId, MyListener listener) {
        View view = getView(viewId);
        if (view == null || listener == null) return;
        final boolean[] state = {false};
        view.setOnClickListener(v -> {
            state[0] = !state[0];
            listener.click(state[0]);
        });
    }

    /**
     * 设置控件的点击事件
     *
     * @param viewId
     * @param listener
     */
    public void setClick(final int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        if (view == null || listener == null) return;
        view.setOnClickListener(listener);
    }


    /**
     * 设置控件是否可点击
     * 注意：如果该控件有点击事件，则要在控件的点击事件之后设置，否则会失效
     *
     * @param viewId
     * @param clickable
     */
    public void setClickable(final int viewId, boolean clickable) {
        View view = getView(viewId);
        view.setClickable(clickable);
    }

    /**
     * 设置控件点击是否可用
     *
     * @param viewId
     * @param enabled
     */
    public void setEnabled(final int viewId, boolean enabled) {
        View view = getView(viewId);
        view.setEnabled(enabled);
    }

    /**
     * 设置TextView文本
     *
     * @param viewId
     * @param text
     */
    public void setText(final int viewId, String text) {
        if (text == null) return;
        TextView textView = getView(viewId);
        textView.setText(text);
    }

    /**
     * 设置背景颜色
     * 注意：参数2应传 getResources().getColor(R.color.white) 而不是 直接使用 R.color.white
     *
     * @param viewId
     * @param color
     */
    public void setBackgroundColor(final int viewId, int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
    }

    /**
     * 设置背景颜色
     * 注意：参数3可以直接使用 R.color.white
     *
     * @param viewId
     * @param context
     * @param color
     */
    public void setBackgroundColor(final int viewId, Context context, int color) {
        View view = getView(viewId);
        view.setBackgroundColor(context.getResources().getColor(color));
    }

    /**
     * 设置背景颜色
     *
     * @param viewId
     * @param colorStr
     */
    public void setBackgroundColor(final int viewId, String colorStr) {
        if (colorStr == null || TextUtils.isEmpty(colorStr)) return;
        View view = getView(viewId);
        view.setBackgroundColor(Color.parseColor(colorStr));
    }

    /**
     * 设置TextView文本颜色
     * 注意：参数2应传 getResources().getColor(R.color.white) 而不是 直接使用 R.color.white
     *
     * @param viewId
     * @param color
     */
    public void setTextColor(final int viewId, int color) {
        TextView textView = getView(viewId);
        textView.setTextColor(color);
    }

    /**
     * 设置TextView文本颜色
     * 注意：参数3可以直接使用 R.color.white
     *
     * @param viewId
     * @param context
     * @param color
     */
    public void setTextColor(final int viewId, Context context, int color) {
        TextView textView = getView(viewId);
        textView.setTextColor(context.getResources().getColor(color));
    }

    /**
     * 设置TextView文本颜色
     *
     * @param viewId
     * @param colorStr
     */
    public void setTextColor(final int viewId, String colorStr) {
        if (colorStr == null || TextUtils.isEmpty(colorStr)) return;
        TextView textView = getView(viewId);
        textView.setTextColor(Color.parseColor(colorStr));
    }

    /**
     * 设置图片
     *
     * @param viewId
     * @param resId
     */
    public void setImageResource(final int viewId, int resId) {
        ImageView imageView = getView(viewId);
        imageView.setImageResource(resId);
    }

}
