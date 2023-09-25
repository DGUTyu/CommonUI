package com.wxdgut.uilibrary.lockview.utils;

import android.graphics.Paint;

import com.wxdgut.uilibrary.lockview.bean.CellBean;

import java.util.ArrayList;
import java.util.List;

public class CellUtils {
    //最小模块份数 圆形份数2 间距份数1（固定常量，为了保持正方形）
    private static final int ITEM_NUM = 3;

    public static List<CellBean> buildCells(int width, int height) {
        return buildCells(width, height, 3);
    }

    public static List<CellBean> buildCells(int width, int height, Integer rows) {
        //行数
        if (rows == null || rows < 1) {
            rows = 1;
        }
        //份数
        float per = ((ITEM_NUM * rows) - 1) * 1.0f;
        List<CellBean> result = new ArrayList<>();
        //每份的宽度、高度
        float pWidth = (float) width / per;
        float pHeight = (float) height / per;

        for (int i = 0; i < rows * rows; i++) {
            // 计算行数
            int row = i / rows;
            // 计算列数
            int col = i % rows;
            CellBean cellBean = new CellBean(i, i % rows, i / rows, (float) (1 + row * ITEM_NUM) * pWidth, (float) (1 + col * ITEM_NUM) * pHeight, pWidth);
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
