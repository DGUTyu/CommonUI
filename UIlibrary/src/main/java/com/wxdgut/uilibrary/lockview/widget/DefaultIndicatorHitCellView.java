package com.wxdgut.uilibrary.lockview.widget;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.wxdgut.uilibrary.lockview.bean.CellBean;
import com.wxdgut.uilibrary.lockview.im.IHitCellView;
import com.wxdgut.uilibrary.lockview.decorator.DefaultStyleDecorator;
import com.wxdgut.uilibrary.lockview.utils.CellUtils;

public class DefaultIndicatorHitCellView implements IHitCellView {

    private Paint paint;
    private DefaultStyleDecorator styleDecorator;

    public DefaultIndicatorHitCellView(DefaultStyleDecorator styleDecorator) {
        this.paint = CellUtils.createPaint();
        this.paint.setStyle(Paint.Style.FILL);
        this.styleDecorator = styleDecorator;
    }

    @Override
    public void draw(Canvas canvas, CellBean cellBean, boolean isError) {
        int saveCount = canvas.save();
        this.paint.setColor(this.getColor(isError));
        canvas.drawCircle(cellBean.getCenterX(), cellBean.getCenterY(), cellBean.getRadius(), this.paint);
        canvas.restoreToCount(saveCount);
    }

    private int getColor(boolean isError) {
        return isError ? this.styleDecorator.getErrorColor() : this.styleDecorator.getHitColor();
    }
}