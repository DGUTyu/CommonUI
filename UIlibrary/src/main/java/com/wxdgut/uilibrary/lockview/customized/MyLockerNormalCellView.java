package com.wxdgut.uilibrary.lockview.customized;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.wxdgut.uilibrary.lockview.bean.CellBean;
import com.wxdgut.uilibrary.lockview.decorator.DefaultStyleDecorator;
import com.wxdgut.uilibrary.lockview.im.INormalCellView;
import com.wxdgut.uilibrary.lockview.utils.CellUtils;

public class MyLockerNormalCellView implements INormalCellView {

    private Paint paint;
    private DefaultStyleDecorator styleDecorator;

    public MyLockerNormalCellView(DefaultStyleDecorator styleDecorator) {
        this.paint = CellUtils.createPaint();
        this.paint.setStyle(Paint.Style.FILL);
        this.styleDecorator = styleDecorator;
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

        // draw additional circles 画额外的圆。要注意大小，即先画大圆，再画小圆
        this.paint.setColor(Color.BLUE);
        canvas.drawCircle(cellBean.getCenterX(), cellBean.getCenterY(), cellBean.getRadius() * 0.8f, this.paint);

        this.paint.setColor(Color.GRAY);
        canvas.drawCircle(cellBean.getCenterX(), cellBean.getCenterY(), cellBean.getRadius() * 0.6f, this.paint);

        // 可以继续添加更多颜色圈
        // ...

        // draw inner circle 画内部圆。放在最后画，覆盖前面的图案。
        this.paint.setColor(this.styleDecorator.getNormalInnerColor());
        float innerPercent = this.styleDecorator.getInnerPercent();
        canvas.drawCircle(cellBean.getCenterX(), cellBean.getCenterY(), cellBean.getRadius() * innerPercent, this.paint);

        canvas.restoreToCount(saveCount);
    }
}