package com.wxdgut.uilibrary.like;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.wxdgut.uilibrary.utils.UIConfigUtils;

import java.util.Random;

public class LoveLayout extends RelativeLayout implements View.OnClickListener {
    //点击一次出现的爱心次数
    private int likePerClick = 1;
    //出场动画时长
    private final int BEGIN_DURATION = 500;
    //运动动画时长
    private final int MOVE_DURATION = 3000;
    // 随机数
    private Random mRandom;
    // 图片资源
    private int[] mImageRes;
    // 控件的宽高
    private int mWidth, mHeight;
    // 图片的宽高
    private int mDrawableWidth, mDrawableHeight;
    // 插值器
    private Interpolator[] mInterpolator;
    //点击事件监听器
    private OnClickListener mExternalClickListener;
    
    public LoveLayout(Context context) {
        this(context, null);
    }

    public LoveLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoveLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnClickListener(this);
        mRandom = new Random();
        mImageRes = UIConfigUtils.getDefaultLoveImgIds();

        Drawable drawable = ContextCompat.getDrawable(context, mImageRes[0]);
        mDrawableWidth = drawable.getIntrinsicWidth();
        mDrawableHeight = drawable.getIntrinsicHeight();

        mInterpolator = new Interpolator[]{
                new AccelerateDecelerateInterpolator(),
                new AccelerateInterpolator(),
                new DecelerateInterpolator(),
                new LinearInterpolator()};
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获取控件的宽高
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
    }

    // 设置外部的点击监听器
    public void setExternalClickListener(OnClickListener listener) {
        setExternalClickListener(listener, UIConfigUtils.getDefaultLovePerClick());
    }

    // 设置外部的点击监听器
    public void setExternalClickListener(OnClickListener listener, int likePerClick) {
        this.likePerClick = likePerClick;
        mExternalClickListener = listener;
    }

    //添加count个点赞的View
    public void addLove(int count) {
        for (int i = 0; i < count; i++) {
            addLove();
        }
    }

    //添加一个点赞的View
    public void addLove() {
        // 添加一个ImageView在底部
        final ImageView loveIv = new ImageView(getContext());
        // 给一个图片资源（随机）
        loveIv.setImageResource(mImageRes[mRandom.nextInt(mImageRes.length - 1)]);
        // 怎么添加到底部中心？ LayoutParams
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(ALIGN_PARENT_BOTTOM);
        params.addRule(CENTER_HORIZONTAL);
        loveIv.setLayoutParams(params);
        addView(loveIv);

        AnimatorSet animator = getAnimator(loveIv);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // 执行完毕之后移除
                removeView(loveIv);
            }
        });
        animator.start();
    }

    //获取开始动画
    private AnimatorSet getAnimator(ImageView iv) {
        AnimatorSet allAnimatorSet = new AnimatorSet();
        //添加的效果，有放大和透明度的变化（属性动画）
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(iv, "alpha", 0.3f, 1.0f);
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(iv, "scaleX", 0.3f, 1.0f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(iv, "scaleY", 0.3f, 1.0f);
        // 一起执行
        animatorSet.playTogether(alphaAnimator, scaleXAnimator, scaleYAnimator);
        animatorSet.setDuration(BEGIN_DURATION);

        // 运行的路径动画  playSequentially 按循序执行
        allAnimatorSet.playSequentially(animatorSet, getBezierAnimator(iv));
        return allAnimatorSet;
    }

    //获取贝塞尔路径动画
    private Animator getBezierAnimator(final ImageView iv) {
        // 怎么确定四个点
        PointF point0 = new PointF(mWidth / 2f - mDrawableWidth / 2f, mHeight - mDrawableHeight);
        // 确保 p2 点的 y 值 一定要大于 p1 点的 y 值
        PointF point1 = getPoint(1);
        PointF point2 = getPoint(2);
        PointF point3 = new PointF(mRandom.nextInt(mWidth) - mDrawableWidth, 0);

        LoveTypeEvaluator typeEvaluator = new LoveTypeEvaluator(point1, point2);
        // ofFloat  第一个参数 LoveTypeEvaluator 第二个参数 p0, 第三个是 p3（传入初始点）
        ValueAnimator bezierAnimator = ObjectAnimator.ofObject(typeEvaluator, point0, point3);
        // 加一些随机的插值器（效果更炫）
        bezierAnimator.setInterpolator(mInterpolator[mRandom.nextInt(mInterpolator.length - 1)]);
        bezierAnimator.setDuration(MOVE_DURATION);
        bezierAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF pointF = (PointF) animation.getAnimatedValue();
                iv.setX(pointF.x);
                iv.setY(pointF.y);
                //返回一个浮点数，范围在 0.0 到 1.0 之间，表示动画的完成度。0.0 表示动画刚开始；1.0 表示动画已经完成
                float t = animation.getAnimatedFraction();
                iv.setAlpha(1 - t + 0.2f);
            }
        });
        return bezierAnimator;
    }

    //获取第index个点
    private PointF getPoint(int index) {
        int x = mRandom.nextInt(mWidth) - mDrawableWidth;
        float y = mRandom.nextInt(mHeight / 2) + (index - 1) * (mHeight / 2f);
        return new PointF(x, y);
    }

    @Override
    public void onClick(View v) {
        addLove(likePerClick);
        if (mExternalClickListener != null) {
            mExternalClickListener.onClick(v);
        }
    }
}
