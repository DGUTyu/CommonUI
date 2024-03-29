package com.wxdgut.uilibrary.rv.refresh;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.wxdgut.uilibrary.rv.CommonAdapter;
import com.wxdgut.uilibrary.rv.view_creator.DefaultRefreshCreator;
import com.wxdgut.uilibrary.rv.wrap.WrapRecyclerView;
import com.wxdgut.uilibrary.rv.view_creator.RefreshViewCreator;

public class RefreshRecyclerView extends WrapRecyclerView {
    // 下拉刷新的辅助类
    private RefreshViewCreator mRefreshCreator;
    // 下拉刷新头部的高度
    private int mRefreshViewHeight = 0;
    // 下拉刷新的头部View
    private View mRefreshView;
    // 手指按下的Y位置
    private int mFingerDownY;
    // 手指拖拽的阻力指数
    protected float mDragIndex = 0.35f;
    // 当前是否正在拖动
    private boolean mCurrentDrag = false;
    // 当前的状态
    private int mCurrentRefreshStatus;
    // 默认状态
    private int REFRESH_STATUS_NORMAL = 0x0011;
    // 下拉刷新状态
    private int REFRESH_STATUS_PULL_DOWN_REFRESH = 0x0022;
    // 松开刷新状态
    private int REFRESH_STATUS_LOOSEN_REFRESHING = 0x0033;
    // 正在刷新状态
    private int REFRESH_STATUS_REFRESHING = 0x0044;
    // 处理刷新回调监听
    private OnRefreshListener mListener;

    public interface OnRefreshListener {
        void onRefresh();
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.mListener = listener;
    }

    //设置下拉监听器的同时使用默认的下拉样式
    public void setOnRefreshListener(OnRefreshListener listener, boolean useDefaultStyle) {
        this.mListener = listener;
        if (useDefaultStyle) addDefaultRefreshViewCreator();
    }

    public RefreshRecyclerView(@NonNull Context context) {
        super(context);
    }

    public RefreshRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    //重写方法
    @Override
    public void setAdapter(@Nullable Adapter adapter) {
        super.setAdapter(adapter);
        addRefreshView(); //get CommonAdapter为null,不执行
    }


    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                // 如果是在最顶部才处理，否则不需要处理
                if (canScrollUp() || mCurrentRefreshStatus == REFRESH_STATUS_REFRESHING
                        || mRefreshView == null || mRefreshCreator == null) {
                    // 如果没有到达最顶端，也就是说还可以向上滚动就什么都不处理
                    return super.onTouchEvent(e);
                }

                // 解决下拉刷新自动滚动问题
                if (mCurrentDrag) {
                    scrollToPosition(0);
                }

                // 获取手指触摸拖拽的距离
                int distanceY = (int) ((e.getRawY() - mFingerDownY) * mDragIndex);
                // 如果是已经到达头部，并且不断的向下拉，那么不断的改变refreshView的marginTop的值
                if (distanceY > 0) {
                    int marginTop = distanceY - mRefreshViewHeight;
                    setRefreshViewMarginTop(marginTop);
                    updateRefreshStatus(distanceY);
                    mCurrentDrag = true;
                    return false;
                }
                break;
        }

        return super.onTouchEvent(e);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 记录手指按下的位置 ,之所以写在dispatchTouchEvent那是因为如果我们处理了条目点击事件，
                // 那么就不会进入onTouchEvent里面，所以只能在这里获取
                mFingerDownY = (int) ev.getRawY();
                break;

            case MotionEvent.ACTION_UP:
                if (mCurrentDrag) {
                    restoreRefreshView();
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //if (changed) {
        if (mRefreshView != null && mRefreshViewHeight <= 0) {
            // 获取头部刷新View的高度
            mRefreshViewHeight = mRefreshView.getMeasuredHeight();
            if (mRefreshViewHeight > 0) {
                // 隐藏头部刷新的View  marginTop  多留出1px防止无法判断是不是滚动到头部问题
                setRefreshViewMarginTop(-mRefreshViewHeight + 1);
            }
        }
        // }
    }

    //************************* 私有方法 *************************
    //添加头部的刷新View
    private void addRefreshView() {
        CommonAdapter adapter = getCommonAdapter();
        if (adapter != null && mRefreshCreator != null) {
            // 添加头部的刷新View
            View refreshView = mRefreshCreator.getRefreshView(getContext(), this);
            if (refreshView != null) {
                adapter.addHeaderView(refreshView);
                this.mRefreshView = refreshView;
            }
        }
    }

    //判断是不是滚动到了最顶部
    private boolean canScrollUp() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            return this.canScrollVertically(-1) || this.getScrollY() > 0;
        } else {
            return this.canScrollVertically(-1);
        }
    }

    //设置刷新View的marginTop
    private void setRefreshViewMarginTop(int marginTop) {
        MarginLayoutParams params = (MarginLayoutParams) mRefreshView.getLayoutParams();
        if (marginTop < -mRefreshViewHeight + 1) {
            marginTop = -mRefreshViewHeight + 1;
        }
        params.topMargin = marginTop;
        mRefreshView.setLayoutParams(params);
    }

    //更新刷新的状态
    private void updateRefreshStatus(int distanceY) {
        if (distanceY <= 0) {
            mCurrentRefreshStatus = REFRESH_STATUS_NORMAL;
        } else if (distanceY < mRefreshViewHeight) {
            mCurrentRefreshStatus = REFRESH_STATUS_PULL_DOWN_REFRESH;
        } else {
            mCurrentRefreshStatus = REFRESH_STATUS_LOOSEN_REFRESHING;
        }

        if (mRefreshCreator != null) {
            mRefreshCreator.onPull(distanceY, mRefreshViewHeight, mCurrentRefreshStatus);
        }
    }

    //重置当前刷新状态状态
    private void restoreRefreshView() {
        int currentTopMargin = ((MarginLayoutParams) mRefreshView.getLayoutParams()).topMargin;
        int finalTopMargin = -mRefreshViewHeight + 1;
        if (mCurrentRefreshStatus == REFRESH_STATUS_LOOSEN_REFRESHING) {
            finalTopMargin = 0;
            mCurrentRefreshStatus = REFRESH_STATUS_REFRESHING;
            if (mRefreshCreator != null) {
                mRefreshCreator.onRefreshing();
            }
            if (mListener != null) {
                mListener.onRefresh();
            }
        }

        int distance = currentTopMargin - finalTopMargin;

        // 回弹到指定位置
        ValueAnimator animator = ObjectAnimator.ofFloat(currentTopMargin, finalTopMargin).setDuration(distance);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentTopMargin = (float) animation.getAnimatedValue();
                setRefreshViewMarginTop((int) currentTopMargin);
            }
        });
        animator.start();
        mCurrentDrag = false;
    }

    //************************* 公开方法 *************************
    // 先处理下拉刷新，同时考虑刷新列表的不同风格样式，确保这个项目还是下一个项目都能用
    // 所以我们不能直接添加View，需要利用辅助类
    public void addRefreshViewCreator(RefreshViewCreator refreshCreator) {
        this.mRefreshCreator = refreshCreator;
        addRefreshView();
    }

    //添加默认下拉样式
    public void addDefaultRefreshViewCreator() {
        addRefreshViewCreator(new DefaultRefreshCreator());
    }

    //停止刷新
    public void onStopRefresh() {
        mCurrentRefreshStatus = REFRESH_STATUS_NORMAL;
        restoreRefreshView();
        if (mRefreshCreator != null) {
            mRefreshCreator.onStopRefresh();
        }
    }
}
