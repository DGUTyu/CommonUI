package com.wxdgut.uilibrary.btn;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.wxdgut.uilibrary.R;
import com.wxdgut.uilibrary.utils.UIConfigUtils;

/**
 * 自定义Button，支持圆角矩形，圆形按钮等样式
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
    //圆角
    private int radius;
    //圆角
    private float[] radii;
    //边框属性
    private int borderWidth;
    private int borderColor;
    private int selectedBorderWidth;
    private int selectedBorderColor;

    //触摸监听器
    private TouchListener listener;

    public interface TouchListener {
        void onTouch(MotionEvent event);
    }

    //设置触摸监听器
    public void setTouchListener(TouchListener listener) {
        this.listener = listener;
    }

    //处理点击事件的接口,isChange用于记录点击状态（按需采用）
    public interface MyListener {
        //isChange 能起到类似开关标志的作用，isChange首次回调为true，此后依次反转
        void click(View view, boolean isChange);
    }

    //设置点击监听器，也可以不用此做法
    public void setClick(MyListener myListener) {
        final boolean[] state = {false};
        setOnClickListener(v -> {
            state[0] = !state[0];
            myListener.click(v, state[0]);
        });
    }

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
            //圆角
            radius = a.getDimensionPixelSize(R.styleable.CommonButton_btnRadius, UIConfigUtils.getDefaultBtnRadius());
            //边框属性
            borderWidth = a.getDimensionPixelSize(R.styleable.CommonButton_btnBorderWidth, 0);
            borderColor = a.getColor(R.styleable.CommonButton_btnBorderColor, Color.TRANSPARENT);
            selectedBorderWidth = a.getDimensionPixelSize(R.styleable.CommonButton_btnSelectedBorderWidth, borderWidth);
            selectedBorderColor = a.getColor(R.styleable.CommonButton_btnSelectedBorderColor, borderColor);
            //设置边框宽度限制
            if (radius > 0) {
                borderWidth = borderWidth > radius ? radius : borderWidth;
                selectedBorderWidth = selectedBorderWidth > radius ? radius : selectedBorderWidth;
            }
            //圆角属性
            int mRadiusTopLeft = a.getDimensionPixelSize(R.styleable.CommonButton_btnRadiusTopLeft, radius);
            int mRadiusTopRight = a.getDimensionPixelSize(R.styleable.CommonButton_btnRadiusTopRight, radius);
            int mRadiusBottomRight = a.getDimensionPixelSize(R.styleable.CommonButton_btnRadiusBottomRight, radius);
            int mRadiusBottomLeft = a.getDimensionPixelSize(R.styleable.CommonButton_btnRadiusBottomLeft, radius);
            radii = new float[]{
                    mRadiusTopLeft, mRadiusTopLeft,
                    mRadiusTopRight, mRadiusTopRight,
                    mRadiusBottomRight, mRadiusBottomRight,
                    mRadiusBottomLeft, mRadiusBottomLeft
            };
            //设置背景色
            ColorStateList colorList = a.getColorStateList(R.styleable.CommonButton_btnBgColor);
            if (colorList != null) {
                bgColor = colorList.getColorForState(getDrawableState(), 0);
                if (bgColor != 0) {
                    setBackgroundColor(bgColor);
                }
            } else {
                bgColor = getColorById(UIConfigUtils.getDefaultBtnBgColorId());
                setBackgroundColor(bgColor);
            }
            //记录按钮被按下时的背景色
            ColorStateList colorListPress = a.getColorStateList(R.styleable.CommonButton_btnBgColorPress);
            if (colorListPress != null) {
                bgColorPress = colorListPress.getColorForState(getDrawableState(), 0);
            } else {
                bgColorPress = getColorById(UIConfigUtils.getDefaultBtnBgPressColorId());
                setBackgroundColor(bgColorPress);
            }
            //设置背景图片，若backColor与backGroundDrawable同时存在，则backGroundDrawable将覆盖backColor
            bgDrawable = a.getDrawable(R.styleable.CommonButton_btnBgImg);
            if (bgDrawable != null) {
                setBackground(bgDrawable);
            }
            //记录按钮被按下时的背景图片
            bgDrawablePress = a.getDrawable(R.styleable.CommonButton_btnBgImgPress);
            //设置文字的颜色
            textColor = a.getColorStateList(R.styleable.CommonButton_btnTvColor);
            if (textColor != null) {
                setTextColor(textColor);
            }else {
                int defaultColor = getColorById(UIConfigUtils.getDefaultBtnTvColorId());
                textColor = ColorStateList.valueOf(defaultColor); // 默认颜色
                setTextColor(textColor);
            }
            //记录按钮被按下时文字的颜色
            textColorPress = a.getColorStateList(R.styleable.CommonButton_btnTvColorPress);
            if (textColorPress == null) {
                int pressColor = getColorById(UIConfigUtils.getDefaultBtnTvPressColorId());
                textColorPress = ColorStateList.valueOf(pressColor); // 按压颜色
            }
            //设置圆角或圆形等样式的背景色
            fillet = a.getBoolean(R.styleable.CommonButton_btnFillet, true);
            if (fillet) {
                getGradientDrawable();
                gradientDrawable.setCornerRadii(radii);
                gradientDrawable.setStroke(borderWidth, borderColor); // 设置边框
                if (bgColor != 0) {
                    gradientDrawable.setColor(bgColor);
                    setBackground(gradientDrawable);
                }
            }
            //设置按钮形状，fillet为true时才生效
            int shape = a.getInteger(R.styleable.CommonButton_btnShape, 0);
            if (fillet && shape != 0) {
                setShape(shape);
            }
            a.recycle();
        }
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (listener != null) {
                    listener.onTouch(event);
                }
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
                    gradientDrawable.setCornerRadii(radii); // 设置圆角
                    gradientDrawable.setStroke(selectedBorderWidth, selectedBorderColor); // 设置按压状态的边框
                    gradientDrawable.setColor(bgColorPress); // 设置按压状态的背景颜色
                    setBackground(gradientDrawable); // 应用设置
                } else {
                    setBackgroundColor(bgColorPress); // 设置按压状态的背景颜色
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
                    gradientDrawable.setCornerRadii(radii); // 设置圆角
                    gradientDrawable.setStroke(borderWidth, borderColor); // 设置边框
                    gradientDrawable.setColor(bgColor); // 设置背景颜色
                    setBackground(gradientDrawable); // 应用设置
                } else {
                    setBackgroundColor(bgColor); // 设置背景颜色
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

    //设置圆角按钮的角度
    public void setCornerRadii(float[] radii) {
        if (!fillet) return;
        getGradientDrawable();
        gradientDrawable.setCornerRadii(radii);
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

    public int getColorById(int colorId) {
        return ContextCompat.getColor(getContext(), colorId);
    }
}
