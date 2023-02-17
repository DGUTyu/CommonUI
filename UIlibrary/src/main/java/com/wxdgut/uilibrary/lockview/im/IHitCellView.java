package com.wxdgut.uilibrary.lockview.im;

import android.graphics.Canvas;

import com.wxdgut.uilibrary.lockview.bean.CellBean;

public interface IHitCellView {

    /**
     * 绘制已设置的每个图案的样式
     *
     * @param canvas
     * @param cellBean
     * @param isError
     */
    void draw(Canvas canvas, CellBean cellBean, boolean isError);
}
