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
 */
public class ImageViewPro extends ImageView {
    private int mImageResId = -1;
    private int mColor = Color.BLACK;

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
        try {
            typedArray = context.obtainStyledAttributes(attrs, new int[]{android.R.attr.src});
            mImageResId = typedArray.getResourceId(0, -1);
        } finally {
            if (typedArray != null) {
                typedArray.recycle();
            }
        }
        if (mImageResId != -1) {
            //设置默认颜色
            int defaultColor = ImgProUtils.getDefaultColor();
            setImageResourceWithColor(mImageResId, defaultColor);
        }
    }


    /**
     * 设置图片资源并着色
     * 在 Android 中，Drawable.setColorFilter() 方法从 API level 1 就已经存在,
     * PorterDuff.Mode.SRC_ATOP 模式也是在 API level 1 中引入的，因此也可以在所有 Android 版本中使用。
     * 需要注意的是，Drawable.setColorFilter() 方法的实现可能会略有不同，具体取决于 Android 平台版本和使用的绘图库。
     * 一些早期版本的 setColorFilter() 方法可能对 PorterDuff.Mode 的支持不够完善，或者存在一些兼容性问题。
     * 为了保证最好的兼容性，建议使用 ColorFilter 类的 PorterDuffColorFilter 子类进行着色。
     * <p>
     * //方法1
     * drawable.setColorFilter(mColor, PorterDuff.Mode.SRC_ATOP);
     * //方法2
     * PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(mColor, PorterDuff.Mode.SRC_ATOP);
     * drawable.setColorFilter(colorFilter);
     *
     * @param resId
     * @param colorId
     */
    public void setImageResourceWithColor(int resId, int colorId) {
        // 静态方法，通过传入一个 Context 对象和 Drawable 资源的 ID 来获取相应的 Drawable 对象
        Drawable drawable = ContextCompat.getDrawable(getContext(), resId);
        if (drawable == null) {
            return;
        }
        // 在 API level 23 及以上版本中获取颜色值
        mColor = ContextCompat.getColor(getContext(), colorId);
        PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(mColor, PorterDuff.Mode.SRC_ATOP);
        drawable.setColorFilter(colorFilter);
        setImageDrawable(drawable);
    }

    // 在运行时动态改变 ImageView 的颜色
    public void setColor(int colorId) {
        //  ImageView 类中的非静态方法，只能在 ImageView 类及其子类中使用，用于获取当前 ImageView 中显示的 Drawable 对象。
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        // 在 API level 23 及以上版本中获取颜色值
        mColor = ContextCompat.getColor(getContext(), colorId);
        PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(mColor, PorterDuff.Mode.SRC_ATOP);
        drawable.setColorFilter(colorFilter);
        invalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // 避免内存泄漏
        // 这种做法可能会导致重新加载图片时存在闪烁等问题。为了解决这个问题，我们可以使用 LRU 缓存等技术，避免重复加载相同的图片资源。
        setImageDrawable(null);
    }
}



//public class ImageViewPro extends ImageView {
//    private int mImageResId = -1;
//    private int mColor = Color.BLACK;
//
//    public ImageViewPro(Context context) {
//        super(context);
//    }
//
//    public ImageViewPro(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        init(context, attrs);
//    }
//
//    public ImageViewPro(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//        init(context, attrs);
//    }
//
//    // 初始化方法，从 XML 属性中获取图片资源 ID，并进行着色
//    private void init(Context context, AttributeSet attrs) {
//        TypedArray typedArray = null;
//        try {
//            typedArray = context.obtainStyledAttributes(attrs, new int[]{android.R.attr.src});
//            mImageResId = typedArray.getResourceId(0, -1);
//        } finally {
//            if (typedArray != null) {
//                typedArray.recycle();
//            }
//        }
//        if (mImageResId != -1) {
//            //设置默认颜色
//            int parseColor = Color.parseColor(BuildConfig.IMG_COLOR);
//            setImageResourceWithColor(mImageResId, R.color.green);
//        }
//    }
//
//
//    /**
//     * 设置图片资源并着色
//     * 在 Android 中，Drawable.setColorFilter() 方法从 API level 1 就已经存在,
//     * PorterDuff.Mode.SRC_ATOP 模式也是在 API level 1 中引入的，因此也可以在所有 Android 版本中使用。
//     * 需要注意的是，Drawable.setColorFilter() 方法的实现可能会略有不同，具体取决于 Android 平台版本和使用的绘图库。
//     * 一些早期版本的 setColorFilter() 方法可能对 PorterDuff.Mode 的支持不够完善，或者存在一些兼容性问题。
//     * 为了保证最好的兼容性，建议使用 ColorFilter 类的 PorterDuffColorFilter 子类进行着色。
//     * <p>
//     * //方法1
//     * drawable.setColorFilter(mColor, PorterDuff.Mode.SRC_ATOP);
//     * //方法2
//     * PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(mColor, PorterDuff.Mode.SRC_ATOP);
//     * drawable.setColorFilter(colorFilter);
//     *
//     * @param resId
//     * @param colorId
//     */
//    public void setImageResourceWithColor(int resId, int colorId) {
//        // 静态方法，通过传入一个 Context 对象和 Drawable 资源的 ID 来获取相应的 Drawable 对象
//        Drawable drawable = ContextCompat.getDrawable(getContext(), resId);
//        if (drawable == null) {
//            return;
//        }
//        // 在 API level 23 及以上版本中获取颜色值
//        mColor = ContextCompat.getColor(getContext(), colorId);
//        PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(mColor, PorterDuff.Mode.SRC_ATOP);
//        drawable.setColorFilter(colorFilter);
//        setImageDrawable(drawable);
//    }
//
//    // 在运行时动态改变 ImageView 的颜色
//    public void setColor(int colorId) {
//        //  ImageView 类中的非静态方法，只能在 ImageView 类及其子类中使用，用于获取当前 ImageView 中显示的 Drawable 对象。
//        Drawable drawable = getDrawable();
//        if (drawable == null) {
//            return;
//        }
//        // 在 API level 23 及以上版本中获取颜色值
//        mColor = ContextCompat.getColor(getContext(), colorId);
//        PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(mColor, PorterDuff.Mode.SRC_ATOP);
//        drawable.setColorFilter(colorFilter);
//        invalidate();
//    }
//
//    @Override
//    protected void onDetachedFromWindow() {
//        super.onDetachedFromWindow();
//        // 避免内存泄漏
//        // 这种做法可能会导致重新加载图片时存在闪烁等问题。为了解决这个问题，我们可以使用 LRU 缓存等技术，避免重复加载相同的图片资源。
//        setImageDrawable(null);
//    }
//}

/*
public class ImageViewPro extends ImageView {
    private int mImageResId = -1;
    private int mColor = Color.BLACK;

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

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attrs, new int[]{android.R.attr.src});
            mImageResId = typedArray.getResourceId(0, -1);
        } finally {
            if (typedArray != null) {
                typedArray.recycle();
            }
        }
        if (mImageResId != -1) {
            setImageResourceWithColor(mImageResId, R.color.green);
        }
    }

    public void setImageResourceWithColor(int resId, int colorId) {
        Drawable drawable = ContextCompat.getDrawable(getContext(), resId);
        if (drawable == null) {
            return;
        }
        mColor = getContext().getResources().getColor(colorId);
        drawable.setColorFilter(mColor, PorterDuff.Mode.SRC_ATOP);
        setImageDrawable(drawable);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // 避免内存泄漏
        setImageDrawable(null);
    }
}
 */

/*
public class ImageViewPro extends ImageView {
    private Context mContext;
    private int mImageResId;
    private int mColor;

    public ImageViewPro(Context context) {
        super(context);
        mContext = context;
    }

    public ImageViewPro(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        //在代码中获取属性值
        TypedArray typedArray = context.obtainStyledAttributes(attrs, new int[]{android.R.attr.src});
        int imageResId = typedArray.getResourceId(0, -1);
        mImageResId = imageResId;
        typedArray.recycle();
        if (imageResId != -1) setImageResourceWithColor(mImageResId, R.color.green);
    }

    public ImageViewPro(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }

    public void setImageResourceWithColor(int resId, int colorId) {
        if (mContext == null) return;
        mColor = mContext.getResources().getColor(colorId);
        Drawable drawable = ContextCompat.getDrawable(getContext(), resId);
        drawable.setColorFilter(mColor, PorterDuff.Mode.SRC_ATOP);

        setImageDrawable(drawable);
    }
}
 */
