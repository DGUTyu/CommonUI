package com.wxdgut.uilibrary.lockview.customized;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Pair;

import com.wxdgut.uilibrary.lockview.bean.CellBean;
import com.wxdgut.uilibrary.lockview.decorator.DefaultStyleDecorator;
import com.wxdgut.uilibrary.lockview.im.IHitCellView;
import com.wxdgut.uilibrary.lockview.utils.CellUtils;

import java.util.Collections;
import java.util.List;


public class MyLockerHitCellView implements IHitCellView {

    private Paint paint;
    private DefaultStyleDecorator styleDecorator;
    //Pair.first:颜色值 Pair.second:百分比
    private List<Pair<Integer, Float>> pairList;

    public MyLockerHitCellView(DefaultStyleDecorator styleDecorator, List<Pair<Integer, Float>> pairList) {
        this.paint = CellUtils.createPaint();
        this.paint.setStyle(Paint.Style.FILL);
        this.styleDecorator = styleDecorator;
        this.pairList = pairList;
    }

    @Override
    public void draw(Canvas canvas, CellBean cellBean, boolean isError) {
        int saveCount = canvas.save();
        // draw outer circle 画外圆
        this.paint.setColor(this.getColor(isError));
        canvas.drawCircle(cellBean.getCenterX(), cellBean.getCenterY(), cellBean.getRadius(), this.paint);

        // draw fill circle 画填充圆
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

        // draw inner circle 画内部圆。放在最后画，覆盖前面的图案。
        this.paint.setColor(this.getColor(isError));
        float innerHitPercent = this.styleDecorator.getInnerHitPercent();
        canvas.drawCircle(cellBean.getCenterX(), cellBean.getCenterY(), cellBean.getRadius() * innerHitPercent, this.paint);
        canvas.restoreToCount(saveCount);
    }

    private int getColor(boolean isError) {
        return isError ? this.styleDecorator.getErrorColor() : this.styleDecorator.getHitColor();
    }
}
