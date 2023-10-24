package com.wxdgut.uilibrary.popup;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.support.v4.widget.PopupWindowCompat;
import android.transition.Transition;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.wxdgut.uilibrary.R;
import com.wxdgut.uilibrary.utils.CommonUtils;

public class CommonPopup implements PopupWindow.OnDismissListener {
    private static final String TAG = "CommonPopup";

    private PopupWindow mPopupWindow;
    private View mContentView;
    private Context mContext;
    private int mLayoutId;

    //弹出pop时，背景是否变暗
    private boolean isBackgroundDim;
    //背景变暗的view
    @NonNull
    private ViewGroup mDimView;
    //背景变暗时透明度
    private float mDimValue = 0.7f;
    //背景变暗颜色
    @ColorInt
    private int mDimColor = Color.BLACK;

    //宽高
    private int mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
    private int mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
    //是否重新测量宽高
    private boolean isNeedReMeasureWH = false;
    //真实的宽高是否已经准备好
    private boolean isRealWHAlready = false;
    private OnRealWHAlreadyListener mOnRealWHAlreadyListener;

    //PopupWindow是否显示在window中.用于获取准确的PopupWindow宽高，可以重新设置偏移量
    public interface OnRealWHAlreadyListener {
        //在 show方法之后 updateLocation之前执行。popWidth  PopupWindow准确的宽；popHeight PopupWindow准确的高；anchorW   锚点View宽；anchorH   锚点View高
        void onRealWHAlready(CommonPopup commonPopup, int popWidth, int popHeight, int anchorW, int anchorH);
    }

    private View mAnchorView;
    private boolean isAtAnchorViewMethod = false;
    @YGravity
    private int mYGravity = YGravity.BELOW;
    @XGravity
    private int mXGravity = XGravity.LEFT;
    private int mOffsetX;
    private int mOffsetY;

    //PopupWindow.INPUT_METHOD_FROM_FOCUSABLE 当 PopupWindow 可以获得焦点时，输入法会自动弹出。意味着只有在用户点击或触摸到 PopupWindow 时，输入法才会显示。
    //PopupWindow.INPUT_METHOD_NEEDED 需要与输入法一起工作，也就是说，当 PopupWindow 显示时，输入法会自动弹出。无论用户是否与 PopupWindow 交互，输入法都会显示。
    private int mInputMethodMode = PopupWindow.INPUT_METHOD_FROM_FOCUSABLE;
    //WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED 与 PopupWindow 一起显示时，软键盘的状态不会改变。如果软键盘当前已显示，它将保持显示状态；如果未显示，则保持隐藏状态。
    //WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE 当软键盘弹出时，系统会自动调整窗口的大小，以确保输入框可见。通常用于确保输入框不会被软键盘遮挡
    private int mSoftInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED;

    private int mAnimationStyle;

    private boolean mFocusAndOutsideEnable = true;
    //获取焦点
    private boolean mFocusable = true;
    //是否触摸之外dismiss
    private boolean mOutsideTouchable = true;

    private Transition mEnterTransition;
    private Transition mExitTransition;

    private CancelListener cancelListener = null;
    public interface CancelListener {
        void afterCancel(int cancelType);
    }

    private float mLastTouchX, mLastTouchY;

    // ----------------- set方法  -----------------
    public CommonPopup setCancelListener(CancelListener listener) {
        if (listener != null) {
            cancelListener = listener;
        }
        return this;
    }

    public CommonPopup setOnRealWHAlreadyListener(OnRealWHAlreadyListener listener) {
        this.mOnRealWHAlreadyListener = listener;
        return this;
    }

    public CommonPopup setContext(Context context) {
        this.mContext = context;
        return this;
    }

    public CommonPopup setContentView(View contentView) {
        this.mContentView = contentView;
        this.mLayoutId = 0;
        return this;
    }

    public CommonPopup setContentView(@LayoutRes int layoutId) {
        this.mContentView = null;
        this.mLayoutId = layoutId;
        return this;
    }

    public CommonPopup setContentView(Context context, @LayoutRes int layoutId) {
        this.mContext = context;
        this.mContentView = null;
        this.mLayoutId = layoutId;
        return this;
    }

    public CommonPopup setContentView(View contentView, int width, int height) {
        this.mContentView = contentView;
        this.mLayoutId = 0;
        this.mWidth = width;
        this.mHeight = height;
        return this;
    }

    public CommonPopup setContentView(@LayoutRes int layoutId, int width, int height) {
        this.mContentView = null;
        this.mLayoutId = layoutId;
        this.mWidth = width;
        this.mHeight = height;
        return this;
    }

    public CommonPopup setContentView(Context context, @LayoutRes int layoutId, int width, int height) {
        this.mContext = context;
        this.mContentView = null;
        this.mLayoutId = layoutId;
        this.mWidth = width;
        this.mHeight = height;
        return this;
    }

    public CommonPopup setContentView(Context context, @LayoutRes int layoutId, View contentView, int width, int height) {
        this.mContext = context;
        this.mContentView = contentView;
        this.mLayoutId = layoutId;
        this.mWidth = width;
        this.mHeight = height;
        return this;
    }

    public CommonPopup setWidth(int width) {
        this.mWidth = width;
        return this;
    }

    public CommonPopup setHeight(int height) {
        this.mHeight = height;
        return this;
    }

    public CommonPopup setAnchorView(View view) {
        this.mAnchorView = view;
        return this;
    }

    public CommonPopup setYGravity(@YGravity int yGravity) {
        this.mYGravity = yGravity;
        return this;
    }

    public CommonPopup setXGravity(@XGravity int xGravity) {
        this.mXGravity = xGravity;
        return this;
    }

    public CommonPopup setOffsetX(int offsetX) {
        this.mOffsetX = offsetX;
        return this;
    }

    public CommonPopup setOffsetY(int offsetY) {
        this.mOffsetY = offsetY;
        return this;
    }

    public CommonPopup setAnimationStyle(@StyleRes int animationStyle) {
        this.mAnimationStyle = animationStyle;
        return this;
    }

    public CommonPopup setFocusable(boolean focusable) {
        this.mFocusable = focusable;
        return this;
    }

    public CommonPopup setOutsideTouchable(boolean outsideTouchable) {
        this.mOutsideTouchable = outsideTouchable;
        return this;
    }

    public CommonPopup setFocusAndOutsideEnable(boolean focusAndOutsideEnable) {
        this.mFocusAndOutsideEnable = focusAndOutsideEnable;
        return this;
    }

    //背景变暗支持api>=18
    public CommonPopup setBackgroundDimEnable(boolean isDim) {
        this.isBackgroundDim = isDim;
        return this;
    }

    public CommonPopup setDimView(@NonNull ViewGroup dimView) {
        this.mDimView = dimView;
        return this;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public CommonPopup setEnterTransition(Transition enterTransition) {
        this.mEnterTransition = enterTransition;
        return this;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public CommonPopup setExitTransition(Transition exitTransition) {
        this.mExitTransition = exitTransition;
        return this;
    }

    public CommonPopup setInputMethodMode(int mode) {
        this.mInputMethodMode = mode;
        mPopupWindow.setInputMethodMode(mInputMethodMode);
        return this;
    }

    public CommonPopup setSoftInputMode(int mode) {
        this.mSoftInputMode = mode;
        mPopupWindow.setSoftInputMode(mSoftInputMode);
        return this;
    }

    //是否需要重新获取宽高
    public CommonPopup setNeedReMeasureWH(boolean needReMeasureWH) {
        this.isNeedReMeasureWH = needReMeasureWH;
        return this;
    }

    public CommonPopup setDimValue(@FloatRange(from = 0.0f, to = 1.0f) float dimValue) {
        this.mDimValue = dimValue;
        return this;
    }

    public CommonPopup setDimColor(@ColorInt int color) {
        this.mDimColor = color;
        return this;
    }


    // ----------------- get方法  -----------------

    public PopupWindow getPopupWindow() {
        return mPopupWindow;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    //获取纵向Gravity
    public int getXGravity() {
        return mXGravity;
    }

    //获取横向Gravity
    public int getYGravity() {
        return mYGravity;
    }

    //获取x轴方向的偏移
    public int getOffsetX() {
        return mOffsetX;
    }

    //获取y轴方向的偏移
    public int getOffsetY() {
        return mOffsetY;
    }

    //是否精准的宽高获取完成
    public boolean isRealWHAlready() {
        return isRealWHAlready;
    }


    // ----------------- 其他方法  -----------------

    @Override
    public void onDismiss() {
        handleDismiss();
    }

    //PopupWindow消失后处理一些逻辑
    private void handleDismiss() {
        //清除背景变暗
        clearBackgroundDim();
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
        if (cancelListener != null) {
            cancelListener.afterCancel(1);
        }
    }

    //清除背景变暗
    private void clearBackgroundDim() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            if (isBackgroundDim) {
                if (mDimView != null) {
                    clearDim(mDimView);
                } else {
                    if (getContentView() != null) {
                        Activity activity = (Activity) getContentView().getContext();
                        if (activity != null) {
                            clearDim(activity);
                        }
                    }
                }
            }
        }
    }

    public View getContentView() {
        if (mPopupWindow != null) {
            return mPopupWindow.getContentView();
        } else {
            return null;
        }
    }

    public <T extends View> T findViewById(@IdRes int viewId) {
        View view = null;
        if (getContentView() != null) {
            view = getContentView().findViewById(viewId);
        }
        return (T) view;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void clearDim(ViewGroup dimView) {
        ViewGroupOverlay overlay = dimView.getOverlay();
        overlay.clear();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void clearDim(Activity activity) {
        ViewGroup parent = (ViewGroup) activity.getWindow().getDecorView().getRootView();
        //activity跟布局
        //ViewGroup parent = (ViewGroup) parent1.getChildAt(0);
        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.clear();
    }

    //是否正在显示
    public boolean isShowing() {
        return mPopupWindow != null && mPopupWindow.isShowing();
    }

    //消失
    public void dismiss() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
    }

    public static CommonPopup newInstance() {
        return new CommonPopup();
    }

    public CommonPopup apply() {
        if (mPopupWindow == null) {
            mPopupWindow = new PopupWindow();
            mPopupWindow.setOnDismissListener(this);
        }
        initContentViewAndWH();

        if (mAnimationStyle != 0) {
            mPopupWindow.setAnimationStyle(mAnimationStyle);
        }

        initFocusAndBack();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mEnterTransition != null) {
                mPopupWindow.setEnterTransition(mEnterTransition);
            }

            if (mExitTransition != null) {
                mPopupWindow.setExitTransition(mExitTransition);
            }
        }

        return this;
    }

    private void initContentViewAndWH() {
        if (mContentView == null) {
            if (mLayoutId != 0 && mContext != null) {
                mContentView = LayoutInflater.from(mContext).inflate(mLayoutId, null);
            } else {
                throw new IllegalArgumentException("The content view is null,the layoutId=" + mLayoutId + ",context=" + mContext);
            }
        }
        mPopupWindow.setContentView(mContentView);

        if (mWidth > 0 || mWidth == ViewGroup.LayoutParams.WRAP_CONTENT || mWidth == ViewGroup.LayoutParams.MATCH_PARENT) {
            mPopupWindow.setWidth(mWidth);
        } else {
            mPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        if (mHeight > 0 || mHeight == ViewGroup.LayoutParams.WRAP_CONTENT || mHeight == ViewGroup.LayoutParams.MATCH_PARENT) {
            mPopupWindow.setHeight(mHeight);
        } else {
            mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        //测量contentView大小 ,可能不准
        measureContentView();
        //获取contentView的精准大小
        registerOnGlobalLayoutListener();

        mPopupWindow.setInputMethodMode(mInputMethodMode);
        mPopupWindow.setSoftInputMode(mSoftInputMode);
    }

    /**
     * 是否需要测量 contentView的大小
     * 如果需要重新测量并为宽高赋值
     * 注：此方法获取的宽高可能不准确 MATCH_PARENT时无法获取准确的宽高
     */
    private void measureContentView() {
        final View contentView = getContentView();
        if (mWidth <= 0 || mHeight <= 0) {
            //测量大小
            contentView.measure(0, View.MeasureSpec.UNSPECIFIED);
            if (mWidth <= 0) {
                mWidth = contentView.getMeasuredWidth();
            }
            if (mHeight <= 0) {
                mHeight = contentView.getMeasuredHeight();
            }
        }
    }

    //注册GlobalLayoutListener 获取精准的宽高
    private void registerOnGlobalLayoutListener() {
        getContentView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getContentView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mWidth = getContentView().getWidth();
                mHeight = getContentView().getHeight();

                isRealWHAlready = true;
                isNeedReMeasureWH = false;

                if (mOnRealWHAlreadyListener != null) {
                    mOnRealWHAlreadyListener.onRealWHAlready(CommonPopup.this, mWidth, mHeight,
                            mAnchorView == null ? 0 : mAnchorView.getWidth(), mAnchorView == null ? 0 : mAnchorView.getHeight());
                }
                //Log.d(TAG, "onGlobalLayout finished. isShowing=" + isShowing());
                if (isShowing() && isAtAnchorViewMethod) {
                    updateLocation(mWidth, mHeight, mAnchorView, mYGravity, mXGravity, mOffsetX, mOffsetY);
                }
            }
        });
    }

    //更新 PopupWindow 到精准的位置
    private void updateLocation(int width, int height, @NonNull View anchor, @YGravity final int yGravity, @XGravity int xGravity, int x, int y) {
        if (mPopupWindow == null) {
            return;
        }
        x = calculateX(anchor, xGravity, width, x);
        y = calculateY(anchor, yGravity, height, y);
        mPopupWindow.update(anchor, x, y, width, height);
    }

    //根据水平gravity计算x偏移
    private int calculateX(View anchor, int horizGravity, int measuredW, int x) {
        switch (horizGravity) {
            case XGravity.LEFT:
                //anchor view左侧
                x -= measuredW;
                break;
            case XGravity.ALIGN_RIGHT:
                //与anchor view右边对齐
                x -= measuredW - anchor.getWidth();
                break;
            case XGravity.CENTER:
                //anchor view水平居中
                x += anchor.getWidth() / 2 - measuredW / 2;
                break;
            case XGravity.ALIGN_LEFT:
                //与anchor view左边对齐
                // Default position.
                break;
            case XGravity.RIGHT:
                //anchor view右侧
                x += anchor.getWidth();
                break;
        }
        return x;
    }

    //根据垂直gravity计算y偏移
    private int calculateY(View anchor, int vertGravity, int measuredH, int y) {
        switch (vertGravity) {
            case YGravity.ABOVE:
                //anchor view之上
                y -= measuredH + anchor.getHeight();
                break;
            case YGravity.ALIGN_BOTTOM:
                //anchor view底部对齐
                y -= measuredH;
                break;
            case YGravity.CENTER:
                //anchor view垂直居中
                y -= anchor.getHeight() / 2 + measuredH / 2;
                break;
            case YGravity.ALIGN_TOP:
                //anchor view顶部对齐
                y -= anchor.getHeight();
                break;
            case YGravity.BELOW:
                //anchor view之下
                // Default position.
                break;
        }
        return y;
    }

    private void initFocusAndBack() {
        if (!mFocusAndOutsideEnable) {
            mPopupWindow.setFocusable(true);
            mPopupWindow.setOutsideTouchable(false);
            mPopupWindow.setBackgroundDrawable(null);
            //注意下面这三个是contentView 不是PopupWindow，响应返回按钮事件
            mPopupWindow.getContentView().setFocusable(true);
            mPopupWindow.getContentView().setFocusableInTouchMode(true);
            mPopupWindow.getContentView().setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        mPopupWindow.dismiss();

                        return true;
                    }
                    return false;
                }
            });
            //在Android 6.0以上 ，只能通过拦截事件来解决
            mPopupWindow.setTouchInterceptor(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    final int x = (int) event.getX();
                    final int y = (int) event.getY();

                    if ((event.getAction() == MotionEvent.ACTION_DOWN)
                            && ((x < 0) || (x >= mWidth) || (y < 0) || (y >= mHeight))) {
                        //outside
                        //Log.d(TAG, "onTouch outside:mWidth=" + mWidth + ",mHeight=" + mHeight);
                        return true;
                    } else if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                        //outside
                        //Log.d(TAG, "onTouch outside event:mWidth=" + mWidth + ",mHeight=" + mHeight);
                        return true;
                    }
                    return false;
                }
            });
            //Log.e(TAG, "initFocusAndBack: 111111111111111111111111" + mOutsideTouchable);
        } else {
            mPopupWindow.setFocusable(mFocusable);
            mPopupWindow.setOutsideTouchable(mOutsideTouchable);
            //Log.e(TAG, "initFocusAndBack: 222222222222222222222" + mOutsideTouchable);
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    //使用此方法需要在创建的时候调用setAnchorView()等属性设置{@see setAnchorView()}
    public void showAsDropDown() {
        if (mAnchorView == null) {
            return;
        }
        showAsDropDown(mAnchorView, mOffsetX, mOffsetY);
    }


    public void showAsDropDown(View anchor) {
        //防止忘记调用 apply() 方法
        checkIsApply(false);

        handleBackgroundDim();
        mAnchorView = anchor;
        //是否重新获取宽高
        if (isNeedReMeasureWH) {
            registerOnGlobalLayoutListener();
        }
        mPopupWindow.showAsDropDown(anchor);
    }

    //PopupWindow自带的显示方法
    public void showAsDropDown(View anchor, int offsetX, int offsetY) {
        //防止忘记调用 apply() 方法
        checkIsApply(false);

        handleBackgroundDim();
        mAnchorView = anchor;
        mOffsetX = offsetX;
        mOffsetY = offsetY;
        //是否重新获取宽高
        if (isNeedReMeasureWH) {
            registerOnGlobalLayoutListener();
        }
        mPopupWindow.showAsDropDown(anchor, mOffsetX, mOffsetY);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void showAsDropDown(View anchor, int offsetX, int offsetY, int gravity) {
        //防止忘记调用 apply() 方法
        checkIsApply(false);

        handleBackgroundDim();
        mAnchorView = anchor;
        mOffsetX = offsetX;
        mOffsetY = offsetY;
        //是否重新获取宽高
        if (isNeedReMeasureWH) {
            registerOnGlobalLayoutListener();
        }
        PopupWindowCompat.showAsDropDown(mPopupWindow, anchor, mOffsetX, mOffsetY, gravity);
    }

    //检查是否调用了 build() 方法。isAtAnchorView 是否是 showAt
    private void checkIsApply(boolean isAtAnchorView) {
        if (this.isAtAnchorViewMethod != isAtAnchorView) {
            this.isAtAnchorViewMethod = isAtAnchorView;
        }
        if (mPopupWindow == null) {
            apply();
        }
    }

    private void handleBackgroundDim() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            if (!isBackgroundDim) {
                return;
            }
            if (mDimView != null) {
                applyDim(mDimView);
            } else {
                if (getContentView() != null && getContentView().getContext() != null &&
                        getContentView().getContext() instanceof Activity) {
                    Activity activity = (Activity) getContentView().getContext();
                    applyDim(activity);
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void applyDim(Activity activity) {
        ViewGroup parent = (ViewGroup) activity.getWindow().getDecorView().getRootView();
        //activity跟布局
        //ViewGroup parent = (ViewGroup) parent1.getChildAt(0);
        Drawable dimDrawable = new ColorDrawable(mDimColor);
        dimDrawable.setBounds(0, 0, parent.getWidth(), parent.getHeight());
        dimDrawable.setAlpha((int) (255 * mDimValue));
        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.add(dimDrawable);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void applyDim(ViewGroup dimView) {
        Drawable dimDrawable = new ColorDrawable(mDimColor);
        dimDrawable.setBounds(0, 0, dimView.getWidth(), dimView.getHeight());
        dimDrawable.setAlpha((int) (255 * mDimValue));
        ViewGroupOverlay overlay = dimView.getOverlay();
        overlay.add(dimDrawable);
    }

    public void showAtLocation(View parent, int gravity, int offsetX, int offsetY) {
        //防止忘记调用 apply() 方法
        checkIsApply(false);
        handleBackgroundDim();
        mAnchorView = parent;
        mOffsetX = offsetX;
        mOffsetY = offsetY;
        //是否重新获取宽高
        if (isNeedReMeasureWH) {
            registerOnGlobalLayoutListener();
        }
        mPopupWindow.showAtLocation(parent, gravity, mOffsetX, mOffsetY);
    }


    //相对anchor view显示
    public void showAtAnchorView() {
        if (mAnchorView == null) {
            return;
        }
        showAtAnchorView(mAnchorView, mYGravity, mXGravity);
    }

    public void showAtAnchorViewAbove(View anchor) {
        showAtAnchorView(anchor, YGravity.ABOVE, XGravity.CENTER);
    }

    public void showAtAnchorViewBelow(View anchor) {
        showAtAnchorView(anchor, YGravity.BELOW, XGravity.CENTER);
    }

    public void showAtAnchorViewLeft(View anchor) {
        showAtAnchorView(anchor, YGravity.CENTER, XGravity.LEFT);
    }

    public void showAtAnchorViewRight(View anchor) {
        showAtAnchorView(anchor, YGravity.CENTER, XGravity.RIGHT);
    }

    /**
     * 相对anchor view显示，适用 宽高不为match_parent
     * <p>
     * 注意：如果使用 VerticalGravity 和 HorizontalGravity 时，请确保使用之后 PopupWindow 没有超出屏幕边界，
     * 如果超出屏幕边界，VerticalGravity 和 HorizontalGravity 可能无效，从而达不到你想要的效果。     *
     *
     * @param anchor
     * @param vertGravity
     * @param horizGravity
     */
    public void showAtAnchorView(@NonNull View anchor, @YGravity int vertGravity, @XGravity int horizGravity) {
        showAtAnchorView(anchor, vertGravity, horizGravity, 0, 0);
    }

    /**
     * 相对anchor view显示，适用 宽高不为match_parent
     * <p>
     * 注意：如果使用 VerticalGravity 和 HorizontalGravity 时，请确保使用之后 PopupWindow 没有超出屏幕边界，
     * 如果超出屏幕边界，VerticalGravity 和 HorizontalGravity 可能无效，从而达不到你想要的效果。
     *
     * @param anchor
     * @param vertGravity  垂直方向的对齐方式
     * @param horizGravity 水平方向的对齐方式
     * @param x            水平方向的偏移
     * @param y            垂直方向的偏移
     */
    public void showAtAnchorView(@NonNull View anchor, @YGravity final int vertGravity, @XGravity int horizGravity, int x, int y) {
        //防止忘记调用 apply() 方法
        checkIsApply(true);

        mAnchorView = anchor;
        mOffsetX = x;
        mOffsetY = y;
        mYGravity = vertGravity;
        mXGravity = horizGravity;
        //处理背景变暗
        handleBackgroundDim();
        x = calculateX(anchor, horizGravity, mWidth, mOffsetX);
        y = calculateY(anchor, vertGravity, mHeight, mOffsetY);
        //是否重新获取宽高
        if (isNeedReMeasureWH) {
            registerOnGlobalLayoutListener();
        }
        //Log.i(TAG, "showAtAnchorView: w=" + measuredW + ",y=" + measuredH);
        PopupWindowCompat.showAsDropDown(mPopupWindow, anchor, x, y, Gravity.NO_GRAVITY);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void showEverywhereInView(Context context, View view) {
        view.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mLastTouchX = event.getRawX();
                mLastTouchY = event.getRawY();
            }
            return false;
        });

        view.setOnLongClickListener(v -> {
            showEverywhere(context, v, (int) mLastTouchX, (int) mLastTouchY);
            return true;
        });
    }

    //在View内任意地方显示
    public void showEverywhere(Context context, View view, int touchX, int touchY) {
        int screenHeight = CommonUtils.getScreenHeight(context);
        int screenWidth = CommonUtils.getScreenWidth(context);
        int offsetX = touchX;
        int offsetY = touchY;
        if (touchX < getWidth() && screenHeight - touchY < getHeight()) {
            //左下弹出动画
            getPopupWindow().setAnimationStyle(R.style.LeftBottomPopAnim);
            offsetY = touchY - getHeight();
        } else if (touchX + getWidth() > screenWidth && touchY + getHeight() > screenHeight) {
            //右下弹出动画
            getPopupWindow().setAnimationStyle(R.style.RightBottomPopAnim);
            offsetX = (touchX - getWidth());
            offsetY = touchY - getHeight();
        } else if (touchX + getWidth() > screenWidth) {
            getPopupWindow().setAnimationStyle(R.style.RightTopPopAnim);
            offsetX = (touchX - getWidth());
        } else {
            getPopupWindow().setAnimationStyle(R.style.LeftTopPopAnim);
        }
        showAtLocation(view, Gravity.NO_GRAVITY, offsetX, offsetY);
    }

}
