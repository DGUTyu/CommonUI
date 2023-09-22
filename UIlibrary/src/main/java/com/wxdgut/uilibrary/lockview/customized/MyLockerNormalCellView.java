package com.wxdgut.uilibrary.lockview.customized;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.util.Pair;

import com.wxdgut.uilibrary.lockview.bean.CellBean;
import com.wxdgut.uilibrary.lockview.decorator.DefaultStyleDecorator;
import com.wxdgut.uilibrary.lockview.im.INormalCellView;
import com.wxdgut.uilibrary.lockview.utils.CellUtils;

import java.util.Collections;
import java.util.List;

public class MyLockerNormalCellView implements INormalCellView {
    private Paint paint;
    private DefaultStyleDecorator styleDecorator;
    //Pair.first:颜色值 Pair.second:百分比
    private List<Pair<Integer, Float>> pairList;

    public MyLockerNormalCellView(DefaultStyleDecorator styleDecorator, List<Pair<Integer, Float>> pairList) {
        this.paint = CellUtils.createPaint();
        this.paint.setStyle(Paint.Style.FILL);
        this.styleDecorator = styleDecorator;
        this.pairList = pairList;
    }

    @Override
    public void draw(Canvas canvas, CellBean cellBean) {
        int saveCount = canvas.save();

        // draw outer circle
        this.paint.setColor(this.styleDecorator.getNormalColor());
        canvas.drawCircle(cellBean.getCenterX(), cellBean.getCenterY(), cellBean.getRadius(), this.paint);

        // draw fill circle
        this.paint.setColor(this.styleDecorator.getFillColor());
        canvas.drawCircle(cellBean.getCenterX(), cellBean.getCenterY(), cellBean.getRadius() - this.styleDecorator.getLineWidth(), this.paint);

        // 降序绘制
        if (pairList != null && !pairList.isEmpty()) {
            Collections.sort(pairList, (circle1, circle2) -> Float.compare(circle2.second, circle1.second));
            for (Pair<Integer, Float> circleInfo : pairList) {
                int color = circleInfo.first;
                float percent = circleInfo.second;
                if (percent < 0f || percent > 1f) {
                    percent = 0.9f;
                }
                this.paint.setColor(color);
                canvas.drawCircle(cellBean.getCenterX(), cellBean.getCenterY(), cellBean.getRadius() * percent, this.paint);
            }
        }

        // draw inner circle
        this.paint.setColor(this.styleDecorator.getNormalInnerColor());
        float innerPercent = this.styleDecorator.getInnerPercent();
        canvas.drawCircle(cellBean.getCenterX(), cellBean.getCenterY(), cellBean.getRadius() * innerPercent, this.paint);

        canvas.restoreToCount(saveCount);
    }
}