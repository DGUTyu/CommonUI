package com.wxdgut.uilibrary.lockview.customized;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import com.wxdgut.uilibrary.lockview.bean.CellBean;
import com.wxdgut.uilibrary.lockview.decorator.DefaultStyleDecorator;
import com.wxdgut.uilibrary.lockview.im.ILockerLinkedLineView;
import com.wxdgut.uilibrary.lockview.utils.CellUtils;
import com.wxdgut.uilibrary.utils.CommonUtils;

import java.util.List;


public class MyLockerLineView implements ILockerLinkedLineView {

    private Paint paint;
    private DefaultStyleDecorator styleDecorator;
    //连接线颜色、宽度（dp）
    private int linkColor = -1;
    private int linkErrorColor = -1;
    private int linkWidth = -1;

    public MyLockerLineView(DefaultStyleDecorator styleDecorator) {
        this.paint = CellUtils.createPaint();
        this.paint.setStyle(Paint.Style.STROKE);
        this.styleDecorator = styleDecorator;
        //getLinkColor、getLinkErrorColor、getLinkWidth 默认值为0
        this.linkColor = styleDecorator.getLinkColor();
        this.linkErrorColor = styleDecorator.getLinkErrorColor();
        this.linkWidth = styleDecorator.getLinkWidth();
    }

    @Override
    public void draw(Canvas canvas, List<Integer> hitIndexList, List<CellBean> cellBeanList, float endX, float endY, boolean isError) {
        if (hitIndexList.isEmpty() || cellBeanList.isEmpty()) {
            return;
        }
        int saveCount = canvas.save();
        Path path = new Path();
        boolean first = true;

        for (int i = 0; i < hitIndexList.size(); i++) {
            // 记录选中的点id
            Integer it = hitIndexList.get(i);
            if (0 <= it && it < cellBeanList.size()) {
                // 获取选中的点
                CellBean c = cellBeanList.get(it);
                if (first) {
                    path.reset();
                    // 如果是第一个，则先将起点移动到第一个点
                    path.moveTo(c.getCenterX(), c.getCenterY());
                    first = false;
                } else {
                    // 连接第一个点到第二个点
                    path.lineTo(c.getCenterX(), c.getCenterY());
                }
            }
        }

        if ((endX != 0f || endY != 0f) && hitIndexList.size() < 9) {
            path.lineTo(endX, endY);
        }

        this.paint.setColor(getColor(isError));
        this.paint.setStrokeWidth(getLinkWidth());
        canvas.drawPath(path, this.paint);
        canvas.restoreToCount(saveCount);
    }

    private int getColor(boolean isError) {
        if (linkColor == 0) {
            linkColor = styleDecorator.getHitColor();
        }
        if (linkErrorColor == 0) {
            linkErrorColor = styleDecorator.getErrorColor();
        }
        return isError ? linkErrorColor : linkColor;
    }

    private int getLinkWidth() {
        if (styleDecorator.getLinkWidth() == 0) {
            linkWidth = Math.round(styleDecorator.getLineWidth());
            return linkWidth;
        }
        return CommonUtils.dpToPx(linkWidth);
    }
}