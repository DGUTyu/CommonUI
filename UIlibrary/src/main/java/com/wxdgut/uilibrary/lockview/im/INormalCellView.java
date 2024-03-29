package com.wxdgut.uilibrary.lockview.im;

import android.graphics.Canvas;

import com.wxdgut.uilibrary.lockview.bean.CellBean;

public interface INormalCellView {

    /**
     * 绘制正常情况下（即未设置的）每个图案的样式
     *
     * @param canvas
     * @param cellBean the target cell view
     */
    void draw(Canvas canvas, CellBean cellBean);
}
