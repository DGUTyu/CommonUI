package com.wxdgut.uilibrary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wxdgut.uilibrary.R;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static android.content.Context.WINDOW_SERVICE;

/**
 * FileName: CommonDialog2
 * Founder: lu yao
 * User: Administrator
 * Date: 2022年11月
 * Copyright (c) 2022 http://wxdgut.com
 * Email: hello_luyao@163.com
 * Profile: 万能的Dialog
 * 对页面回退做了监听，默认页面回退时关闭Dialog
 */
public class CommonDialog extends Dialog {
    //TAG
    protected String TAG = CommonDialog.class.getCanonicalName();
    //默认弹窗无动画效果，用-1表示
    protected static final int DEFAULT_ANIM = -1;
    //取消类型
    public static final int NORMAL_CANCEL = 1;
    public static final int BACK_CANCEL = 2;

    //子View的集合
    private SparseArray<View> mViews;

    //用于更新移动后的View
    private final WindowManager.LayoutParams layoutParams;
    private final WindowManager wm;
    //是否移动
    private boolean isMove = false;
    //是否拖拽
    private boolean isDrag = false;
    //点击坐标是否合适
    private boolean flag;
    //最新的坐标
    private int mLastY;
    private int mLastX;

    //用于获取Builder 参数
    final Context context; //上下文
    final int layout; //Dialog 布局文件id
    final int animId; //动画效果id
    final boolean widthMatch; //弹窗宽度是否MATCH_PARENT
    final int gravity; //居中方式
    final int styleId; //Dialog 默认样式id
    final boolean draggable; //Dialog 是否支持拖拽
    final boolean cancelable; //Dialog 是否支持自动关闭

    //处理点击事件的接口,isChange用于记录点击状态（按需采用）
    public interface MyListener {
        //isChange 能起到类似开关标志的作用
        void click(View view, boolean isChange);
    }

    //对外抛出处理取消Dialog的接口
    private MyCancelListener cancelListener = null;

    //处理取消事件的接口
    public interface MyCancelListener {
        void afterCancel(int cancelType);
    }

    //设置取消事件监听器
    public void setMyCancelListener(MyCancelListener listener) {
        if (listener != null) {
            cancelListener = listener;
        }
    }

    /**
     * 提供给外部访问View的方法
     *
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = this.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
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
            listener.click(v, state[0]);
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
     * 设置TextView文本
     *
     * @param viewId
     * @param stringId
     */
    public void setText(final int viewId, int stringId) {
        if (context == null) return;
        String string = context.getResources().getString(stringId);
        setText(viewId, string);
    }

    /**
     * 设置背景颜色
     *
     * @param viewId
     * @param color
     */
    public void setBackgroundColor(final int viewId, int color) {
        if (context == null) return;
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
     *
     * @param viewId
     * @param color
     */
    public void setTextColor(final int viewId, int color) {
        if (context == null) return;
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

    /**
     * 设置背景样式
     * 注意：如果shape是有点击样式的，则单纯设置setBackgroundShape会无法显示state_pressed的样式，
     * state_pressed样式需要该控件setClickable(true)或有点击事件监听器才会起作用
     *
     * @param viewId
     * @param shape
     * @return
     */
    public void setBackgroundShape(final int viewId, int shape) {
        if (context == null) return;
        View view = getView(viewId);
        Drawable drawable = context.getResources().getDrawable(shape);
        if (drawable != null) {
            view.setBackground(drawable);
        }
    }

    /**
     * 获取Dialog中的EditText控件
     *
     * @param viewId
     * @return
     */
    public EditText getEditText(final int viewId) {
        View view = getView(viewId);
        if (view instanceof EditText) return (EditText) view;
        return null;
    }


    /**
     * 获取Dialog中的EditText控件的文本内容
     *
     * @param viewId
     * @return
     */
    public String getEditTextStr(final int viewId) {
        EditText editText = getEditText(viewId);
        if (editText != null) return editText.getText().toString().trim();
        return null;
    }

    /**
     * 设置Dialog中的EditText控件的文本内容
     *
     * @param viewId
     * @param str
     */
    public void setEditTextStr(final int viewId, String str) {
        EditText editText = getEditText(viewId);
        if (editText != null && str != null) editText.setText(str);
    }

    /**
     * 设置Dialog中的EditText控件的文本内容
     *
     * @param viewId
     * @param strId
     */
    public void setEditTextStr(final int viewId, int strId) {
        if (context == null) return;
        String str = context.getResources().getString(strId);
        setEditTextStr(viewId, str);
    }

    /**
     * 清空Dialog中的EditText控件的输入内容
     *
     * @param viewId
     */
    public void clearEditText(final int viewId) {
        EditText editText = getEditText(viewId);
        if (editText != null) editText.setText("");
    }

    /**
     * 清空Dialog中的EditText控件的输入内容
     *
     * @param viewIds
     */
    public void clearEditText(final int... viewIds) {
        for (int viewId : viewIds) {
            clearEditText(viewId);
        }
    }


    //显示Dialog
    public void showDialog() {
        if (this != null && !isShowing()) {
            Log.i(TAG, "show");
            show();
        }
    }

    //隐藏Dialog
    public void hideDialog() {
        if (this != null && isShowing()) {
            Log.i(TAG, "hide");
            hide();
        }
    }

    //销毁Dialog
    public void dismissDialog() {
        if (this != null) {
            Log.i(TAG, "dismiss");
            dismiss();
            mViews = null;
        }
    }

    private CommonDialog(Builder builder) {
        //初始化
        super(builder.context, builder.styleId);
        //获取参数
        this.context = builder.context;
        this.layout = builder.layout;
        this.animId = builder.animId;
        this.widthMatch = builder.widthMatch;
        this.gravity = builder.gravity;
        this.styleId = builder.styleId;
        this.draggable = builder.draggable;
        this.cancelable = builder.cancelable;
        //设置布局
        setContentView(layout);
        mViews = new SparseArray<>();
        //获取父容器
        Window window = getWindow();
        layoutParams = window.getAttributes();
        wm = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        layoutParams.width = widthMatch ? WindowManager.LayoutParams.MATCH_PARENT : WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = gravity;
        if (animId != DEFAULT_ANIM) layoutParams.windowAnimations = animId;
        window.setAttributes(layoutParams);
        setCancelable(cancelable);
        //初始化事件
        initEvent(window.getDecorView());
    }

    //初始化事件
    private void initEvent(View decorView) {
        Log.i(TAG, "initEvent");
        //setOnCancelListener(dialog -> dismissDialog());
        setOnCancelListener(dialogInterface -> {
            dismissDialog();
            if (cancelListener != null) cancelListener.afterCancel(NORMAL_CANCEL);
        });
        setOnKeyListener((OnKeyListener) (dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                if (cancelable) {
                    dismissDialog();
                    if (cancelListener != null) cancelListener.afterCancel(BACK_CANCEL);
                    return true;
                }
            }
            return false;
        });
        //是否设置移动事件
        if (draggable) {
            if (decorView != null) setDrag(decorView, layoutParams);
            else Toast.makeText(context, "decorView == null", Toast.LENGTH_SHORT).show();
        }

    }

    //建造者模式
    public static class Builder {
        Context context;
        int layout;
        boolean widthMatch;
        int gravity;
        int styleId;
        boolean draggable;
        boolean cancelable;
        int animId;

        /**
         * 默认配置
         *
         * @param context
         */
        public Builder(Context context) {
            this.context = context;
            this.widthMatch = false;
            this.gravity = Gravity.CENTER;
            this.layout = R.layout.dialog_fingerprint;
            this.animId = DEFAULT_ANIM;
            //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //API 21
            //this.styleId = android.R.style.Theme_Material_Dialog_NoActionBar;
            //}
            this.styleId = R.style.My_Dialog_Theme;
            this.draggable = false;
            this.cancelable = true;
        }

        /**
         * @param layout Dialog 布局文件id
         * @return
         */
        public Builder layout(int layout) {
            this.layout = layout;
            return this;
        }

        /**
         * @param animId 动画效果id
         * @return
         */
        public Builder anim(int animId) {
            this.animId = animId;
            return this;
        }

        /**
         * @param widthMatch 弹窗宽度是否 MATCH_PARENT
         * @return
         */
        public Builder widthMatch(boolean widthMatch) {
            this.widthMatch = widthMatch;
            return this;
        }

        /**
         * @param gravity 居中方式
         * @return
         */
        public Builder gravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        /**
         * @param styleId Dialog 默认样式id
         * @return
         */
        public Builder style(int styleId) {
            this.styleId = styleId;
            return this;
        }

        /**
         * @param draggable 是否支持拖拽
         * @return
         */
        public Builder draggable(boolean draggable) {
            this.draggable = draggable;
            return this;
        }

        /**
         * 是否支持自动取消
         * 建议使用这种方式而不是直接对Dialog对象设置setCancelable，
         *
         * @param cancelable 是否支持自动取消
         * @return
         */
        public Builder cancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        /**
         * @return
         */
        public CommonDialog build() {
            return new CommonDialog(this);
        }
    }

    //创建Builder对象
    public static Builder newBuilder(Context context) {
        return new Builder(context);
    }


    //设置移动
    public void setDrag(View view, WindowManager.LayoutParams layoutParams) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                /**
                 * OnTouch 和 OnClick 点击拖拽
                 * 如何判断是拖拽还是 移动
                 * 通过点击下的坐标 - 落地的坐标
                 * 如果移动则说明是移动 如果 = 0 ，那说明没有移动则是点击拖拽
                 */
                int mStartX = (int) motionEvent.getRawX();
                int mStartY = (int) motionEvent.getRawY();

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //判断点击的坐标是否是方块的内部，是的话才可以移动验证码
                        isMove = false;
                        isDrag = false;
                        mLastX = (int) motionEvent.getRawX();
                        mLastY = (int) motionEvent.getRawY();
                        flag = inRangeOfView(mLastX, mLastY, view);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int dx = mStartX - mLastX;  //偏移量
                        int dy = mStartY - mLastY;
                        if (isMove) {
                            isDrag = true;
                        } else {
                            if (dx == 0 && dy == 0) {
                                isMove = false;
                            } else {
                                isMove = true;
                                isDrag = true;
                            }
                            isDrag = false;
                        }
                        if (!flag) { //注意此句代码的位置要出现在isDrag的所有计算之后
                            break; //点击的坐标不在方块的内部
                        }
                        //移动
                        layoutParams.x += dx;
                        if (gravity == Gravity.BOTTOM) {
                            layoutParams.y -= dy;
                        } else {
                            layoutParams.y += dy;
                        }
                        //重置坐标
                        mLastX = mStartX;
                        mLastY = mStartY;
                        //刷新
                        updateView(view, layoutParams);
                        break;
                }
                return isDrag; //把事件往下放
            }
        });
    }

    //更新view的布局
    public void updateView(View view, WindowManager.LayoutParams layoutParams) {
        if (view != null && layoutParams != null) {
            wm.updateViewLayout(view, layoutParams);
        }
    }

    /**
     * @param clickX 点击的 x 坐标
     * @param clickY 点击的 y 坐标
     * @param view
     * @return
     */
    private boolean inRangeOfView(int clickX, int clickY, View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();
        if (clickX < left || clickX > right || clickY < top || clickY > bottom) {
            return false;
        }
        return true;
    }

}