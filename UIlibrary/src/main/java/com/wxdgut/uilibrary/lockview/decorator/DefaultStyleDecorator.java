package com.wxdgut.uilibrary.lockview.decorator;


/**
 * 默认样式装饰器
 */
public class DefaultStyleDecorator {
    private int normalColor;
    private int normalInnerColor;
    private float innerPercent;
    private float innerHitPercent;
    private int fillColor;
    private int clickColor;
    private int errorColor;
    private float lineWidth;

    public DefaultStyleDecorator(int normalColor, int normalInnerColor, float innerPercent,  float innerHitPercent, int fillColor, int hitColor, int errorColor, float lineWidth) {
        this.normalColor = normalColor;
        this.normalInnerColor = normalInnerColor;
        this.innerPercent = innerPercent;
        this.innerHitPercent = innerHitPercent;
        this.fillColor = fillColor;
        this.clickColor = hitColor;
        this.errorColor = errorColor;
        this.lineWidth = lineWidth;
    }

    public int getNormalColor() {
        return normalColor;
    }

    public void setNormalColor(int normalColor) {
        this.normalColor = normalColor;
    }

    public int getNormalInnerColor() {
        return normalInnerColor;
    }

    public void setNormalInnerColor(int normalInnerColor) {
        this.normalInnerColor = normalInnerColor;
    }

    public float getInnerPercent() {
        return innerPercent;
    }

    public void setInnerPercent(float innerPercent) {
        this.innerPercent = innerPercent;
    }

    public float getInnerHitPercent() {
        return innerHitPercent;
    }

    public void setInnerHitPercent(float innerHitPercent) {
        this.innerHitPercent = innerHitPercent;
    }

    public int getFillColor() {
        return fillColor;
    }

    public void setFillColor(int fillColor) {
        this.fillColor = fillColor;
    }

    public int getClickColor() {
        return clickColor;
    }

    public void setClickColor(int clickColor) {
        this.clickColor = clickColor;
    }

    public int getErrorColor() {
        return errorColor;
    }

    public void setErrorColor(int errorColor) {
        this.errorColor = errorColor;
    }

    public float getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
    }
}
