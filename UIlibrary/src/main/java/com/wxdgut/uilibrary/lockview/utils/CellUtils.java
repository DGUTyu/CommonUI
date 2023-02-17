package com.wxdgut.uilibrary.lockview.utils;

import android.graphics.Paint;

import com.wxdgut.uilibrary.lockview.bean.CellBean;

import java.util.ArrayList;
import java.util.List;

public class CellUtils {
    public static List<CellBean> buildCells(int width, int height) {
        List<CellBean> result = new ArrayList<>();
        float pWidth = (float) width / 8.0f;
        float pHeight = (float) height / 8.0f;

        for (int i = 0; i < 9; i++) {
            CellBean cellBean = new CellBean(i, i % 3, i / 3, (float) (i % 3 * 3 + 1) * pWidth, (float) (i / 3 * 3 + 1) * pHeight, pWidth);
            result.add(cellBean);
        }
        return result;
    }

    // 创建画笔
    public static Paint createPaint() {
        Paint paint = new Paint();
        paint.setDither(true);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        return paint;
    }
}
