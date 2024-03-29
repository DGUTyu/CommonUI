package com.wxdgut.uilibrary.lockview.widget;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.wxdgut.uilibrary.lockview.bean.CellBean;
import com.wxdgut.uilibrary.lockview.im.INormalCellView;
import com.wxdgut.uilibrary.lockview.decorator.DefaultStyleDecorator;
import com.wxdgut.uilibrary.lockview.utils.CellUtils;

public class DefaultIndicatorNormalCellView implements INormalCellView {

    private Paint paint;
    private DefaultStyleDecorator styleDecorator;

    public DefaultIndicatorNormalCellView(DefaultStyleDecorator styleDecorator) {
        this.paint = CellUtils.createPaint();
        this.paint.setStyle(Paint.Style.FILL);
        this.styleDecorator = styleDecorator;
    }

    @Override
    public void draw(Canvas canvas, CellBean cellBean) {
        int saveCount = canvas.save();
        //outer circle
        this.paint.setColor(this.styleDecorator.getNormalColor());
        canvas.drawCircle(cellBean.getCenterX(), cellBean.getCenterY(), cellBean.getRadius(), this.paint);

        //inner circle
        this.paint.setColor(this.styleDecorator.getFillColor());
        canvas.drawCircle(cellBean.getCenterX(), cellBean.getCenterY(), cellBean.getRadius() - this.styleDecorator.getLineWidth(), this.paint);
        canvas.restoreToCount(saveCount);
    }
}