package com.wxdgut.uilibrary.rv.pro;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;


/**
 * 可以添加头部和底部的RecyclerView
 */
public class CommonRecyclerView extends RecyclerView {
    // 包裹了一层的头部底部Adapter
    private CommonWrapAdapter mAdapter;

    public CommonRecyclerView(@NonNull Context context) {
        super(context);
    }

    public CommonRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CommonRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    //重写方法
    @Override
    public void setAdapter(@Nullable Adapter adapter) {
        mAdapter = (CommonWrapAdapter) adapter;
        //设置适配器
        super.setAdapter(mAdapter);
        // 解决GridLayout添加头部和底部也要占据一行
        mAdapter.adjustSpanSize(this);
    }
}
