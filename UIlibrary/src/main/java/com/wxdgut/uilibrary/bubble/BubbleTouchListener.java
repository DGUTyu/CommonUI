package com.wxdgut.uilibrary.bubble;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.drawable.AnimationDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.wxdgut.uilibrary.utils.CommonUtils;
import com.wxdgut.uilibrary.utils.UIConfigUtils;


public class BubbleTouchListener implements View.OnTouchListener, BubbleView.BubbleListener {
    // 原来需要拖动爆炸的View
    private View mStaticView;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mParams;
    private Context mContext;
    private BubbleView mBubbleView;
    // 爆炸动画
    private FrameLayout mBombFrame;
    private ImageView mBombImage;
    //监听器
    private BubbleDismissListener mDismissListener;

    public interface BubbleDismissListener {
        //按需实现
        default void dismiss(View view){};
    }

    public BubbleTouchListener(View staticView, Context context, BubbleDismissListener listener) {
        this.mStaticView = staticView;
        this.mContext = context;
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mParams = new WindowManager.LayoutParams();
        // 背景要透明
        mParams.format = PixelFormat.TRANSPARENT;
        mBubbleView = new BubbleView(context);
        mBubbleView.setBubbleListener(this);
        //爆炸动画
        mBombFrame = new FrameLayout(mContext);
        mBombImage = new ImageView(mContext);
        mBombImage.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mBombFrame.addView(mBombImage);
        //监听器
        this.mDismissListener = listener;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 要在WindowManager上面画一个View
                mWindowManager.addView(mBubbleView, mParams);
                // 初始化贝塞尔View的点，保证固定圆的中心在View的中心
                int[] location = new int[2];
                mStaticView.getLocationOnScreen(location);
                mBubbleView.initPoint(location[0] + mStaticView.getWidth() / 2.0f, location[1] + mStaticView.getHeight() / 2.0f - CommonUtils.getStatusBarHeight(mContext));
                Bitmap bitmap = getBitmapByView(mStaticView);
                mBubbleView.setDragBitmap(bitmap);
                //把原来的View隐藏
                mStaticView.setVisibility(View.INVISIBLE);
                break;
            case MotionEvent.ACTION_MOVE:
                mBubbleView.updateDragPoint(event.getRawX(), event.getRawY() - CommonUtils.getStatusBarHeight(mContext));
                break;
            case MotionEvent.ACTION_UP:
                mBubbleView.handleActionUp();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void restore() {
        // 把消息的View移除
        mWindowManager.removeView(mBubbleView);
        // 把原来的View显示
        mStaticView.setVisibility(View.VISIBLE);
    }

    @Override
    public void bomb(PointF pointF) {
        // 要去执行爆炸动画 （帧动画）
        // 原来的View的View要移除
        mWindowManager.removeView(mBubbleView);
        // 要在 mWindowManager 添加一个爆炸动画
        mWindowManager.addView(mBombFrame, mParams);
        mBombImage.setBackgroundResource(UIConfigUtils.getDefaultBubbleBombDrawableId());

        AnimationDrawable drawable = (AnimationDrawable) mBombImage.getBackground();
        mBombImage.setX(pointF.x - drawable.getIntrinsicWidth() / 2.0f);
        mBombImage.setY(pointF.y - drawable.getIntrinsicHeight() / 2.0f);
        drawable.start();

        // 等它执行完之后要移除掉这个View 爆炸动画也就是 mBombFrame
        mBombImage.postDelayed(() -> {
            mWindowManager.removeView(mBombFrame);
            // 通知一下外面该消失
            if (mDismissListener != null) {
                mDismissListener.dismiss(mStaticView);
            }
        }, getAnimationDrawableTime(drawable));
    }

    private Bitmap getBitmapByView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    //获取动画
    private long getAnimationDrawableTime(AnimationDrawable drawable) {
        int numberOfFrames = drawable.getNumberOfFrames();
        long time = 0;
        for (int i = 0; i < numberOfFrames; i++) {
            time += drawable.getDuration(i);
        }
        return time;
    }
}



