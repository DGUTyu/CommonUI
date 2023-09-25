package com.wxdgut.uilibrary.lockview.decorator;


import android.graphics.Bitmap;

/**
 * 默认样式装饰器
 */
public class DefaultStyleDecorator {
    private int rows;
    private int normalColor;
    private int normalInnerColor;
    private float innerPercent;
    private float innerHitPercent;
    private int fillColor;
    private int hitColor;
    private int errorColor;
    private float lineWidth;
    //扩展属性 设置图案在正常、点击、出错时的Bitmap对象
    private Bitmap normalBitmap;
    private Bitmap hitBitmap;
    private Bitmap errorBitmap;
    //扩展属性 设置连接线颜色、宽度（dp）
    private int linkColor;
    private int linkErrorColor;
    private int linkWidth;

    public DefaultStyleDecorator(int rows, int normalColor, int normalInnerColor, float innerPercent,  float innerHitPercent, int fillColor, int hitColor, int errorColor, float lineWidth) {
        this.rows = rows;
        this.normalColor = normalColor;
        this.normalInnerColor = normalInnerColor;
        this.innerPercent = innerPercent;
        this.innerHitPercent = innerHitPercent;
        this.fillColor = fillColor;
        this.hitColor = hitColor;
        this.errorColor = errorColor;
        this.lineWidth = lineWidth;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
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

    public int getHitColor() {
        return hitColor;
    }

    public void setHitColor(int hitColor) {
        this.hitColor = hitColor;
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

    public Bitmap getNormalBitmap() {
        return normalBitmap;
    }

    public void setNormalBitmap(Bitmap normalBitmap) {
        this.normalBitmap = normalBitmap;
    }

    public Bitmap getHitBitmap() {
        return hitBitmap;
    }

    public void setHitBitmap(Bitmap hitBitmap) {
        this.hitBitmap = hitBitmap;
    }

    public Bitmap getErrorBitmap() {
        return errorBitmap;
    }

    public void setErrorBitmap(Bitmap errorBitmap) {
        this.errorBitmap = errorBitmap;
    }

    public int getLinkColor() {
        return linkColor;
    }

    public void setLinkColor(int linkColor) {
        this.linkColor = linkColor;
    }

    public int getLinkErrorColor() {
        return linkErrorColor;
    }

    public void setLinkErrorColor(int linkErrorColor) {
        this.linkErrorColor = linkErrorColor;
    }

    public int getLinkWidth() {
        return linkWidth;
    }

    public void setLinkWidth(int linkWidth) {
        this.linkWidth = linkWidth;
    }
}
