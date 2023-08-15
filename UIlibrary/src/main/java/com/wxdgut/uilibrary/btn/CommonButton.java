package com.wxdgut.uilibrary.btn;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.wxdgut.uilibrary.R;

/**
 * Created by landptf on 2016/10/25.
 * 自定义Button，支持圆角矩形，圆形按钮等样式，可通过配置文件改变按下后的样式
 * 若通过代码设置圆角或者圆形，需要先调用setFillet方法将fillet设置为true
 */
public class CommonButton extends Button {
    //按钮的背景色
    private int bgColor = 0;
    //按钮被按下时的背景色
    private int bgColorPress = 0;
    //按钮的背景图片
    private Drawable bgDrawable = null;
    //按钮被按下时显示的背景图片
    private Drawable bgDrawablePress = null;
    //按钮文字的颜色
    private ColorStateList textColor = null;
    //按钮被按下时文字的颜色
    private ColorStateList textColorPress = null;
    //
    private GradientDrawable gradientDrawable = null;
    //是否设置圆角或者圆形等样式
    private boolean fillet = false;
    //标示onTouch方法的返回值，用来解决onClick和onTouch冲突问题
    private boolean isCost = true;

    public CommonButton(Context context) {
        this(context, null, 0);
    }

    public CommonButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CommonButton, defStyle, 0);
        if (a != null) {
            //设置背景色
            ColorStateList colorList = a.getColorStateList(R.styleable.CommonButton_bgColor);
            if (colorList != null) {
                bgColor = colorList.getColorForState(getDrawableState(), 0);
                if (bgColor != 0) {
                    setBackgroundColor(bgColor);
                }
            }
            //记录按钮被按下时的背景色
            ColorStateList colorListPress = a.getColorStateList(R.styleable.CommonButton_bgColorPress);
            if (colorListPress != null) {
                bgColorPress = colorListPress.getColorForState(getDrawableState(), 0);
            }
            //设置背景图片，若backColor与backGroundDrawable同时存在，则backGroundDrawable将覆盖backColor
            bgDrawable = a.getDrawable(R.styleable.CommonButton_bgImg);
            if (bgDrawable != null) {
                setBackground(bgDrawable);
            }
            //记录按钮被按下时的背景图片
            bgDrawablePress = a.getDrawable(R.styleable.CommonButton_bgImgPress);
            //设置文字的颜色
            textColor = a.getColorStateList(R.styleable.CommonButton_tvColor);
            if (textColor != null) {
                setTextColor(textColor);
            }
            //记录按钮被按下时文字的颜色
            textColorPress = a.getColorStateList(R.styleable.CommonButton_tvColorPress);
            //设置圆角或圆形等样式的背景色
            fillet = a.getBoolean(R.styleable.CommonButton_fillet, false);
            if (fillet) {
                getGradientDrawable();
                if (bgColor != 0) {
                    gradientDrawable.setColor(bgColor);
                    setBackground(gradientDrawable);
                }
            }
            //设置圆角矩形的角度，fillet为true时才生效
            float radius = a.getFloat(R.styleable.CommonButton_radius, 0);
            if (fillet && radius != 0) {
                setRadius(radius);
            }
            //设置按钮形状，fillet为true时才生效
            int shape = a.getInteger(R.styleable.CommonButton_shape, 0);
            if (fillet && shape != 0) {
                setShape(shape);
            }
            a.recycle();
        }
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //根据touch事件设置按下抬起的样式
                return setTouchStyle(event.getAction());
            }
        });
    }

    /**
     * 根据按下或者抬起来改变背景和文字样式
     *
     * @param state
     * @return isCost
     * 为解决onTouch和onClick冲突的问题
     * 根据事件分发机制，如果onTouch返回true，则不响应onClick事件
     * 因此采用isCost标识位，当用户设置了onClickListener则onTouch返回false
     */
    private boolean setTouchStyle(int state) {
        if (state == MotionEvent.ACTION_DOWN) {
            if (bgColorPress != 0) {
                if (fillet) {
                    gradientDrawable.setColor(bgColorPress);
                    setBackground(gradientDrawable);
                } else {
                    setBackgroundColor(bgColorPress);
                }
            }
            if (bgDrawablePress != null) {
                setBackground(bgDrawablePress);
            }
            if (textColorPress != null) {
                setTextColor(textColorPress);
            }
        }
        if (state == MotionEvent.ACTION_UP) {
            if (bgColor != 0) {
                if (fillet) {
                    gradientDrawable.setColor(bgColor);
                    setBackground(gradientDrawable);
                } else {
                    setBackgroundColor(bgColor);
                }
            }
            if (bgDrawable != null) {
                setBackground(bgDrawable);
            }
            if (textColor != null) {
                setTextColor(textColor);
            }
        }
        return isCost;
    }

    /**
     * 重写setOnClickListener方法，解决onTouch和onClick冲突问题
     *
     * @param listener
     */
    @Override
    public void setOnClickListener(OnClickListener listener) {
        super.setOnClickListener(listener);
        isCost = false;
    }

    //设置按钮的背景色
    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
        if (fillet) {
            gradientDrawable.setColor(bgColor);
            setBackground(gradientDrawable);
        } else {
            setBackgroundColor(bgColor);
        }
    }

    //设置按钮被按下时的背景色
    public void setBgColorPress(int bgColorPress) {
        this.bgColorPress = bgColorPress;
    }

    //设置按钮的背景图片
    public void setBgDrawable(Drawable backGroundDrawable) {
        this.bgDrawable = backGroundDrawable;
        setBackground(backGroundDrawable);
    }

    //设置按钮被按下时的背景图片
    public void setBgDrawablePress(Drawable backGroundDrawablePress) {
        this.bgDrawablePress = backGroundDrawablePress;
    }

    //设置文字的颜色
    public void setTvColor(int textColor) {
        if (textColor == 0) return;
        this.textColor = ColorStateList.valueOf(textColor);
        //此处应加super关键字，调用父类的setTextColor方法，否则会造成递归导致内存溢出
        super.setTextColor(this.textColor);
    }

    //设置按钮被按下时文字的颜色
    public void setTvColorPress(int textColorPress) {
        if (textColorPress == 0) return;
        this.textColorPress = ColorStateList.valueOf(textColorPress);
    }

    //设置按钮是否设置圆角或者圆形等样式
    public void setFillet(boolean fillet) {
        this.fillet = fillet;
        getGradientDrawable();
    }

    //设置圆角按钮的角度
    public void setRadius(float radius) {
        if (!fillet) return;
        getGradientDrawable();
        gradientDrawable.setCornerRadius(radius);
        setBackground(gradientDrawable);
    }

    //设置按钮的形状
    public void setShape(int shape) {
        if (!fillet) return;
        getGradientDrawable();
        gradientDrawable.setShape(shape);
        setBackground(gradientDrawable);
    }

    //
    private void getGradientDrawable() {
        if (gradientDrawable == null) {
            gradientDrawable = new GradientDrawable();
        }
    }
}
