package com.wxdgut.uilibrary.popup;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 使用注解定义了一个名为 XGravity 的枚举类型，用于表示水平方向的对齐方式。
 *
 * @IntDef 注解表示这是一个整数类型的枚举，可以理解为取值范围的声明。
 * @Retention(RetentionPolicy.SOURCE) 表示这个注解只在源代码级别保留，不会包含在编译后的字节码中。
 */
@IntDef({
        XGravity.CENTER,
        XGravity.LEFT,
        XGravity.RIGHT,
        XGravity.ALIGN_LEFT,
        XGravity.ALIGN_RIGHT,
})
@Retention(RetentionPolicy.SOURCE)
public @interface XGravity {
    int CENTER = 0;
    int LEFT = 1;
    int RIGHT = 2;
    int ALIGN_LEFT = 3;
    int ALIGN_RIGHT = 4;
}