package com.wxdgut.uilibrary.dialog;


import android.view.Gravity;
import android.view.View;

import com.wxdgut.uilibrary.R;

import java.lang.ref.WeakReference;

import static com.wxdgut.uilibrary.dialog.BaseDialog.UN_DEFINED;

/**
 * 用于初始化BaseDialog
 */
public class BaseDialogInitBean {
    private int styleId = R.style.My_Dialog_Theme;
    private int layout = R.layout.dialog_fingerprint;
    private boolean widthMatch = false;
    private int height = UN_DEFINED;
    private int gravity = Gravity.CENTER;
    private boolean draggable = false;
    private boolean cancelable = true;
    private boolean autoDismiss = true;
    private boolean clearShadow = false;
    private float dimAmount = 0.1f;
    int animId = UN_DEFINED;
    private WeakReference<View> anchorWR;
    private int anchorGravity = Gravity.BOTTOM;
    private int xOff = 0;
    private int yOff = 0;

    public int getLayout() {
        return layout;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    public boolean isWidthMatch() {
        return widthMatch;
    }

    public void setWidthMatch(boolean widthMatch) {
        this.widthMatch = widthMatch;
    }

    public int getGravity() {
        return gravity;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    public int getStyleId() {
        return styleId;
    }

    public void setStyleId(int styleId) {
        this.styleId = styleId;
    }

    public boolean isDraggable() {
        return draggable;
    }

    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
    }

    public boolean isCancelable() {
        return cancelable;
    }

    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    public boolean isAutoDismiss() {
        return autoDismiss;
    }

    public void setAutoDismiss(boolean autoDismiss) {
        this.autoDismiss = autoDismiss;
    }

    public boolean isClearShadow() {
        return clearShadow;
    }

    public void setClearShadow(boolean clearShadow) {
        this.clearShadow = clearShadow;
    }

    public float getDimAmount() {
        return dimAmount;
    }

    public void setDimAmount(float dimAmount) {
        this.dimAmount = dimAmount;
    }

    public WeakReference<View> getAnchorWR() {
        return anchorWR;
    }

    public void setAnchorWR(View anchor) {
        this.anchorWR = new WeakReference<>(anchor);
    }

    public int getAnchorGravity() {
        return anchorGravity;
    }

    public void setAnchorGravity(int anchorGravity) {
        this.anchorGravity = anchorGravity;
    }

    public int getxOff() {
        return xOff;
    }

    public void setxOff(int xOff) {
        this.xOff = xOff;
    }

    public int getyOff() {
        return yOff;
    }

    public void setyOff(int yOff) {
        this.yOff = yOff;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getAnimId() {
        return animId;
    }

    public void setAnimId(int animId) {
        this.animId = animId;
    }
}
