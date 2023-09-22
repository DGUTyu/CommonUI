package com.wxdgut.uilibrary.lockview.customized;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.wxdgut.uilibrary.R;
import com.wxdgut.uilibrary.lockview.bean.CellBean;
import com.wxdgut.uilibrary.lockview.decorator.DefaultStyleDecorator;
import com.wxdgut.uilibrary.lockview.im.INormalCellView;
import com.wxdgut.uilibrary.lockview.utils.CellUtils;

public class MyLockerNormalImgView implements INormalCellView {

    private Paint paint;
    private DefaultStyleDecorator styleDecorator;
    // 缓存图片资源
    private Bitmap bitmap;

    public MyLockerNormalImgView(DefaultStyleDecorator styleDecorator) {
        this.paint = CellUtils.createPaint();
        this.paint.setStyle(Paint.Style.FILL);
        this.styleDecorator = styleDecorator;
        this.bitmap = styleDecorator.getNormalBitmap();
    }

    @Override
    public void draw(Canvas canvas, CellBean cellBean) {
        int saveCount = canvas.save();

        // 获取小正方形的边长和中心坐标
        float sideLength = cellBean.getRadius() * 2; // 小正方形的边长为2倍的radius
        float centerX = cellBean.getCenterX();
        float centerY = cellBean.getCenterY();

        // 如果图片未加载，进行加载
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(20, 20, Bitmap.Config.ARGB_8888);
            bitmap.eraseColor(Color.parseColor("#7BC0FC"));
        }

        // 计算绘制图片时的矩形区域
        RectF rect = new RectF(centerX - sideLength / 2, centerY - sideLength / 2, centerX + sideLength / 2, centerY + sideLength / 2);

        // 绘制Bitmap
        canvas.drawBitmap(bitmap, null, rect, null);

        canvas.restoreToCount(saveCount);
    }
}