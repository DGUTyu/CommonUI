package com.wxdgut.uilibrary.iv;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.wxdgut.uilibrary.R;

/**
 * 支持变色的ImageView
 * 使用示例：
 * 1.build.gradle(app)下的defaultConfig标签中添加
 * buildConfigField("int", "IMG_PRO_COLOR", "R.color.img_theme")
 * 请确保R.color.img_theme中有对应的颜色值
 * 2.在Application的onCreate方法中初始化ImageViewPro
 *         ImgProUtils.init(new AppConfig() {
 *             @Override
 *             public int getDefaultColorId() {
 *                 return BuildConfig.IMG_PRO_COLOR;
 *             }
 *         });
 * 3.布局文件中使用ImageViewPro，建议指明好src
 *         <com.wxdgut.uilibrary.iv.ImageViewPro
 *             android:id="@+id/iv_man"
 *             android:layout_width="wrap_content"
 *             android:layout_height="wrap_content"
 *             android:src="@drawable/img_man" />
 * 4.代码中初始化ImageViewPro
 *         ImageViewPro iv_man = findViewById(R.id.iv_man);
 * 5.代码中改ImageViewPro的颜色或图标及颜色
 *         iv_man.setImageResourceWithColor(R.drawable.img_woman, R.color.red);
 *         iv_data.setTint(R.color.red);
 *
 * 如果buildConfigField没有指明图片颜色，则控件使用默认的颜色。
 * 如果buildConfigField指明图片颜色同时指定了defaultColor，则该控件（有明确指示的）优先显示defaultColor，其余显示buildConfigField指明的颜色
 */
public class ImageViewPro extends AppCompatImageView {
    private int mImageResId = -1;
    private int mColor = Color.BLACK;
    private int defaultColorId;
    private int defaultColor;

    public ImageViewPro(Context context) {
        super(context);
    }

    public ImageViewPro(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ImageViewPro(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    // 初始化方法，从 XML 属性中获取图片资源 ID，并进行着色
    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = null;
        TypedArray mTypedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attrs, new int[]{android.R.attr.src});
            mImageResId = typedArray.getResourceId(0, -1);
            mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.ImageViewPro);
            defaultColor = mTypedArray.getColor(R.styleable.ImageViewPro_defaultColor, 0);
        } finally {
            if (typedArray != null) {
                typedArray.recycle();
            }
            if (mTypedArray != null) {
                mTypedArray.recycle();
            }
        }
        if (mImageResId != -1) {
            defaultColorId = ColorConfigUtils.getDefaultImgColorId();
            if (defaultColor != 0) {
                setImageResourceWithColor(mImageResId, defaultColor);
            } else {
                setImageResourceWithColorId(mImageResId, defaultColorId);
            }
        }
    }

    // 在运行时动态更换图片资源并着色
    public void setImageResourceWithColor(int resId, int color) {
        // 静态方法，通过传入一个 Context 对象和 Drawable 资源的 ID 来获取相应的 Drawable 对象
        Drawable drawable = ContextCompat.getDrawable(getContext(), resId);
        if (drawable == null) {
            return;
        }
        mColor = color;
        PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(mColor, PorterDuff.Mode.SRC_ATOP);
        drawable.setColorFilter(colorFilter);
        setImageDrawable(drawable);
    }

    // 在运行时动态更换图片资源并着色
    public void setImageResourceWithColorId(int resId, int colorId) {
        int color = ContextCompat.getColor(getContext(), colorId);
        setImageResourceWithColor(resId, color);
    }

    // 在运行时动态更换图片资源并着主题色
    public void setImageResource(int resId) {
        setImageResourceWithColorId(resId, defaultColorId);
    }

    // 在运行时动态改变 ImageView 的颜色
    public void setColor(int color) {
        //  ImageView 类中的非静态方法，只能在 ImageView 类及其子类中使用，用于获取当前 ImageView 中显示的 Drawable 对象。
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        mColor = color;
        PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(mColor, PorterDuff.Mode.SRC_ATOP);
        drawable.setColorFilter(colorFilter);
        invalidate();
    }

    // 在运行时动态改变 ImageView 的颜色
    public void setColorId(int colorId) {
        int color = ContextCompat.getColor(getContext(), colorId);
        setColor(color);
    }

    // 在运行时动态恢复 ImageView 的颜色
    public void setDefaultColor() {
        int color = ContextCompat.getColor(getContext(), defaultColorId);
        setColor(color);
    }

    // 在运行时动态恢复 ImageView 的图片及颜色
    public void setDefault() {
        if (mImageResId == -1) return;
        setImageResourceWithColorId(mImageResId, defaultColorId);
    }

    // 编译时，批量修改该页面中其他图标的颜色。
    // 此方法也可以当做工具类方法来使用，new一个ImageViewPro对象来执行此种方法。
    // 如果该resId之前未被ImageViewPro引用过（可以理解为保持原有颜色），则其他页面的该resId颜色也会发生变化。
    public void changeOtherImgByColor(int color, int... resIds) {
        for (int i = 0; i < resIds.length; i++) {
            setImageResourceWithColor(resIds[i], color);
        }
    }

    // 编译时，批量修改该页面中其他图标的颜色。
    public void changeOtherImgByColorId(int colorId, int... resIds) {
        int color = ContextCompat.getColor(getContext(), colorId);
        changeOtherImgByColor(color, resIds);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // 避免内存泄漏
        setImageDrawable(null);
    }
}