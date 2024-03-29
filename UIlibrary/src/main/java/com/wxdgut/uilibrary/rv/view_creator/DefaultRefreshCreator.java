package com.wxdgut.uilibrary.rv.view_creator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.wxdgut.uilibrary.R;


/**
 * Description: 默认样式的刷新头部辅助类
 * 如淘宝、京东、不同的样式可以自己去实现
 */

public class DefaultRefreshCreator extends RefreshViewCreator {
    // 加载数据的ImageView
    private View mRefreshIv;

    @Override
    public View getRefreshView(Context context, ViewGroup parent) {
        View refreshView = LayoutInflater.from(context).inflate(R.layout.layout_refresh_header_view, parent, false);
        mRefreshIv = refreshView.findViewById(R.id.refresh_iv);
        return refreshView;
    }

    @Override
    public void onPull(int currentDragHeight, int refreshViewHeight, int currentRefreshStatus) {
        float rotate = ((float) currentDragHeight) / refreshViewHeight;
        // 不断下拉的过程中旋转图片
        mRefreshIv.setRotation(rotate * 360);
    }

    @Override
    public void onRefreshing() {
        // 刷新的时候不断旋转
        RotateAnimation animation = new RotateAnimation(0, 720,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setRepeatCount(-1);
        animation.setDuration(1000);
        mRefreshIv.startAnimation(animation);
    }

    @Override
    public void onStopRefresh() {
        // 停止加载的时候清除动画
        mRefreshIv.setRotation(0);
        mRefreshIv.clearAnimation();
    }
}
