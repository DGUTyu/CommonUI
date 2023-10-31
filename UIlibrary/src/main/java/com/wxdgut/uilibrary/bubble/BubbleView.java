package com.wxdgut.uilibrary.bubble;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.wxdgut.uilibrary.utils.CommonUtils;

public class BubbleView extends View {
    //是否是调试模式；在xml布局里直接引用BubbleView控件时，可以把这个设置为true，看实际效果
    private boolean DEBUG = true;
    // 两个圆的圆形
    private PointF mFixationPoint, mDragPoint;
    // 画笔
    private Paint mPaint;
    // 拖拽圆的半径
    private int mDragRadius = 10;
    // 固定圆的最小半径
    private int mFixationRadiusMin = 3;
    // 固定圆的最大半径（初始半径，无固定要求，小于mDragRadius、大于mFixationRadiusMin即可）
    private int mFixationRadiusMax = mDragRadius - mFixationRadiusMin / 2;
    // 固定圆的当前半径
    private int mFixationRadius;
    //缩放系数，影响曲线长度。值越大，线越长
    private int SCALE_FACTOR = 15;
    private Bitmap mDragBitmap;
    //监听器
    private BubbleListener mListener;

    //设置拖拽Bitmap
    public void setDragBitmap(Bitmap bitmap) {
        this.mDragBitmap = bitmap;
    }

    //设置监听器
    public void setBubbleListener(BubbleListener listener) {
        this.mListener = listener;
    }

    //监听器
    public interface BubbleListener {
        // 复位
        void restore();

        // 爆炸
        void bomb(PointF pointF);
    }

    public BubbleView(Context context) {
        //super(context);
        this(context, null);
    }

    public BubbleView(Context context, @Nullable AttributeSet attrs) {
        //super(context, attrs);
        this(context, attrs, 0);
    }

    public BubbleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDragRadius = dpToPx(mDragRadius);
        mFixationRadiusMax = dpToPx(mFixationRadiusMax);
        mFixationRadiusMin = dpToPx(mFixationRadiusMin);
        initPaint();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (DEBUG) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // 手指按下要去指定当前的位置
                    float downX = event.getX();
                    float downY = event.getY();
                    initPoint(downX, downY);
                    break;
                case MotionEvent.ACTION_MOVE:
                    //更新位置
                    float moveX = event.getX();
                    float moveY = event.getY();
                    updateDragPoint(moveX, moveY);
                    break;
                default:
                    break;
            }
            invalidate();
            return true;
        } else {
            return super.onTouchEvent(event);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        //画两个圆
        if (mDragPoint == null || mFixationPoint == null) {
            return;
        }
        //画拖拽圆
        canvas.drawCircle(mDragPoint.x, mDragPoint.y, mDragRadius, mPaint);
        //画固定圆 有一个初始大小 而且它的半径随着距离的增大而减小，小到一定程度就不见了
        Path bezeierPath = getBezierPath();
        if (bezeierPath != null) {
            canvas.drawCircle(mFixationPoint.x, mFixationPoint.y, mFixationRadius, mPaint);
            // 画贝塞尔曲线
            canvas.drawPath(bezeierPath, mPaint);
        }
        // 画图片 位置也是手指移动的位置 , 中心位置才是手指拖动的位置
        if (mDragBitmap != null) {
            // 可以写一个渐变动画
            canvas.drawBitmap(mDragBitmap, mDragPoint.x - mDragBitmap.getWidth() / 2.0f, mDragPoint.y - mDragBitmap.getHeight() / 2.0f, null);
        }
    }

    //处理手指松开
    public void handleActionUp() {
        if (mFixationRadius > mFixationRadiusMin) {
            //回弹 ValueAnimator 值变化的动画 0变化到1
            ValueAnimator animator = ObjectAnimator.ofFloat(1);
            animator.setDuration(250);
            PointF start = new PointF(mDragPoint.x, mDragPoint.y);
            PointF end = new PointF(mFixationPoint.x, mFixationPoint.y);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    // percent 的值超出了 0 到 1 的范围是因为使用了动画的插值器
                    float percent = (float) animation.getAnimatedValue();
                    PointF pointF = CommonUtils.getPointBetweenTwoPoints(start, end, percent);
                    // 用代码更新拖拽点
                    updateDragPoint(pointF.x, pointF.y);
                }
            });
            // OvershootInterpolator会在动画结束时产生超过终点的效果，它会让动画在结束时“超调”一段距离，然后再回弹到终点位置。
            // 设置一个差值器 在结束的时候回弹。tension 张力值越大，动画超调的幅度就越大
            animator.setInterpolator(new OvershootInterpolator(3f));
            animator.start();
            // 还要通知 TouchListener 移除当前View 然后显示静态的 View
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (mListener != null) {
                        mListener.restore();
                    }
                }
            });
        } else {
            //爆炸
            if (mListener != null) {
                mListener.bomb(mDragPoint);
            }
        }
    }

    //初始化画笔
    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        //抗锯齿
        mPaint.setAntiAlias(true);
        //开启抖动处理
        mPaint.setDither(true);
    }

    //初始化位置
    public void initPoint(float downX, float downY) {
        mFixationPoint = new PointF(downX, downY);
        mDragPoint = new PointF(downX, downY);
        //invalidate();  //?
    }

    //dp转px
    public int dpToPx(int dp) {
        //(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getResources().getDisplayMetrics());
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    //更新拖拽点的位置
    public void updateDragPoint(float moveX, float moveY) {
        mDragPoint.x = moveX;
        mDragPoint.y = moveY;
        //重新绘制
        invalidate();
    }

    //获取贝塞尔路径
    public Path getBezierPath() {
        //计算两个点的距离
        double distance = getDistance(mDragPoint, mFixationPoint);
        mFixationRadius = (int) (mFixationRadiusMax - distance / SCALE_FACTOR);
        if (mFixationRadius < mFixationRadiusMin) {
            // 小于一定距离 贝塞尔和固定圆都不要画了
            return null;
        }
        Path bezeierPath = new Path();
        //求斜率
        float dy = mDragPoint.y - mFixationPoint.y;
        float dx = mDragPoint.x - mFixationPoint.x;
        float tanA = dy / dx;
        // 求角a
        double arcTanA = Math.atan(tanA);
        // p0
        float p0x = (float) (mFixationPoint.x + mFixationRadius * Math.sin(arcTanA));
        float p0y = (float) (mFixationPoint.y - mFixationRadius * Math.cos(arcTanA));
        // p1
        float p1x = (float) (mDragPoint.x + mDragRadius * Math.sin(arcTanA));
        float p1y = (float) (mDragPoint.y - mDragRadius * Math.cos(arcTanA));
        // p2
        float p2x = (float) (mDragPoint.x - mDragRadius * Math.sin(arcTanA));
        float p2y = (float) (mDragPoint.y + mDragRadius * Math.cos(arcTanA));
        // p3
        float p3x = (float) (mFixationPoint.x - mFixationRadius * Math.sin(arcTanA));
        float p3y = (float) (mFixationPoint.y + mFixationRadius * Math.cos(arcTanA));

        // 拼装 贝塞尔的曲线路径
        bezeierPath.moveTo(p0x, p0y); // 移动
        // 两个点
        PointF controlPoint = getControlPoint();
        // quadTo 添加二次贝塞尔曲线的方法。参数1,2：x1,y1：二次贝塞尔曲线的控制点的 x,y 坐标。参数3,4：：二次贝塞尔曲线的终点的 x,y 坐标。
        bezeierPath.quadTo(controlPoint.x, controlPoint.y, p1x, p1y);
        // 画第二条
        bezeierPath.lineTo(p2x, p2y);
        // 再次画曲线
        bezeierPath.quadTo(controlPoint.x, controlPoint.y, p3x, p3y);
        // 封闭路径
        bezeierPath.close();
        return bezeierPath;
    }

    //获取贝塞尔曲线控制点，这里以两个圆点连线的中心点
    public PointF getControlPoint() {
        return new PointF((mDragPoint.x + mFixationPoint.x) / 2, (mDragPoint.y + mFixationPoint.y) / 2);
    }

    //获取两个圆之间的距离
    public double getDistance(PointF point1, PointF point2) {
        float dx = point1.x - point2.x;
        float dy = point1.y - point2.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    //绑定视图
    public static void attach(View view, BubbleTouchListener.BubbleDismissListener listener) {
        BubbleTouchListener touchListener = new BubbleTouchListener(view, view.getContext(), listener);
        view.setOnTouchListener(touchListener);
    }
}
