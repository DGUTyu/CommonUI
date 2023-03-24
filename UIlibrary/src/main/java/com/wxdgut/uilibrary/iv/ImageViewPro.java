package com.wxdgut.uilibrary.iv;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ImageView;

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
 *             public int getDefaultColor() {
 *                 return BuildConfig.IMG_PRO_COLOR;
 *             }
 *         });
 * 3.布局文件中使用ImageViewPro
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
 * 注意事项：
 * 1.一个src图片,如果在xml布局中使用了ImageViewPro，则在任何一个xml下该src图片使用ImageView时也会表现出ImageViewPro的效果
 * 2.想要ImageViewPro的app:defaultColor属性生效，则要确保该src图片对应的众多ImageViewPro中，最后一个要设置app:defaultColor。否则，app:defaultColor会被默认颜色替换。
 * 综上，即可得出：
 * 1、一个src图片仅需设置一次ImageViewPro即可达到全局替换效果
 * 2、如果需要设置app:defaultColor属性，仅需在src图片对应的、唯一一个的ImageViewPro中设置。
 */
public class ImageViewPro extends ImageView {
    private int mImageResId = -1;
    private int mColor = Color.BLACK;
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
            //设置默认颜色
            int color = ImgProUtils.getDefaultColor();
            if (defaultColor != 0) {
                setImageResourceWithColor(mImageResId, defaultColor);
            } else {
                setImageResourceWithColorId(mImageResId, color);
            }
        }
    }

    //更换图片资源并着色
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

    //更换图片资源并着色
    public void setImageResourceWithColorId(int resId, int colorId) {
        // 静态方法，通过传入一个 Context 对象和 Drawable 资源的 ID 来获取相应的 Drawable 对象
        Drawable drawable = ContextCompat.getDrawable(getContext(), resId);
        if (drawable == null) {
            return;
        }
        mColor = ContextCompat.getColor(getContext(), colorId); //所有版本均适用，无需特判 API level 23 (Build.VERSION_CODES.M) 以下的情况
        PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(mColor, PorterDuff.Mode.SRC_ATOP);
        drawable.setColorFilter(colorFilter);
        setImageDrawable(drawable);
    }

    // 在运行时动态改变 ImageView 的颜色，本质上和setImageResourceWithColor无差别
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

    // 在运行时动态改变 ImageView 的颜色，本质上和setImageResourceWithColorId无差别
    public void setColorId(int colorId) {
        //  ImageView 类中的非静态方法，只能在 ImageView 类及其子类中使用，用于获取当前 ImageView 中显示的 Drawable 对象。
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        mColor = ContextCompat.getColor(getContext(), colorId); //所有版本均适用，无需特判 API level 23 (Build.VERSION_CODES.M) 以下的情况
        PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(mColor, PorterDuff.Mode.SRC_ATOP);
        drawable.setColorFilter(colorFilter);
        invalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // 避免内存泄漏
        setImageDrawable(null);
    }
}