package com.wxdgut.uilibrary.lockview.customized;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.wxdgut.uilibrary.lockview.bean.CellBean;
import com.wxdgut.uilibrary.lockview.decorator.DefaultStyleDecorator;
import com.wxdgut.uilibrary.lockview.im.IHitCellView;
import com.wxdgut.uilibrary.lockview.utils.CellUtils;


public class MyLockerHitCellView implements IHitCellView {

    private Paint paint;
    private DefaultStyleDecorator styleDecorator;

    public MyLockerHitCellView(DefaultStyleDecorator styleDecorator) {
        this.paint = CellUtils.createPaint();
        this.paint.setStyle(Paint.Style.FILL);
        this.styleDecorator = styleDecorator;
    }

    @Override
    public void draw(Canvas canvas, CellBean cellBean, boolean isError) {
        int saveCount = canvas.save();
        // draw outer circle 画外圆
        this.paint.setColor(this.getColor(isError));
        canvas.drawCircle(cellBean.getCenterX(), cellBean.getCenterY(), cellBean.getRadius(), this.paint);

        // draw fill circle 画填充圆
        this.paint.setColor(Color.GREEN);
        canvas.drawCircle(cellBean.getCenterX(), cellBean.getCenterY(), cellBean.getRadius() - this.styleDecorator.getLineWidth(), this.paint);


        // draw additional circles 画额外的圆。要注意大小，即先画大圆，再画小圆
        this.paint.setColor(Color.YELLOW);
        canvas.drawCircle(cellBean.getCenterX(), cellBean.getCenterY(), cellBean.getRadius() * 0.8f, this.paint);

        this.paint.setColor(Color.BLACK);
        canvas.drawCircle(cellBean.getCenterX(), cellBean.getCenterY(), cellBean.getRadius() * 0.6f, this.paint);

        // 可以继续添加更多颜色圈
        // ...

        // draw inner circle 画内部圆。放在最后画，覆盖前面的图案。
        this.paint.setColor(this.getColor(isError));
        float innerHitPercent = this.styleDecorator.getInnerHitPercent();
        canvas.drawCircle(cellBean.getCenterX(), cellBean.getCenterY(), cellBean.getRadius() * innerHitPercent, this.paint);
        canvas.restoreToCount(saveCount);
    }

    private int getColor(boolean isError) {
        return isError ? this.styleDecorator.getErrorColor() : this.styleDecorator.getClickColor();
    }
}
