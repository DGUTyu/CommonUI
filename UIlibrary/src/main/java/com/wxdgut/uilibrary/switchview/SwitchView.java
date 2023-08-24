package com.wxdgut.uilibrary.switchview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.wxdgut.uilibrary.R;
import com.wxdgut.uilibrary.utils.CommonUtils;
import com.wxdgut.uilibrary.utils.UIConfigUtils;

/**
 * 自定义开关View
 *
 * 如果buildConfigField没有指明图片开关的颜色，则控件使用默认的颜色。
 * 开关图片的背景色如果没有指明，则采用当前颜色的0.2透明度的值。也可以指明当前颜色的透明度或当前颜色
 */
public class SwitchView extends View {
    private int circlePadding;
    private int switchWidth;
    private int switchHeight;
    private int borderWidth;
    private int onColorBg;
    private int offColorBg;
    private int onColor;
    private int offColor;
    private float onAlpha;
    private float offAlpha;
    private int borderColor;

    private boolean isChecked;
    private ClickListener listener;

    private Paint switchPaint;
    private Paint borderPaint;

    private int radius;
    private Path clipPath;
    private Path borderPath;
    private RectF borderRectF;

    public SwitchView(Context context) {
        super(context);
        init(null);
        initListener();
    }

    private void initListener() {
        setOnClickListener(view -> {
            isChecked = !isChecked;
            invalidate();
            if (listener != null) {
                listener.onSwitchStateChanged(isChecked);
            }
        });
    }

    public SwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
        initListener();
    }

    public SwitchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
        initListener();
    }

    private void init(AttributeSet attrs) {
        // 获取其他属性
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.SwitchView);
        circlePadding = ta.getDimensionPixelSize(R.styleable.SwitchView_swCirclePadding, CommonUtils.dpToPx(3));
        switchWidth = ta.getDimensionPixelSize(R.styleable.SwitchView_swWidth, CommonUtils.dpToPx(60));
        switchHeight = ta.getDimensionPixelSize(R.styleable.SwitchView_swHeight, CommonUtils.dpToPx(30));
        borderWidth = ta.getDimensionPixelSize(R.styleable.SwitchView_swBorderWidth, CommonUtils.dpToPx(2));
        // 使用透明度属性来设置 onColorBg 和 offColorBg 的透明度
        onAlpha = ta.getFloat(R.styleable.SwitchView_swOnAlpha, 0.2f);
        offAlpha = ta.getFloat(R.styleable.SwitchView_swOffAlpha, 0.2f);
        onColor = ta.getColor(R.styleable.SwitchView_swOnColor, getColorById(UIConfigUtils.getDefaultSwitchOnColorId()));
        onColorBg = ta.getColor(R.styleable.SwitchView_swOnColorBg, addAlpha(onColor, onAlpha));
        offColor = ta.getColor(R.styleable.SwitchView_swOffColor, getColorById(UIConfigUtils.getDefaultSwitchOffColorId()));
        offColorBg = ta.getColor(R.styleable.SwitchView_swOffColorBg, addAlpha(offColor, offAlpha));
        borderColor = ta.getColor(R.styleable.SwitchView_swBorderColor, 0xFFE5E6EB);
        isChecked = ta.getBoolean(R.styleable.SwitchView_swIsChecked, false);
        ta.recycle();

        // 初始化画笔
        switchPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        switchPaint.setAntiAlias(true);

        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setAntiAlias(true);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(borderWidth);

        // 初始化裁剪路径和边框路径
        clipPath = new Path();
        borderPath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 增加对padding的处理
        int desiredWidth = switchWidth + getPaddingLeft() + getPaddingRight();
        int desiredHeight = switchHeight + getPaddingTop() + getPaddingBottom();
        int width = resolveSize(desiredWidth, widthMeasureSpec);
        int height = resolveSize(desiredHeight, heightMeasureSpec);
        setMeasuredDimension(width, height);

        // 计算圆形的半径，这里使用switchHeight的一半作为圆形的半径
        radius = (height - getPaddingTop() - getPaddingBottom()) / 2;
        // 设置边框矩形
        borderRectF = new RectF(getPaddingLeft(), getPaddingTop(), width - getPaddingRight(), height - getPaddingBottom());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 创建裁剪区域，使得绘制的内容只在边框内部显示
        clipPath.reset();
        clipPath.addRoundRect(borderRectF, radius, radius, Path.Direction.CW);
        canvas.clipPath(clipPath);

        // 根据isChecked设置背景颜色（只在边框内部绘制）
        int backgroundColor = isChecked ? onColorBg : offColorBg;
        canvas.drawColor(backgroundColor);

        // 绘制开或关对应的圆形
        int centerX = isChecked ? getWidth() - getPaddingRight() - radius : getPaddingLeft() + radius;
        int centerY = getHeight() / 2;

        // 绘制圆形
        int circleColor = isChecked ? onColor : offColor;
        switchPaint.setColor(circleColor);
        switchPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(centerX, centerY, radius - circlePadding, switchPaint);

        // 绘制边框
        borderPath.reset();
        borderPath.addRoundRect(borderRectF, radius, radius, Path.Direction.CW);
        borderPaint.setColor(borderColor); // 设置边框颜色
        canvas.drawPath(borderPath, borderPaint);
    }


    // 添加透明度，返回一个带有透明度的新颜色值。颜色的透明度范围是 0（完全透明）到 255（完全不透明）
    public int addAlpha(int color, float alpha) {
        int alphaValue = (int) (alpha * 255) << 24;
        return (color & 0x00FFFFFF) | alphaValue;
    }

    public void setClickListener(ClickListener listener) {
        this.listener = listener;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
        invalidate();
        if (listener != null) {
            listener.onSwitchStateChanged(isChecked);
        }
    }

    public boolean isChecked() {
        return isChecked;
    }

    public interface ClickListener {
        void onSwitchStateChanged(boolean isChecked);
    }

    public int getColorById(int colorId) {
        return ContextCompat.getColor(getContext(), colorId);
    }
}