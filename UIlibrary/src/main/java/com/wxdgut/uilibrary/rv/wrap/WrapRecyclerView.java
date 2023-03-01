package com.wxdgut.uilibrary.rv.wrap;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.wxdgut.uilibrary.rv.CommonAdapter;


/**
 * 可以添加头部和底部的RecyclerView
 * 与 CommonAdapter 搭配使用
 */
public class WrapRecyclerView extends RecyclerView {
    // 包裹了一层的头部底部Adapter
    private CommonAdapter commonAdapter;

    // 增加一些通用功能
    // 空列表数据应该显示的空View
    // 加载页，也就是正在获取后台接口页面
    private View mEmptyView, mLoadingView;

    //数据观察者
    private AdapterDataObserver mDataObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            dataChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            dataChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
            dataChanged();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            dataChanged();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            dataChanged();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            dataChanged();
        }
    };

    /**
     * Adapter数据改变的方法
     */
    private void dataChanged() {
        if (mLoadingView != null) mLoadingView.setVisibility(GONE);
        Adapter adapter = super.getAdapter();
        int itemCount = adapter.getItemCount();
        if (adapter instanceof CommonAdapter)
            itemCount = ((CommonAdapter) adapter).getRealItemCount();
        if (itemCount <= 0) { // 没有数据
            if (mEmptyView != null) {
                mEmptyView.setVisibility(VISIBLE);
                if (getVisibility() == VISIBLE) setVisibility(GONE);
            }
        } else { // 有数据
            if (mEmptyView != null) {
                mEmptyView.setVisibility(GONE);
                if (getVisibility() == INVISIBLE || getVisibility() == GONE) setVisibility(VISIBLE);
            }
        }
    }

    public WrapRecyclerView(@NonNull Context context) {
        super(context);
    }

    public WrapRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WrapRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    //重写方法
    @Override
    public void setAdapter(@Nullable Adapter adapter) {
        if (adapter instanceof CommonAdapter) {
            commonAdapter = (CommonAdapter) adapter;

            //设置适配器
            super.setAdapter(commonAdapter);

            // 注册一个观察者
            commonAdapter.registerAdapterDataObserver(mDataObserver);

            // 解决GridLayout添加头部和底部也要占据一行
            commonAdapter.adjustSpanSize(this);
        } else {
            super.setAdapter(adapter);
            adapter.registerAdapterDataObserver(mDataObserver);
        }
        // 加载页
        if (mLoadingView != null) {
            setVisibility(GONE); //不建议用INVISIBLE，如果该RecyclerView是match_parent时会覆盖其他布局
            mLoadingView.setVisibility(View.VISIBLE);
        }
    }

    //返回CommonAdapter
    public CommonAdapter getCommonAdapter() {
        return commonAdapter;
    }

    //返回真实的适配器
    public Adapter getRealAdapter() {
        return super.getAdapter();
    }

    /**
     * 添加一个正在加载数据的页面
     */
    public void addLoadingView(View loadingView) {
        this.mLoadingView = loadingView;
    }

    /**
     * 添加一个空列表数据页面
     */
    public void addEmptyView(View emptyView) {
        this.mEmptyView = emptyView;
    }

    /**
     * 添加FloatingActionButton监听
     */
    public void addFloatActionButton(FloatingActionButton button, int firstVisibleItemPosition) {
        if (button == null || firstVisibleItemPosition < 0) return;
        button.setOnClickListener(v -> {
            smoothScrollToPosition(0);
        });
        this.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager != null) {
                    if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                        int position = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                        Log.e("TAG", "onScrollStateChanged: position:" + position);
                        if (position > 5) {
                            button.show();
                        } else {
                            button.hide();
                        }
                    }
                }
            }
        });
    }

    public void addFloatActionButton(FloatingActionButton button) {
        addFloatActionButton(button, 5);
    }
}
