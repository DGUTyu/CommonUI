package com.wxdgut.uilibrary.lockview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;

import com.wxdgut.uilibrary.R;
import com.wxdgut.uilibrary.lockview.im.IHitCellView;
import com.wxdgut.uilibrary.lockview.im.ILockerLinkedLineView;
import com.wxdgut.uilibrary.lockview.im.INormalCellView;
import com.wxdgut.uilibrary.lockview.im.OnPatternChangeListener;
import com.wxdgut.uilibrary.lockview.bean.CellBean;
import com.wxdgut.uilibrary.lockview.config.DefaultConfig;
import com.wxdgut.uilibrary.lockview.decorator.DefaultStyleDecorator;
import com.wxdgut.uilibrary.lockview.utils.CellUtils;
import com.wxdgut.uilibrary.lockview.widget.DefaultLockerHitCellView;
import com.wxdgut.uilibrary.lockview.widget.DefaultLockerLinkedLineView;
import com.wxdgut.uilibrary.lockview.widget.DefaultLockerNormalCellView;

import java.util.ArrayList;
import java.util.List;

public class PatternLockerView extends View {
    private Context mContext;
    //绘制完成后多久可以清除（单位ms），只有在@enableAutoClean = true 时有效
    int freezeDuration = 0;
    //绘制完后是否自动清除标志位，如果开启了该标志位，延时@freezeDuration毫秒后自动清除已绘制图案
    private boolean enableAutoClean = false;
    //是否开启触碰反馈，如果开启了该标志，则每连接一个cell则会震动
    boolean enableHapticFeedback = false;
    //能否跳过中间点标志位，如果开启了该标志，则可以不用连续
    boolean enableSkip = false;
    //是否线在圆形之上，默认为false
    boolean lineAtTop = false;

    //真正的cell数组
    private List<CellBean> cellBeanList;
    //
    private List<Integer> hitIndexList = new ArrayList<>();
    private TimeRunnable timeRunnable;
    // 是否可以滑动图案，避免异步清理图案后，一直滑动图案
    private boolean isEnabled = true;
    //记录绘制多少个cell，用于判断是否调用OnPatternChangeListener
    private int hitSize = 0;
    //是否是错误的图案
    private boolean isError = false;
    //监听器
    private OnPatternChangeListener listener = null;

    //绘制未操作时的cell样式
    private INormalCellView normalCellView = null;
    //绘制操作时的cell样式
    private IHitCellView hitCellView = null;
    //绘制连接线
    private ILockerLinkedLineView linkedLineView = null;

    //终点x坐标
    private float endX = 0f;

    //终点y坐标
    private float endY = 0f;
    private DefaultStyleDecorator styleDecorator = null;

    public PatternLockerView(Context context) {
        //super(context);
        this(context, null);
    }

    public PatternLockerView(Context context, @Nullable AttributeSet attrs) {
        //super(context, attrs);
        this(context, attrs, 0);
    }

    //主要重写此方法
    public PatternLockerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initData();
        //更换默认配置时，修改这里即可
        DefaultConfig config = DefaultConfig.newBuilder(mContext).defaultEnableHapticFeedback(true).defaultLineAtTop(false).build();
        init(attrs, defStyleAttr, config);
    }

    private void initData() {
        this.hitIndexList.clear();
        this.timeRunnable = new TimeRunnable();
    }

    public class TimeRunnable implements Runnable {

        @Override
        public void run() {
            isEnabled = true;
            clearHitState();
        }
    }

    //清除已绘制图案
    private void clearHitState() {
        this.clearHitData();
        this.isError = false;
        if (this.listener != null) {
            this.listener.onClear(this);
        }
        invalidate();
    }

    //
    private void clearHitData() {
        if (this.hitIndexList.size() > 0) {
            this.hitIndexList.clear();
            this.hitSize = 0;
            for (CellBean cellBean : this.cellBeanList) {
                cellBean.setHit(false);
            }
        }
    }

    private void init(AttributeSet attrs, int defStyleAttr, DefaultConfig config) {
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.PatternLockerView, defStyleAttr, 0);

        int rows = ta.getInteger(R.styleable.PatternLockerView_plv_rows, 3);
        int normalColor = ta.getColor(R.styleable.PatternLockerView_plv_color, config.getNormalColor());
        int normalInnerColor = ta.getColor(R.styleable.PatternLockerView_plv_innerColor, config.getHitColor());
        float innerPercent = ta.getFloat(R.styleable.PatternLockerView_plv_innerPercent, 0.5f);
        float innerHitPercent = ta.getFloat(R.styleable.PatternLockerView_plv_innerHitPercent, 0.5f);
        int hitColor = ta.getColor(R.styleable.PatternLockerView_plv_hitColor, config.getHitColor());
        int errorColor = ta.getColor(R.styleable.PatternLockerView_plv_errorColor, config.getErrorColor());
        int fillColor = ta.getColor(R.styleable.PatternLockerView_plv_fillColor, config.getFillColor());
        float lineWidth = ta.getDimension(R.styleable.PatternLockerView_plv_lineWidth, config.getLineWidth());

        this.freezeDuration = ta.getInteger(R.styleable.PatternLockerView_plv_freezeDuration, config.defaultFreezeDuration);
        this.enableAutoClean = ta.getBoolean(R.styleable.PatternLockerView_plv_enableAutoClean, config.defaultEnableAutoClean);
        this.enableHapticFeedback = ta.getBoolean(R.styleable.PatternLockerView_plv_enableHapticFeedback, config.defaultEnableHapticFeedback);
        this.enableSkip = ta.getBoolean(R.styleable.PatternLockerView_plv_enableSkip, config.defaultEnableSkip);
        this.lineAtTop = ta.getBoolean(R.styleable.PatternLockerView_plv_lineAtTop, config.defaultLineAtTop);

        int normalImgResId = ta.getResourceId(R.styleable.PatternLockerView_plv_normalImg, 0);
        int hitImgResId = ta.getResourceId(R.styleable.PatternLockerView_plv_hitImg, 0);
        int errorResId = ta.getResourceId(R.styleable.PatternLockerView_plv_errorImg, 0);
        ta.recycle();

        // style
        styleDecorator = new DefaultStyleDecorator(rows, normalColor, normalInnerColor, innerPercent, innerHitPercent, fillColor, hitColor, errorColor, lineWidth);
        //设置扩展属性
        setDefaultDecoratorExtend(normalImgResId, hitImgResId, errorResId);
        this.normalCellView = new DefaultLockerNormalCellView(styleDecorator);
        this.hitCellView = new DefaultLockerHitCellView(styleDecorator);
        this.linkedLineView = new DefaultLockerLinkedLineView(styleDecorator);
    }

    //设置默认样式的扩展属性
    private void setDefaultDecoratorExtend(int normalImgResId, int hitImgResId, int errorResId) {
        if (normalImgResId != 0) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), normalImgResId);
            styleDecorator.setNormalBitmap(bitmap);
        }
        if (hitImgResId != 0) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), hitImgResId);
            styleDecorator.setHitBitmap(bitmap);
        }
        if (errorResId != 0) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), errorResId);
            styleDecorator.setErrorBitmap(bitmap);
        }
    }

    //******************** 以下是公开方法 ********************
    //设置
    public void setEnableAutoClean(boolean enableAutoClean) {
        this.enableAutoClean = enableAutoClean;
    }

    //更改状态
    public void updateStatus(boolean isError) {
        this.isError = isError;
        invalidate();
    }
    //******************** 以上是公开方法 ********************

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 取最小值，保证是正方形
        int a = Math.min(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(a, a);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.initCellBeanList();
        if (lineAtTop) {
            this.drawCells(canvas);
            this.drawLinkedLine(canvas);
        } else {
            this.drawLinkedLine(canvas);
            this.drawCells(canvas);
        }
    }

    //初始化Cells
    private void initCellBeanList() { //可抽取
        if (this.cellBeanList == null) {
            int w = this.getWidth() - this.getPaddingLeft() - this.getPaddingRight();
            int h = this.getHeight() - this.getPaddingTop() - this.getPaddingBottom();
            this.cellBeanList = CellUtils.buildCells(w, h, getStyleDecorator().getRows());
        }
    }

    //绘制线条
    private void drawLinkedLine(Canvas canvas) {
        if (this.hitIndexList.size() > 0) {
            this.linkedLineView.draw(canvas,
                    this.hitIndexList,
                    this.cellBeanList,
                    this.endX,
                    this.endY,
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

    @Override
    protected void onDetachedFromWindow() {
        //super.onDetachedFromWindow();
        this.setOnPatternChangedListener(null);
        this.removeCallbacks(this.timeRunnable);
        super.onDetachedFromWindow();
    }

    //设置监听器
    public void setOnPatternChangedListener(OnPatternChangeListener listener) {
        this.listener = listener;
    }

    //重写onTouchEvent方法
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //return super.onTouchEvent(event);
        if (!this.isEnabled) {
            return super.onTouchEvent(event);
        }
        boolean isHandle = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.handleActionDown(event);
                isHandle = true;
                break;
            case MotionEvent.ACTION_MOVE:
                this.handleActionMove(event);
                isHandle = true;
                break;
            case MotionEvent.ACTION_UP:
                this.handleActionUp(event);
                isHandle = true;
                break;
        }
        invalidate();
        return isHandle || super.onTouchEvent(event);
    }

    //处理Down事件
    private void handleActionDown(MotionEvent event) {
        //1. reset to default state
        this.clearHitData();

        //2. update hit state
        this.updateHitState(event);

        //3. notify listener
        if (this.listener != null) {
            this.listener.onStart(this);
        }
    }

    //更新点击的状态
    private void updateHitState(MotionEvent event) {
        // 遍历所有的 CellBean
        for (CellBean targetCell : this.cellBeanList) {
            // 判断是否未被击中且被点击到
            if (!targetCell.isHit() && targetCell.of(event.getX(), event.getY())) {
                // 如果不允许跳过并且已经有被击中的 CellBean
                if (!enableSkip && this.hitIndexList.size() > 0) {
                    // 获取上一个被击中的 CellBean
                    CellBean last = this.cellBeanList.get(this.hitIndexList.get(this.hitIndexList.size() - 1));

                    // 计算最小矩形区域的边界。两点连线为直线时，为矩形；两点连线为斜线时，为正方形；
                    float startX = Math.min(targetCell.getCenterX(), last.getCenterX());
                    float endX = Math.max(targetCell.getCenterX(), last.getCenterX());
                    float startY = Math.min(targetCell.getCenterY(), last.getCenterY());
                    float endY = Math.max(targetCell.getCenterY(), last.getCenterY());

                    // 遍历所有的 CellBean
                    for (CellBean cell : this.cellBeanList) {
                        // 排除当前 CellBean 以及上一个被点击的 CellBean
                        if (cell.getId() != targetCell.getId() && cell.getId() != last.getId()) {
                            float centerX = cell.getCenterX();
                            float centerY = cell.getCenterY();

                            // 前提：判断该点是否在两点之间的连线上（直线）
                            if (isPointOnLine(last.getCenterX(), last.getCenterY(), targetCell.getCenterX(), targetCell.getCenterY(), cell.getCenterX(), cell.getCenterY())) {
                                // 再判断该点是否在最小矩形区域内。
                                if (centerX >= startX && centerX <= endX && centerY >= startY && centerY <= endY) {
                                    cell.setHit(true);
                                    this.hitIndexList.add(cell.getId());
                                }
                            }
                        }
                    }
                }

                // 设置当前 CellBean 为被击中状态
                targetCell.setHit(true);
                this.hitIndexList.add(targetCell.getId());
                this.hapticFeedback();
            }
        }
    }


    /**
     * 判断点是否在直线上
     *
     * @param x1 直线上的第一个点的x坐标
     * @param y1 直线上的第一个点的y坐标
     * @param x2 直线上的第二个点的x坐标
     * @param y2 直线上的第二个点的y坐标
     * @param cx 待判断的点的x坐标
     * @param cy 待判断的点的y坐标
     * @return 是否在直线上
     */
    private boolean isPointOnLine(float x1, float y1, float x2, float y2, float cx, float cy) {
        // 计算直线的一般形式 Ax + By + C = 0 的系数 A, B, C
        float A = y2 - y1;
        float B = x1 - x2;
        float C = x2 * y1 - x1 * y2;
        // 计算点到直线的距离
        float distance = Math.abs(A * cx + B * cy + C) / (float) Math.sqrt(A * A + B * B);
        // 这里的0.1可以根据实际情况进行调整
        return distance < 0.1;
    }

    //
    private void hapticFeedback() {
        if (this.enableHapticFeedback) {
            this.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY,
                    HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING);
        }
    }

    //处理Move事件
    private void handleActionMove(MotionEvent event) {
        //1. update hit state
        this.updateHitState(event);

        //2. update end point
        this.endX = event.getX();
        this.endY = event.getY();

        //3. notify listener if needed
        int size = this.hitIndexList.size();
        if (this.hitSize != size) {
            this.hitSize = size;
            if (this.listener != null) {
                this.listener.onChange(this, this.hitIndexList);
            }
        }
    }

    //处理UP事件
    private void handleActionUp(MotionEvent event) {
        //1. update hit state
        this.updateHitState(event);

        //2. update end point
        this.endX = 0f;
        this.endY = 0f;

        //3. notify listener
        if (this.listener != null) {
            this.listener.onComplete(this, this.hitIndexList);
        }

        //4. startTimer if needed
        if (this.enableAutoClean && this.hitIndexList.size() > 0) {
            this.startTimer();
        }
    }

    //开始计时
    private void startTimer() {
        isEnabled = false;
        this.postDelayed(timeRunnable, this.freezeDuration);
    }

    public PatternLockerView setStyleDecorator(DefaultStyleDecorator styleDecorator) {
        this.styleDecorator = styleDecorator;
        return this;
    }

    public DefaultStyleDecorator getStyleDecorator() {
        return styleDecorator;
    }

    public PatternLockerView setNormalCellView(INormalCellView normalCellView) {
        this.normalCellView = normalCellView;
        return this;
    }

    public INormalCellView getNormalCellView() {
        return normalCellView;
    }

    public PatternLockerView setHitCellView(IHitCellView hitCellView) {
        this.hitCellView = hitCellView;
        return this;
    }

    public IHitCellView getHitCellView() {
        return hitCellView;
    }

    public PatternLockerView setLinkedLineView(ILockerLinkedLineView linkedLineView) {
        this.linkedLineView = linkedLineView;
        return this;
    }

    public ILockerLinkedLineView getLinkedLineView() {
        return linkedLineView;
    }

    public boolean isLineAtTop() {
        return lineAtTop;
    }

    public void setLineAtTop(boolean lineAtTop) {
        this.lineAtTop = lineAtTop;
    }

    public void build() {
        build(lineAtTop);
    }

    public void build(boolean lineAtTop) {
        setLineAtTop(lineAtTop);
        if (this.getNormalCellView() == null) {
            //Log.e("PatternLockerView", "build(), normalCellView is null");
        } else if (this.getHitCellView() == null) {
            //Log.e("PatternLockerView", "build(), hitCellView is null");
        } else {
            if (this.getLinkedLineView() == null) {
                //Log.w("PatternLockerView", "build(), linkedLineView is null");
            }
            this.postInvalidate();
        }
    }
}
