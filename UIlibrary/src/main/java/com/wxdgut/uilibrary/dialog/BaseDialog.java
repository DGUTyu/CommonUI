package com.wxdgut.uilibrary.dialog;

import static android.content.Context.WINDOW_SERVICE;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

/**
 * 基础弹窗
 */
public class BaseDialog extends Dialog {
    /**
     * 日志Tag
     */
    protected String TAG = BaseDialog.class.getCanonicalName();

    /**
     * 属性未定义
     */
    public static final int UN_DEFINED = -1;


    /**
     * 子View的集合
     */
    private SparseArray<View> mViews;

    /**
     * 上下文
     */
    private Context mContext;

    /**
     * 初始化弹窗的参数bean
     */
    private BaseDialogInitBean mBean;

    /**
     * 用于缓存子view的动画
     */
    private SparseArray<ObjectAnimator> mAnims;

    /**
     * 动画状态:取消
     */
    public static final int ANIM_CANCEL = 0;

    /**
     * 动画状态:开始
     */
    public static final int ANIM_START = 1;

    /**
     * 动画状态:暂停
     */
    public static final int ANIM_PAUSE = 2;

    /**
     * 动画状态:恢复
     */
    public static final int ANIM_RESUME = 3;


    /**
     * 弹窗 布局参数
     */
    private WindowManager.LayoutParams layoutParams;

    /**
     * 弹窗 WindowManager
     */
    private WindowManager wm;

    /**
     * 是否移动
     */
    private boolean isMove = false;
    /**
     * 是否拖拽
     */
    private boolean isDrag = false;
    /**
     * 点击坐标是否合适
     */
    private boolean flag;
    /**
     * 最新的x坐标
     */
    private int mLastY;
    /**
     * 最新的y坐标
     */
    private int mLastX;

    /**
     * 对外抛出处理取消Dialog的接口
     */
    private CommonDialog.MyCancelListener cancelListener = null;
    /**
     * 取消类型：常规取消
     */
    public static final int NORMAL_CANCEL = 1;
    /**
     * 取消类型：返回键取消
     */
    public static final int BACK_CANCEL = 2;


    /**
     * 处理取消事件的接口
     */
    public interface MyCancelListener {
        void afterCancel(int cancelType);
    }

    /**
     * 设置取消事件监听器
     *
     * @param listener 取消事件监听器
     */
    public void setMyCancelListener(CommonDialog.MyCancelListener listener) {
        if (listener != null) {
            cancelListener = listener;
        }
    }

    /**
     * 处理点击事件的接口,isChange用于记录点击状态（按需采用）
     */
    public interface MyListener {
        /**
         * isChange 能起到类似开关标志的作用
         *
         * @param view     view
         * @param isChange true->false交替变化
         */
        void click(View view, boolean isChange);
    }

    /**
     * 初始化方法（对外）
     *
     * @param context context
     * @param bean    bean
     * @return
     */
    public static BaseDialog build(Context context, BaseDialogInitBean bean) {
        return new BaseDialog(context, bean);
    }

    /**
     * 私有化构造方法
     *
     * @param context context
     * @param bean    bean
     */
    private BaseDialog(Context context, BaseDialogInitBean bean) {
        super(context, bean.getStyleId());
        this.mContext = context;
        this.mBean = bean;
        initDialog();
    }

    /**
     * 初始化
     */
    private void initDialog() {
        //设置布局
        setContentView(mBean.getLayout());
        mViews = new SparseArray<>();
        mAnims = new SparseArray<>();
        //获取父容器
        Window window = getWindow();
        layoutParams = window.getAttributes();
        wm = (WindowManager) mContext.getSystemService(WINDOW_SERVICE);
        layoutParams.width = mBean.isWidthMatch() ? WindowManager.LayoutParams.MATCH_PARENT : WindowManager.LayoutParams.WRAP_CONTENT;
        int height = mBean.getHeight();
        layoutParams.height = height == UN_DEFINED ? WindowManager.LayoutParams.WRAP_CONTENT : height;
        layoutParams.gravity = mBean.getGravity();
        if (mBean.getAnimId() != UN_DEFINED) {
            layoutParams.windowAnimations = mBean.getAnimId();
        }
        if (mBean.isClearShadow()) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
        float dimAmount = mBean.getDimAmount();
        //限定取值 完全透明不变暗是0.0f，完全变暗不透明是1.0f
        if (dimAmount > -0.01f && dimAmount < 1.01f) {
            layoutParams.dimAmount = dimAmount;
        }
        window.setAttributes(getShowAsDropDownLp(layoutParams, mBean.getAnchorWR(), mBean.getAnchorGravity(), mBean.getxOff(), mBean.getyOff()));
        //使用完后销毁
        if (mBean.getAnchorWR() != null) {
            mBean.getAnchorWR().clear();
        }
        setCancelable(mBean.isCancelable());
        //初始化事件
        initEvent(window.getDecorView());
    }

    /**
     * 返回Dialog调整后的的WindowManager.LayoutParams
     *
     * @param wlp      Dialog当前的WindowManager.LayoutParams
     * @param anchorWR 控件view
     * @param gravity  相对于view的上下左右
     * @param xOff     x坐标
     * @param yOff     y坐标
     * @return
     */
    private WindowManager.LayoutParams getShowAsDropDownLp(WindowManager.LayoutParams wlp, WeakReference<View> anchorWR, int gravity, int xOff, int yOff) {
        if (anchorWR == null) {
            return wlp;
        }
        View view = anchorWR.get();
        //location [0] 为x绝对坐标;location [1] 为y绝对坐标
        int[] location = new int[2];
        //获取通知栏高度  重要的在这，获取到通知栏高度
        int notificationBar = Resources.getSystem().getDimensionPixelSize(Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android"));
        //获取控件 view 的绝对坐标,( y 轴坐标是控件上部到屏幕最顶部（不包括控件本身）)
        //获取在当前窗体内的绝对坐标
        view.getLocationInWindow(location);
        //获取在整个屏幕内的绝对坐标
        view.getLocationOnScreen(location);
        //对dialog设置y轴坐标
        if (gravity == Gravity.TOP) {
            //Dialog上边与view上边对齐
            wlp.gravity = Gravity.TOP;
            wlp.y = location[1] - notificationBar;
        } else if (gravity == Gravity.BOTTOM) {
            //Dialog上边与view下边对齐
            wlp.gravity = Gravity.TOP;
            wlp.y = location[1] + view.getHeight() - notificationBar;
        } else if (gravity == Gravity.LEFT) {
            //Dialog左边与view左边对齐
            wlp.gravity = Gravity.TOP | Gravity.START;
            wlp.y = location[1] + view.getHeight() - notificationBar;
            wlp.x = location[0];
        } else if (gravity == Gravity.RIGHT) {
            //Dialog左边与view右边对齐
            wlp.gravity = Gravity.TOP | Gravity.START;
            wlp.y = location[1] + view.getHeight() - notificationBar;
            wlp.x = location[0] + view.getWidth();
        }
        //如果有坐标设置，更新坐标
        if (gravity == Gravity.TOP || gravity == Gravity.BOTTOM || gravity == Gravity.LEFT || gravity == Gravity.RIGHT) {
            if (xOff != 0) {
                //统一对齐方式
                wlp.gravity = Gravity.TOP | Gravity.START;
                wlp.x = location[0] + xOff;
                if (gravity == Gravity.RIGHT) {
                    wlp.x += view.getWidth();
                }
            }
            if (yOff != 0) {
                wlp.y += yOff;
            }
        }
        return wlp;
    }

    /**
     * 初始化事件
     *
     * @param decorView
     */
    private void initEvent(View decorView) {
        Log.i(TAG, "initEvent");
        //setOnCancelListener(dialog -> dismissDialog());
        setOnCancelListener(dialogInterface -> {
            if (mBean.isAutoDismiss()) {
                dismissDialog();
            } else {
                hideDialog();
            }
            if (cancelListener != null) {
                cancelListener.afterCancel(NORMAL_CANCEL);
            }
        });
        setOnKeyListener((OnKeyListener) (dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (mBean.isCancelable()) {
                    if (mBean.isAutoDismiss()) {
                        dismissDialog();
                    } else {
                        hideDialog();
                    }
                    if (cancelListener != null) {
                        cancelListener.afterCancel(BACK_CANCEL);
                    }
                    return true;
                }
            }
            return false;
        });
        //是否设置移动事件
        if (mBean.isDraggable()) {
            if (decorView != null) {
                setDrag(decorView, layoutParams);
            } else {
                Toast.makeText(mContext, "decorView == null", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 设置移动
     *
     * @param view
     * @param layoutParams
     */
    private void setDrag(View view, WindowManager.LayoutParams layoutParams) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
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
                        if (mBean.getGravity() == Gravity.BOTTOM) {
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
                    default:
                        break;
                }
                return isDrag; //把事件往下放
            }
        });
    }

    /**
     * 更新view的布局
     *
     * @param view         view
     * @param layoutParams layoutParams
     */
    private void updateView(View view, WindowManager.LayoutParams layoutParams) {
        if (view != null && layoutParams != null) {
            wm.updateViewLayout(view, layoutParams);
        }
    }

    /**
     * 是否在View的区域内部
     *
     * @param clickX 点击的 x 坐标
     * @param clickY 点击的 y 坐标
     * @param view   view
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


    /**
     * 处理 mAnim 对象
     *
     * @param type 0：取消；1：开始；2：暂停
     */
    private void handleAnim(int type) {
        if (mAnims != null) {
            for (int i = 0; i < mAnims.size(); i++) {
                switch (type) {
                    case ANIM_CANCEL:
                        mAnims.valueAt(i).cancel();
                        break;
                    case ANIM_START:
                        mAnims.valueAt(i).start();
                        break;
                    case ANIM_PAUSE:
                        mAnims.valueAt(i).pause();
                        break;
                    case ANIM_RESUME:
                        mAnims.valueAt(i).resume();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * 字符串是否为空
     *
     * @param str
     * @return
     */
    private boolean isEmptyOrNull(String str) {
        if (null == str || "".equals(str) || " ".equals(str) || "null".equals(str)) {
            return true;
        }
        return false;
    }


    /**
     * 显示Dialog
     */
    public void showDialog() {
        if (this != null && !isShowing()) {
            handleAnim(1);
            Log.i(TAG, "show");
            show();
        }
    }


    /**
     * 隐藏Dialog
     */
    public void hideDialog() {
        if (this != null) {
            handleAnim(2);
            Log.i(TAG, "hide");
            hide();
        }
    }


    /**
     * 销毁Dialog
     */
    public void dismissDialog() {
        if (this != null) {
            handleAnim(0);
            Log.i(TAG, "dismiss");
            dismiss();
        }
    }

    /**
     * 提供给外部访问View的方法
     *
     * @param viewId viewId
     * @param <T>    View类型
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

    /**
     * 设置Dialog中某个控件的动画效果
     * 最好在showDialog之前设置setAnimView
     *
     * @param viewId viewid
     * @param model  动画
     * @return
     */
    public ObjectAnimator setAnimView(int viewId, final AnimModel model) {
        View view = getView(viewId);
        String name = model.getPropertyName();
        if (view == null || isEmptyOrNull(name)) {
            return new ObjectAnimator();
        }
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, name, model.getStartValues(), model.getEndValues());
        //动画时长
        animator.setDuration(model.getDuration());
        //重复模式与重复次数缺一不可
        animator.setRepeatMode(model.getRepeatMode());
        animator.setRepeatCount(model.getRepeatCount());
        //设置 Interpolator 插值器
        animator.setInterpolator(model.getInterpolator());
        //缓存
        mAnims.put(viewId, animator);
        if (isShowing()) {
            animator.start();
        }
        return animator;
    }

    /**
     * 获取View动画的方法
     *
     * @param viewId
     * @return
     */
    public ObjectAnimator getAnimator(int viewId) {
        ObjectAnimator animator = mAnims.get(viewId);
        return animator;
    }

    /**
     * 设置某个控件的动画效果状态
     *
     * @param status  动画效果状态
     * @param viewIds 控件id
     */
    public void setAnimatorStatus(int status, int... viewIds) {
        if (viewIds == null) {
            return;
        }
        for (int viewId : viewIds) {
            ObjectAnimator animator = getAnimator(viewId);
            if (animator != null) {
                switch (status) {
                    case ANIM_CANCEL:
                        animator.cancel();
                        break;
                    case ANIM_START:
                        animator.start();
                        break;
                    case ANIM_PAUSE:
                        animator.pause();
                        break;
                    case ANIM_RESUME:
                        animator.resume();
                        break;
                    default:
                        break;
                }
            }
        }
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
        if (views == null) {
            return;
        }
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
        if (viewIds == null) {
            return;
        }
        for (int viewId : viewIds) {
            getView(viewId).setVisibility(isGone ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * 设置控件的点击事件
     *
     * @param viewId
     * @param listener
     */
    public void setClick(final int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        if (view == null || listener == null) {
            return;
        }
        view.setOnClickListener(listener);
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
        if (view == null || listener == null) {
            return;
        }
        final boolean[] state = {false};
        view.setOnClickListener(v -> {
            state[0] = !state[0];
            listener.click(v, state[0]);
        });
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
        if (text == null) {
            return;
        }
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
        if (mContext == null) {
            return;
        }
        String string = mContext.getResources().getString(stringId);
        setText(viewId, string);
    }

    /**
     * 设置背景颜色
     *
     * @param viewId
     * @param color
     */
    public void setBackgroundColor(final int viewId, int color) {
        if (mContext == null) {
            return;
        }
        View view = getView(viewId);
        view.setBackgroundColor(mContext.getResources().getColor(color));
    }

    /**
     * 设置背景颜色
     *
     * @param viewId
     * @param colorStr
     */
    public void setBackgroundColor(final int viewId, String colorStr) {
        if (colorStr == null || TextUtils.isEmpty(colorStr)) {
            return;
        }
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
        if (mContext == null) {
            return;
        }
        TextView textView = getView(viewId);
        textView.setTextColor(mContext.getResources().getColor(color));
    }

    /**
     * 设置TextView文本颜色
     *
     * @param viewId
     * @param colorStr
     */
    public void setTextColor(final int viewId, String colorStr) {
        if (colorStr == null || TextUtils.isEmpty(colorStr)) {
            return;
        }
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
        if (mContext == null) {
            return;
        }
        View view = getView(viewId);
        Drawable drawable = mContext.getResources().getDrawable(shape);
        if (drawable != null) {
            view.setBackground(drawable);
        }
    }
}
