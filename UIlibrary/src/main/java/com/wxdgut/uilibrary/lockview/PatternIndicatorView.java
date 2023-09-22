package com.wxdgut.uilibrary.lockview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.wxdgut.uilibrary.R;
import com.wxdgut.uilibrary.lockview.bean.CellBean;
import com.wxdgut.uilibrary.lockview.config.DefaultConfig;
import com.wxdgut.uilibrary.lockview.decorator.DefaultStyleDecorator;
import com.wxdgut.uilibrary.lockview.utils.CellUtils;
import com.wxdgut.uilibrary.lockview.widget.DefaultIndicatorHitCellView;
import com.wxdgut.uilibrary.lockview.widget.DefaultIndicatorLinkedLineView;
import com.wxdgut.uilibrary.lockview.widget.DefaultIndicatorNormalCellView;

import java.util.ArrayList;
import java.util.List;

public class PatternIndicatorView extends View {
    private Context mContext;
    private DefaultIndicatorNormalCellView normalCellView;
    private DefaultIndicatorHitCellView hitCellView;
    private DefaultIndicatorLinkedLineView linkedLineView;

    private boolean isError = false;
    private List hitIndexList = new ArrayList<Integer>();
    private List<CellBean> cellBeanList;

    public PatternIndicatorView(Context context) {
        //super(context);
        this(context, null);
    }

    public PatternIndicatorView(Context context, @Nullable AttributeSet attrs) {
        //super(context, attrs);
        this(context, attrs, 0);
    }

    //主要重写该方法
    public PatternIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        //更换默认配置时，修改这里即可
        DefaultConfig config = DefaultConfig.newBuilder(mContext).build();
        init(attrs, defStyleAttr, config);
    }

    private void init(AttributeSet attrs, int defStyleAttr, DefaultConfig config) {
        final TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.PatternIndicatorView, defStyleAttr, 0);
        int normalColor = ta.getColor(R.styleable.PatternIndicatorView_piv_color, config.getNormalColor());
        int normalInnerColor = ta.getColor(R.styleable.PatternIndicatorView_piv_innerColor, config.getNormalColor());
        float innerPercent = ta.getFloat(R.styleable.PatternIndicatorView_piv_innerPercent, 0.5f);
        float innerHitPercent = ta.getFloat(R.styleable.PatternIndicatorView_piv_innerHitPercent, 0.5f);
        int fillColor = ta.getColor(R.styleable.PatternIndicatorView_piv_fillColor, config.getFillColor());
        int hitColor = ta.getColor(R.styleable.PatternIndicatorView_piv_hitColor, config.getHitColor());
        int errorColor = ta.getColor(R.styleable.PatternIndicatorView_piv_errorColor, config.getErrorColor());
        float lineWidth = ta.getDimension(R.styleable.PatternIndicatorView_piv_lineWidth, config.getLineWidth());

        ta.recycle();

        DefaultStyleDecorator decorator = new DefaultStyleDecorator(normalColor, normalInnerColor, innerPercent, innerHitPercent, fillColor, hitColor, errorColor, lineWidth);
        this.normalCellView = new DefaultIndicatorNormalCellView(decorator); // 正常布局样式
        this.hitCellView = new DefaultIndicatorHitCellView(decorator); // 点击后布局样式
        this.linkedLineView = new DefaultIndicatorLinkedLineView(decorator);
    }

    //******************** 以下是公开方法 ********************
    public void updateState(boolean isError, int... hits) {
        ArrayList<Integer> integers = new ArrayList<>();
        for (int i = 0; i < hits.length; i++) {
            if (hits[i] > 0 && hits[i] < 10) {
                integers.add(hits[i] - 1);
            }
        }
        this.hitIndexList = integers;
        this.isError = isError;
        invalidate();
    }

    public void updateState(int... hits) {
        updateState(false, hits);
    }


    public void updateState(boolean isError, List<Integer> hitIndexList) {
        this.hitIndexList = hitIndexList;
        this.isError = isError;
        invalidate();
    }

    public void updateState(List<Integer> hitIndexList) {
        updateState(false, hitIndexList);
    }
    //******************** 以上是公开方法 ********************

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int a = Math.min(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(a, a);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initCellBeanList();
        this.updateHitState();
        this.drawLinkedLine(canvas);
        this.drawCells(canvas);
    }

    //初始化Cells
    private void initCellBeanList() { //可抽取
        if (this.cellBeanList == null) {
            int w = this.getWidth() - this.getPaddingLeft() - this.getPaddingRight();
            int h = this.getHeight() - this.getPaddingTop() - this.getPaddingBottom();
            this.cellBeanList = CellUtils.buildCells(w, h);
        }
    }

    //
    private void updateHitState() {
        //1. clear pre state
        for (CellBean cellBean : this.cellBeanList) {
            cellBean.setHit(false);
        }
        for (int i = 0; i < this.hitIndexList.size(); i++) {
            //2. update hit state
            Integer it = (Integer) this.hitIndexList.get(i);
            if (0 <= it && it < this.cellBeanList.size()) {
                this.cellBeanList.get(it).setHit(true);
            }

        }
    }

    //
    private void drawLinkedLine(Canvas canvas) {
        if (this.hitIndexList.size() > 0) {
            this.linkedLineView.draw(canvas,
                    this.hitIndexList,
                    this.cellBeanList,
                    this.isError);
        }
    }

    //绘制Cells
    private void drawCells(Canvas canvas) { //可抽取
        for (CellBean cellBean : this.cellBeanList) {
            if (cellBean.isHit() && this.hitCellView != null) {
                this.hitCellView.draw(canvas, cellBean, this.isError);
            } else {
                this.normalCellView.draw(canvas, cellBean);
            }
        }
    }
}
