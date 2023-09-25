package com.wxdgut.uilibrary.lockview.widget;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.wxdgut.uilibrary.lockview.bean.CellBean;
import com.wxdgut.uilibrary.lockview.im.IHitCellView;
import com.wxdgut.uilibrary.lockview.decorator.DefaultStyleDecorator;
import com.wxdgut.uilibrary.lockview.utils.CellUtils;

public class DefaultLockerHitCellView implements IHitCellView {

    private Paint paint;
    private DefaultStyleDecorator styleDecorator;

    public DefaultLockerHitCellView(DefaultStyleDecorator styleDecorator) {
        this.paint = CellUtils.createPaint();
        this.paint.setStyle(Paint.Style.FILL);
        this.styleDecorator = styleDecorator;
    }

    @Override
    public void draw(Canvas canvas, CellBean cellBean, boolean isError) {
        int saveCount = canvas.save();

        Bitmap bitmap = getBitmap(isError);
        if (bitmap != null) {
            // 获取小正方形的边长和中心坐标
            float sideLength = cellBean.getRadius() * 2;
            float centerX = cellBean.getCenterX();
            float centerY = cellBean.getCenterY();
            // 计算绘制图片时的矩形区域
            RectF rect = new RectF(centerX - sideLength / 2, centerY - sideLength / 2, centerX + sideLength / 2, centerY + sideLength / 2);
            // 绘制Bitmap
            canvas.drawBitmap(bitmap, null, rect, null);
        } else {
            // draw outer circle 画外圆
            this.paint.setColor(this.getColor(isError));
            canvas.drawCircle(cellBean.getCenterX(), cellBean.getCenterY(), cellBean.getRadius(), this.paint);

            // draw fill circle 画填充圆
            this.paint.setColor(this.styleDecorator.getFillColor());
            canvas.drawCircle(cellBean.getCenterX(), cellBean.getCenterY(), cellBean.getRadius() - this.styleDecorator.getLineWidth(), this.paint);

            // draw inner circle 画内部圆
            this.paint.setColor(this.getColor(isError));
            float innerHitPercent = this.styleDecorator.getInnerHitPercent();
            canvas.drawCircle(cellBean.getCenterX(), cellBean.getCenterY(), cellBean.getRadius() * innerHitPercent, this.paint);
        }

        canvas.restoreToCount(saveCount);
    }

    private int getColor(boolean isError) {
        return isError ? this.styleDecorator.getErrorColor() : this.styleDecorator.getHitColor();
    }

    private Bitmap getBitmap(boolean isError) {
        return isError ? styleDecorator.getErrorBitmap() : styleDecorator.getHitBitmap();
    }
}
