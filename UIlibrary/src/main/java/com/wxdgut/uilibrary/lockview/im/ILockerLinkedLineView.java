package com.wxdgut.uilibrary.lockview.im;

import android.graphics.Canvas;

import com.wxdgut.uilibrary.lockview.bean.CellBean;

import java.util.List;

public interface ILockerLinkedLineView {

    /**
     * 绘制图案密码连接线
     *
     * @param canvas
     * @param hitIndexList
     * @param cellBeanList
     * @param endX
     * @param endY
     * @param isError
     */
    void draw(Canvas canvas,
              List<Integer> hitIndexList, List<CellBean> cellBeanList,
              float endX, float endY, boolean isError);
}
