package com.wxdgut.uilibrary.rv.standard;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;


import com.wxdgut.uilibrary.rv.CommonViewHolder;

import java.util.List;

/**
 * FileName: CommonAdapter
 * Founder: lu yao
 * User: Administrator
 * Date: 2022年11月
 * Copyright (c) 2022 http://wxdgut.com
 * Email: hello_luyao@163.com
 * Profile: 万能的RecyclerView适配器
 * item的布局类型由依赖库限定，item的数据model需要继承CommonItemModel才可以使用
 */
public class BaseAdapter<T extends CommonItemModel> extends RecyclerView.Adapter<CommonViewHolder> {
    //列表数据
    private List<T> mList;

    //绑定数据的接口 需要传递类型
    private OnBindDataListener<T> onBindDataListener;
    private OnMoreBindDataListener<T> onMoreBindDataListener;

    //点击事件接口，对item统一设置点击事件
    private MyItemClickListener myItemClickListener;
    private MyItemLongClickListener myItemLongClickListener;

    //点击事件接口
    public interface MyItemClickListener {
        void onItemClick(View view, int position);
    }

    //长点击事件接口
    public interface MyItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    //统一对item设置点击事件
    public void setOnItemClickListener(MyItemClickListener listener) {
        if (listener == null) return;
        this.myItemClickListener = listener;
    }

    //统一对item设置长点击事件
    public void setOnItemLongClickListener(MyItemLongClickListener listener) {
        if (listener == null) return;
        this.myItemLongClickListener = listener;
    }

    //绑定数据接口 （item单布局样式）
    public interface OnBindDataListener<T> {
        //item为多布局样式时，则需要根据type的类型给Adapter传递不同的布局文件
        int getLayoutId(int layoutType);

        void onBindViewHolder(T model, CommonViewHolder viewHolder, int type, int position);
    }

    //绑定多类型的数据接口（item多布局样式，如标题栏、内容item）
    //根据position确定当前item，拿到item后需要返回该item对应的布局类型
    public interface OnMoreBindDataListener<T> extends OnBindDataListener<T> {
        int getItemType(int position);
    }

    public BaseAdapter(List<T> mList, OnBindDataListener<T> onBindDataListener) {
        this.mList = mList;
        this.onBindDataListener = onBindDataListener;
    }

    public BaseAdapter(List<T> mList, OnMoreBindDataListener<T> onMoreBindDataListener) {
        this.mList = mList;
        this.onBindDataListener = onMoreBindDataListener;
        this.onMoreBindDataListener = onMoreBindDataListener;
    }


    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = onBindDataListener.getLayoutId(viewType);
        //创建View，使用静态方法
        CommonViewHolder viewHolder = CommonViewHolder.getViewHolder(parent, layoutId);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, int position) {
        //
        onBindDataListener.onBindViewHolder(mList.get(position), holder,
                getItemViewType(position), position);
        //点击事件回调
        handleClick(myItemClickListener, holder);
        handleLongClick(myItemLongClickListener, holder);
    }

    //实现点击事件回调
    private void handleClick(MyItemClickListener listener, CommonViewHolder holder) {
        if (listener == null || holder == null || holder.itemView == null) return;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getLayoutPosition();
                myItemClickListener.onItemClick(holder.itemView, position);
            }
        });
    }

    //实现长点击事件回调
    private void handleLongClick(MyItemLongClickListener listener, CommonViewHolder holder) {
        if (listener == null || holder == null || holder.itemView == null) return;
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = holder.getLayoutPosition();
                myItemLongClickListener.onItemLongClick(holder.itemView, position);
                //返回true 表示消耗了事件 事件不会继续传递
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    //复写RecyclerView.Adapter的方法getItemViewType
    //根据position确定当前item，拿到item后需要返回该item对应的布局类型
    @Override
    public int getItemViewType(int position) {
        if (onMoreBindDataListener != null) {
            return onMoreBindDataListener.getItemType(position);
        }
        //默认返回CommonItemModel的CONTENT枚举类型值，即 1
        return CommonItemModel.Type.CONTENT.getValue();
    }
}
