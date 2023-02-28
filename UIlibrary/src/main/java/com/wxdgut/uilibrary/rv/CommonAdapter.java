package com.wxdgut.uilibrary.rv;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.wxdgut.uilibrary.rv.base.BaseAdapter;

import java.util.List;

/**
 * 可以添加头部和底部的RecyclerView适配器
 */
public class CommonAdapter<T> extends BaseAdapter {
    private SparseArray<View> mHeaderViews;
    private SparseArray<View> mFooterViews;
    // 基本的头部类型开始位置  用于viewType
    private static int BASE_ITEM_TYPE_HEADER = 10000000;
    // 基本的底部类型开始位置  用于viewType
    private static int BASE_ITEM_TYPE_FOOTER = 20000000;

    public CommonAdapter(List<T> mList, OnBindDataListener<T> onBindDataListener) {
        super(mList, onBindDataListener);
        mHeaderViews = new SparseArray<>();
        mFooterViews = new SparseArray<>();
    }

    public CommonAdapter(List<T> mList, OnMoreBindDataListener<T> onMoreBindDataListener) {
        super(mList, onMoreBindDataListener);
        mHeaderViews = new SparseArray<>();
        mFooterViews = new SparseArray<>();
    }

    @NonNull
    @Override
    public CommonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // viewType 可能就是 SparseArray 的key
        if (isHeaderViewType(viewType)) {
            View headerView = mHeaderViews.get(viewType);
            return createHeaderFooterViewHolder(headerView);
        }

        if (isFooterViewType(viewType)) {
            View footerView = mFooterViews.get(viewType);
            return createHeaderFooterViewHolder(footerView);
        }

        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, int position) {
        if (isHeaderPosition(position) || isFooterPosition(position)) {
            return; //此时不能调用 super.onBindViewHolder(holder, adapterPosition);
        }
        // 计算一下位置
        final int adapterPosition = position - mHeaderViews.size();
        super.onBindViewHolder(holder, adapterPosition);
    }

    @Override
    public int getItemCount() {
        // 条数三者相加 = Adapter的条数 + 头部条数 + 底部条数
        return super.getItemCount() + mHeaderViews.size() + mFooterViews.size();
    }

    //额外重写
    @Override
    public int getItemViewType(int position) {
        //return super.getItemViewType(position);
        if (isHeaderPosition(position)) {
            // 直接返回position位置的key
            return mHeaderViews.keyAt(position);
        }
        if (isFooterPosition(position)) {
            // 直接返回position位置的key
            position = position - mHeaderViews.size() - super.getItemCount();
            return mFooterViews.keyAt(position);
        }
        // 返回列表Adapter的getItemViewType
        position = position - mHeaderViews.size();
        return super.getItemViewType(position);
    }

    //************************* 私有方法 *************************
    //是不是头部类型
    private boolean isHeaderViewType(int viewType) {
        int position = mHeaderViews.indexOfKey(viewType);
        return position >= 0;
    }

    //是不是底部类型
    private boolean isFooterViewType(int viewType) {
        int position = mFooterViews.indexOfKey(viewType);
        return position >= 0;
    }

    //创建头部或者底部的ViewHolder
    private CommonViewHolder createHeaderFooterViewHolder(View view) {
        return new CommonViewHolder(view) {
        };
    }

    //是不是头部位置
    private boolean isHeaderPosition(int position) {
        return position < mHeaderViews.size();
    }

    //是不是底部位置
    private boolean isFooterPosition(int position) {
        return position >= (mHeaderViews.size() + super.getItemCount());
    }

    //************************* 公开方法 *************************
    //添加头部
    public void addHeaderView(View view) {
        int position = mHeaderViews.indexOfValue(view);
        if (position < 0) {
            mHeaderViews.put(BASE_ITEM_TYPE_HEADER++, view);
        }
        notifyDataSetChanged();
    }

    //添加底部
    public void addFooterView(View view) {
        int position = mFooterViews.indexOfValue(view);
        if (position < 0) {
            mFooterViews.put(BASE_ITEM_TYPE_FOOTER++, view);
        }
        notifyDataSetChanged();
    }

    //移除头部
    public void removeHeaderView(View view) {
        int index = mHeaderViews.indexOfValue(view);
        if (index < 0) return;
        mHeaderViews.removeAt(index);
        notifyDataSetChanged();
    }

    //移除底部
    public void removeFooterView(View view) {
        int index = mFooterViews.indexOfValue(view);
        if (index < 0) return;
        mFooterViews.removeAt(index);
        notifyDataSetChanged();
    }

    //获取头部view的数量
    public int getHeaderCounts() {
        return mHeaderViews.size();
    }

    //获取底部view的数量
    public int getFooterCounts() {
        return mFooterViews.size();
    }

    //获取头部view
    public SparseArray<View> getHeaders() {
        return mFooterViews;
    }

    //获取底部view
    public SparseArray<View> getFooters() {
        return mFooterViews;
    }

    //解决GridLayoutManager添加头部和底部不占用一行的问题
    public void adjustSpanSize(RecyclerView recycler) {
        if (recycler.getLayoutManager() instanceof GridLayoutManager) {
            final GridLayoutManager layoutManager = (GridLayoutManager) recycler.getLayoutManager();
            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    boolean isHeaderOrFooter =
                            isHeaderPosition(position) || isFooterPosition(position);
                    return isHeaderOrFooter ? layoutManager.getSpanCount() : 1;
                }
            });
        }
    }

    //真实的getItemCount，不包含头部、尾部
    public int getRealItemCount() {
        //Adapter的条数，即mList.size
        return super.getItemCount();
    }
}
