package com.wxdgut.uilibrary.utils;

import android.content.res.Resources;
import android.view.View;
import android.view.ViewTreeObserver;

public class CommonUtils {
    //位置信息回调接口
    public interface LocationCallback {
        void onLocationObtained(int x, int y);
    }

    /**
     * 获取View在视图中的位置
     *
     * @param view
     * @param callback
     */
    public static void getLocationInWindow(final View view, final LocationCallback callback) {
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int[] location = new int[2];
                view.getLocationInWindow(location);

                if (callback != null) {
                    callback.onLocationObtained(location[0], location[1]);
                }

                // 注销监听器，以避免重复调用
                if (view.getViewTreeObserver().isAlive()) {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
    }

    //处理点击事件的接口,isChange用于记录点击状态（按需采用）
    public interface ClickListener {
        //isChange 能起到类似开关标志的作用，isChange首次回调为true，此后依次反转
        void click(View view, boolean isChange);
    }

    //设置点击监听器，也可以不用此做法
    public static void setClick(View view, ClickListener clickListener) {
        final boolean[] state = {false};
        view.setOnClickListener(v -> {
            state[0] = !state[0];
            clickListener.click(v, state[0]);
        });
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static final int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (pxValue / scale);
    }
}