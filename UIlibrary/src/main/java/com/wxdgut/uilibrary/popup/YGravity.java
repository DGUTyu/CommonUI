package com.wxdgut.uilibrary.popup;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/**
 * 使用注解定义了一个名为 YGravity 的枚举类型，用于表示水平方向的对齐方式。
 *
 * @IntDef 注解表示这是一个整数类型的枚举，可以理解为取值范围的声明。
 * @Retention(RetentionPolicy.SOURCE) 表示这个注解只在源代码级别保留，不会包含在编译后的字节码中。
 */
@IntDef({
        YGravity.CENTER,
        YGravity.ABOVE,
        YGravity.BELOW,
        YGravity.ALIGN_TOP,
        YGravity.ALIGN_BOTTOM,
})
@Retention(RetentionPolicy.SOURCE)
public @interface YGravity {
    int CENTER = 0;
    int ABOVE = 1;
    int BELOW = 2;
    int ALIGN_TOP = 3;
    int ALIGN_BOTTOM = 4;
}
