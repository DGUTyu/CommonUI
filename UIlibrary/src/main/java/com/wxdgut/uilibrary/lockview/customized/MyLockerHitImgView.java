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
import com.wxdgut.uilibrary.lockview.im.IHitCellView;
import com.wxdgut.uilibrary.lockview.utils.CellUtils;


public class MyLockerHitImgView implements IHitCellView {

    private Paint paint;
    private DefaultStyleDecorator styleDecorator;
    // 缓存图片资源
    private Bitmap hitBitmap;
    private Bitmap errorBitmap;

    public MyLockerHitImgView(DefaultStyleDecorator styleDecorator) {
        this.paint = CellUtils.createPaint();
        this.paint.setStyle(Paint.Style.FILL);
        this.styleDecorator = styleDecorator;
        this.hitBitmap = styleDecorator.getHitBitmap();
        this.errorBitmap = styleDecorator.getErrorBitmap();
    }

    @Override
    public void draw(Canvas canvas, CellBean cellBean, boolean isError) {
        //保存当前的绘图状态
        int saveCount = canvas.save();

        // 获取小正方形的边长和中心坐标
        float sideLength = cellBean.getRadius() * 2;
        float centerX = cellBean.getCenterX();
        float centerY = cellBean.getCenterY();
        // 计算绘制图片时的矩形区域
        RectF rect = new RectF(centerX - sideLength / 2, centerY - sideLength / 2, centerX + sideLength / 2, centerY + sideLength / 2);
        // 绘制Bitmap
        canvas.drawBitmap(getBitmap(isError), null, rect, null);

        //恢复之前的状态，以保证不会影响到其他的绘图操作
        canvas.restoreToCount(saveCount);
    }

    private Bitmap getBitmap(boolean isError) {
        // 如果图片未加载，进行加载
        if (hitBitmap == null) {
            hitBitmap = Bitmap.createBitmap(20, 20, Bitmap.Config.ARGB_8888);
            hitBitmap.eraseColor(Color.parseColor("#57A9FB"));
        }
        if (errorBitmap == null) {
            errorBitmap = Bitmap.createBitmap(20, 20, Bitmap.Config.ARGB_8888);
            errorBitmap.eraseColor(Color.parseColor("#ED6969"));
        }
        return isError ? errorBitmap : hitBitmap;
    }
}
