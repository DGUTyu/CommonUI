package com.wxdgut.uilibrary.lockview.widget;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.wxdgut.uilibrary.lockview.bean.CellBean;
import com.wxdgut.uilibrary.lockview.im.INormalCellView;
import com.wxdgut.uilibrary.lockview.decorator.DefaultStyleDecorator;
import com.wxdgut.uilibrary.lockview.utils.CellUtils;

public class DefaultLockerNormalCellView implements INormalCellView {

    private Paint paint;
    private DefaultStyleDecorator styleDecorator;

    public DefaultLockerNormalCellView(DefaultStyleDecorator styleDecorator) {
        this.paint = CellUtils.createPaint();
        this.paint.setStyle(Paint.Style.FILL);
        this.styleDecorator = styleDecorator;
    }

    @Override
    public void draw(Canvas canvas, CellBean cellBean) {
        int saveCount = canvas.save();

        Bitmap bitmap = styleDecorator.getNormalBitmap();
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
            // draw outer circle
            this.paint.setColor(this.styleDecorator.getNormalColor());
            canvas.drawCircle(cellBean.getCenterX(), cellBean.getCenterY(), cellBean.getRadius(), this.paint);

            // draw fill circle
            this.paint.setColor(this.styleDecorator.getFillColor());
            canvas.drawCircle(cellBean.getCenterX(), cellBean.getCenterY(), cellBean.getRadius() - this.styleDecorator.getLineWidth(), this.paint);

            // draw inner circle 画内部圆
            this.paint.setColor(this.styleDecorator.getNormalInnerColor());
            float innerPercent = this.styleDecorator.getInnerPercent();
            canvas.drawCircle(cellBean.getCenterX(), cellBean.getCenterY(), cellBean.getRadius() * innerPercent, this.paint);
        }

        canvas.restoreToCount(saveCount);
    }
}