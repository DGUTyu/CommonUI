package com.wxdgut.uilibrary.dialog;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.view.animation.LinearInterpolator;

/**
 * 动画属性
 */
public class AnimModel {
    //预设动画属性名，必须用final，否则用不了switch
    public static final int ROTATION = 1, ROTATION_X = 2, ROTATION_Y = 3, TRANSLATION_X = 4, TRANSLATION_Y = 5;

    //动画属性名 如 translationX，translationY，rotation，rotationX，rotationY
    public String propertyName;

    //开始浮动值
    public float startValues;

    //结束浮动值
    public float endValues;

    //动画时长 毫秒
    public long duration;

    //重复模式与重复次数缺一不可
    //设置重复模式 ValueAnimator.RESTART正序重新开始  ValueAnimator.REVERSE逆序重新开始
    public int repeatMode;

    //动画的循环次数 ValueAnimator.INFINITE代表无限循环
    public int repeatCount;

    //设置 Interpolator 插值器,线性Interpolator,以常量速率改变
    //Interpolator 其实就是速度设置器。你在参数里填入不同的 Interpolator ，动画就会以不同的速度模型来执行
    public TimeInterpolator interpolator;

    public AnimModel() {
    }

    /**
     * 取默认的动画设置
     *
     * @param nameType
     */
    public AnimModel(int nameType) {
        switch (nameType) {
            case ROTATION:
                this.propertyName = "rotation";
                this.startValues = 0f;
                this.endValues = 360f;
                this.duration = 2 * 1000L;
                this.repeatMode = ValueAnimator.RESTART;
                this.repeatCount = ValueAnimator.INFINITE;
                this.interpolator = new LinearInterpolator();
                break;
            default:
                break;
        }
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public float getStartValues() {
        return startValues;
    }

    public void setStartValues(float startValues) {
        this.startValues = startValues;
    }

    public float getEndValues() {
        return endValues;
    }

    public void setEndValues(float endValues) {
        this.endValues = endValues;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getRepeatMode() {
        return repeatMode;
    }

    public void setRepeatMode(int repeatMode) {
        this.repeatMode = repeatMode;
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
    }

    public TimeInterpolator getInterpolator() {
        return interpolator;
    }

    public void setInterpolator(TimeInterpolator interpolator) {
        this.interpolator = interpolator;
    }

}
