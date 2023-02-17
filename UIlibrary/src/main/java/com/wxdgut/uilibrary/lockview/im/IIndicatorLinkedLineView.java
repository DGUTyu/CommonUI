package com.wxdgut.uilibrary.lockview.im;

import android.graphics.Canvas;

import com.wxdgut.uilibrary.lockview.bean.CellBean;

import java.util.List;

public interface IIndicatorLinkedLineView {

    /**
     * 绘制指示器连接线
     *
     * @param canvas
     * @param hitIndexList
     * @param cellBeanList
     * @param isError
     */
    void draw(Canvas canvas, List<Integer> hitIndexList, List<CellBean> cellBeanList, boolean isError);
}