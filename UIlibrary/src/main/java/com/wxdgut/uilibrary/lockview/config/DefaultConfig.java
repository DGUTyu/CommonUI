package com.wxdgut.uilibrary.lockview.config;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.TypedValue;


/**
 * 默认设置
 */
public class DefaultConfig {
    //用于获取Builder 参数
    final Context context; //上下文，有可能为null
    final String normalColor; // 默认正常颜色
    final String clickColor; // 默认点击颜色
    final String errorColor; // 默认错误颜色
    final String fillColor; // 默认填充颜色
    public final int lineWidth;  // 默认线宽度
    public final int defaultFreezeDuration; //ms
    public final boolean defaultEnableAutoClean;
    public final boolean defaultEnableHapticFeedback;
    public final boolean defaultEnableSkip;
    public final boolean defaultEnableLogger;

    private DefaultConfig(Builder builder) {
        //获取参数
        this.context = builder.context;
        this.normalColor = builder.normalColor;
        this.clickColor = builder.clickColor;
        this.errorColor = builder.errorColor;
        this.fillColor = builder.fillColor;
        this.lineWidth = builder.lineWidth;
        this.defaultFreezeDuration = builder.defaultFreezeDuration;
        this.defaultEnableAutoClean = builder.defaultEnableAutoClean;
        this.defaultEnableHapticFeedback = builder.defaultEnableHapticFeedback;
        this.defaultEnableSkip = builder.defaultEnableSkip;
        this.defaultEnableLogger = builder.defaultEnableLogger;
    }

    //创建Builder对象
    public static Builder newBuilder(Context context) {
        return new Builder(context);
    }

    //建造者模式
    public static class Builder {
        Context context;    //context有可能为空
        String normalColor; // 默认正常颜色
        String clickColor; // 默认点击颜色
        String errorColor; // 默认错误颜色
        String fillColor; // 默认填充颜色
        int lineWidth;  // 默认线宽度
        int defaultFreezeDuration; //ms
        boolean defaultEnableAutoClean;
        boolean defaultEnableHapticFeedback;
        boolean defaultEnableSkip;
        boolean defaultEnableLogger;

        /**
         * 默认配置
         *
         * @param context
         */
        public Builder(Context context) { //context有可能为空
            this.context = context;
            this.normalColor = "#2196F3";
            this.clickColor = "#3F51B5";
            this.errorColor = "#F44336";
            this.fillColor = "#FFFFFF";
            this.lineWidth = 1;
            this.defaultFreezeDuration = 1000; //ms
            this.defaultEnableAutoClean = true;
            this.defaultEnableHapticFeedback = false;
            this.defaultEnableSkip = false;
            this.defaultEnableLogger = false;
        }

        public Builder normalColor(String normalColor) {
            this.normalColor = normalColor;
            return this;
        }

        public Builder clickColor(String clickColor) {
            this.clickColor = clickColor;
            return this;
        }

        public Builder errorColor(String errorColor) {
            this.errorColor = errorColor;
            return this;
        }

        public Builder fillColor(String fillColor) {
            this.fillColor = fillColor;
            return this;
        }

        public Builder lineWidth(int lineWidth) {
            this.lineWidth = lineWidth;
            return this;
        }

        public Builder defaultFreezeDuration(int defaultFreezeDuration) {
            this.defaultFreezeDuration = defaultFreezeDuration;
            return this;
        }

        public Builder defaultEnableAutoClean(boolean defaultEnableAutoClean) {
            this.defaultEnableAutoClean = defaultEnableAutoClean;
            return this;
        }

        public Builder defaultEnableHapticFeedback(boolean defaultEnableHapticFeedback) {
            this.defaultEnableHapticFeedback = defaultEnableHapticFeedback;
            return this;
        }

        public Builder defaultEnableSkip(boolean defaultEnableSkip) {
            this.defaultEnableSkip = defaultEnableSkip;
            return this;
        }

        public Builder defaultEnableLogger(boolean defaultEnableLogger) {
            this.defaultEnableLogger = defaultEnableLogger;
            return this;
        }

        /**
         * @return
         */
        public DefaultConfig build() {
            return new DefaultConfig(this);
        }
    }

    public float getLineWidth() {
        if (context == null) return 1;
        return convertDpToPx(lineWidth, context.getResources());
    }

    private float convertDpToPx(float dpValue, Resources resources) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, resources.getDisplayMetrics());
    }

    public int getNormalColor() {
        return Color.parseColor(normalColor);
    }

    public int getClickColor() {
        return Color.parseColor(clickColor);
    }

    public int getErrorColor() {
        return Color.parseColor(errorColor);
    }

    public int getFillColor() {
        return Color.parseColor(fillColor);
    }

}