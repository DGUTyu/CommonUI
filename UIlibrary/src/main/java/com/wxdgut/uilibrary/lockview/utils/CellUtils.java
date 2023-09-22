package com.wxdgut.uilibrary.lockview.utils;

import android.graphics.Paint;

import com.wxdgut.uilibrary.lockview.bean.CellBean;

import java.util.ArrayList;
import java.util.List;

public class CellUtils {
    //最小模块份数 圆形份数2 间距份数1（固定常量，为了保持正方形）
    private static final int ITEM_NUM = 3;
    //行列数（自变量）
    private static final int ROWS = 3;
    //独立分数（因变量）
    private static final float PER = ((ITEM_NUM * ROWS) - 1) * 1.0f;

    public static List<CellBean> buildCells(int width, int height) {
        List<CellBean> result = new ArrayList<>();
        float pWidth = (float) width / PER;
        float pHeight = (float) height / PER;

        for (int i = 0; i < ROWS * ROWS; i++) {
            // 计算行数
            int row = i / ROWS;
            // 计算列数
            int col = i % ROWS;
            CellBean cellBean = new CellBean(i, i % ROWS, i / ROWS, (float) (1 + row * ITEM_NUM) * pWidth, (float) (1 + col * ITEM_NUM) * pHeight, pWidth);
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
