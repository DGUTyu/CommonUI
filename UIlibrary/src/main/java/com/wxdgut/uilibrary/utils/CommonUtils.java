package com.wxdgut.uilibrary.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import com.wxdgut.uilibrary.globalgray.ObservableArrayList;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * 对某个页面开启灰色调或恢复颜色
     *
     * @param view
     * @param isGray
     */
    public static void grayView(View view, boolean isGray) {
        try {
            Paint paint = new Paint();
            ColorMatrix cm = new ColorMatrix();
            if (isGray) {
                cm.setSaturation(0f);
            } else {
                cm.setSaturation(1f);
            }
            paint.setColorFilter(new ColorMatrixColorFilter(cm));
            view.setLayerType(View.LAYER_TYPE_HARDWARE, paint);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否全局置灰。此方法一般用于Application的onCreate方法中
     *
     * @param isGray
     */
    public static void globalGray(boolean isGray) {
        if (!isGray) {
            return;
        }
        try {
            //灰色调Paint
            final Paint mPaint = new Paint();
            ColorMatrix mColorMatrix = new ColorMatrix();
            mColorMatrix.setSaturation(0f);
            mPaint.setColorFilter(new ColorMatrixColorFilter(mColorMatrix));

            //反射获取windowManagerGlobal
            @SuppressLint("PrivateApi")
            Class<?> windowManagerGlobal = Class.forName("android.view.WindowManagerGlobal");
            @SuppressLint("DiscouragedPrivateApi")
            java.lang.reflect.Method getInstanceMethod = windowManagerGlobal.getDeclaredMethod("getInstance");
            getInstanceMethod.setAccessible(true);
            Object windowManagerGlobalInstance = getInstanceMethod.invoke(windowManagerGlobal);

            //反射获取mViews
            Field mViewsField = windowManagerGlobal.getDeclaredField("mViews");
            mViewsField.setAccessible(true);
            Object mViewsObject = mViewsField.get(windowManagerGlobalInstance);

            //创建具有数据感知能力的ObservableArrayList
            ObservableArrayList<View> observerArrayList = new ObservableArrayList<>();
            observerArrayList.addOnListChangedListener(new ObservableArrayList.OnListChangeListener() {
                @Override
                public void onChange(ArrayList list, int index, int count) {

                }

                @Override
                public void onAdd(ArrayList list, int start, int count) {
                    View view = (View) list.get(start);
                    if (view != null) {
                        view.setLayerType(View.LAYER_TYPE_HARDWARE, mPaint);
                    }
                }

                @Override
                public void onRemove(ArrayList list, int start, int count) {

                }
            });
            //将原有的数据添加到新创建的list
            observerArrayList.addAll((ArrayList<View>) mViewsObject);
            //替换掉原有的mViews
            mViewsField.set(windowManagerGlobalInstance, observerArrayList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 重启APP
     *
     * @param context
     */
    public static void restartApp(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setClassName(context.getPackageName(),
                getLauncherActivity(context, context.getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    /**
     * 获取指定包名 pkg 的启动器活动的完整类名。方法返回一个字符串，该字符串是启动器活动的完整类名。
     *
     * @param context
     * @param pkg
     * @return
     */
    public static String getLauncherActivity(Context context, String pkg) {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setPackage(pkg);
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> info = pm.queryIntentActivities(intent, 0);
        return info.get(0).activityInfo.name;
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object object = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = (Integer) field.get(object);
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (statusBarHeight == 0) {
            statusBarHeight = dpToPx(25);
        }
        return statusBarHeight;
    }

    /**
     * 获得两点之间的距离
     * Math.pow() 计算一个数的指定次幂:第一个参数是底数,第二个参数是指数
     *
     * @param p1
     * @param p2
     * @return
     */
    public static float getDistanceBetweenTwoPoints(PointF p1, PointF p2) {
        float distance = (float) Math.sqrt(Math.pow(p1.y - p2.y, 2) + Math.pow(p1.x - p2.x, 2));
        return distance;
    }

    /**
     * 根据百分比获取两点之间的某个点坐标
     *
     * @param p1
     * @param p2
     * @param percent
     * @return
     */
    public static PointF getPointBetweenTwoPoints(PointF p1, PointF p2, float percent) {
        //if (percent < 0 || percent > 1) { //不建议写
        //throw new IllegalArgumentException("Percentage should be between 0 and 1");
        //}
        float x = evaluateValue(percent, p1.x, p2.x);
        float y = evaluateValue(percent, p1.y, p2.y);
        return new PointF(x, y);
    }


    /**
     * 在两个数值之间按照指定的百分比进行插值计算。
     *
     * @param fraction
     * @param start
     * @param end
     * @return
     */
    public static float evaluateValue(float fraction, Number start, Number end) {
        return start.floatValue() + (end.floatValue() - start.floatValue()) * fraction;
    }
}