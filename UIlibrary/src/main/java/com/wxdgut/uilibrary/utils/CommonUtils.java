package com.wxdgut.uilibrary.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

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

    //将Android中的颜色值添加透明度。其中0表示完全透明，255表示完全不透明。
    public static int addAlphaToColorByColorId(Context context, int colorId, int alpha) {
        int color = ContextCompat.getColor(context, colorId);
        if (alpha < 0 || alpha > 100) {
            //throw new IllegalArgumentException("透明度范围应为0-100");
            alpha = 100;
        }
        int alphaValue = (int) (alpha * 2.55); // 将透明度转换为0-255范围内的值
        int red = (color >> 16) & 0xFF; // 提取红色分量
        int green = (color >> 8) & 0xFF; // 提取绿色分量
        int blue = color & 0xFF; // 提取蓝色分量
        int colorWithAlpha = (alphaValue << 24) | (red << 16) | (green << 8) | blue; // 添加透明度
        return colorWithAlpha;
    }

    //字符串判空
    public static boolean isEmptyOrNull(String str) {
        if (null == str || "".equals(str) || " ".equals(str) || "null".equals(str)) {
            return true;
        }
        return false;
    }

    //返回的是屏幕尺寸的物理像素（px）
    public static Point getScreenSize(Context context) {
        Point screenSize = new Point();

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        if (windowManager != null) {
            Display display = windowManager.getDefaultDisplay();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                display.getRealSize(screenSize);
            } else {
                display.getSize(screenSize);
            }
        }

        return screenSize;
    }

    //返回的是屏幕尺寸的物理像素（px）
    public static int getScreenWidth(Context context) {
        return getScreenSize(context).x;
    }

    //返回的是屏幕尺寸的物理像素（px）
    public static int getScreenHeight(Context context) {
        return getScreenSize(context).y;
    }
}