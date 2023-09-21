package com.wxdgut.uilibrary.lockview;

import android.content.Context;
import android.content.res.TypedArray;
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
    INormalCellView normalCellView = null;
    //绘制操作时的cell样式
    IHitCellView hitCellView = null;
    //绘制连接线
    ILockerLinkedLineView linkedLineView = null;

    //终点x坐标
    private float endX = 0f;

    //终点y坐标
    private float endY = 0f;

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
        DefaultConfig config = DefaultConfig.newBuilder(mContext).defaultEnableHapticFeedback(true).build();
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

        int normalColor = ta.getColor(R.styleable.PatternLockerView_plv_color, config.getNormalColor());
        int normalInnerColor = ta.getColor(R.styleable.PatternLockerView_plv_innerColor, config.getClickColor());
        float innerPercent = ta.getFloat(R.styleable.PatternLockerView_plv_innerPercent, 0.5f);
        float innerHitPercent = ta.getFloat(R.styleable.PatternLockerView_plv_innerHitPercent, 0.5f);
        int hitColor = ta.getColor(R.styleable.PatternLockerView_plv_hitColor, config.getClickColor());
        int errorColor = ta.getColor(R.styleable.PatternLockerView_plv_errorColor, config.getErrorColor());
        int fillColor = ta.getColor(R.styleable.PatternLockerView_plv_fillColor, config.getFillColor());
        float lineWidth = ta.getDimension(R.styleable.PatternLockerView_plv_lineWidth, config.getLineWidth());

        this.freezeDuration = ta.getInteger(R.styleable.PatternLockerView_plv_freezeDuration, config.defaultFreezeDuration);
        this.enableAutoClean = ta.getBoolean(R.styleable.PatternLockerView_plv_enableAutoClean, config.defaultEnableAutoClean);
        this.enableHapticFeedback = ta.getBoolean(R.styleable.PatternLockerView_plv_enableHapticFeedback, config.defaultEnableHapticFeedback);
        this.enableSkip = ta.getBoolean(R.styleable.PatternLockerView_plv_enableSkip, config.defaultEnableSkip);

        ta.recycle();

        // style
        DefaultStyleDecorator styleDecorator = new DefaultStyleDecorator(normalColor, normalInnerColor, innerPercent, innerHitPercent, fillColor, hitColor, errorColor, lineWidth);
        this.normalCellView = new DefaultLockerNormalCellView(styleDecorator);
        this.hitCellView = new DefaultLockerHitCellView(styleDecorator);
        this.linkedLineView = new DefaultLockerLinkedLineView(styleDecorator);
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
        for (CellBean it : this.cellBeanList) {
            // 如果没有被点击过并且点击到了圆
            if (!it.isHit() && it.of(event.getX(), event.getY())) {
                if (!enableSkip && this.hitIndexList.size() > 0) {
                    CellBean last = this.cellBeanList.get(this.hitIndexList.get(this.hitIndexList.size() - 1));
                    int mayId = (last.getId() + it.getId()) / 2;
                    if (!this.hitIndexList.contains(mayId) && (Math.abs(last.getX() - it.getX()) % 2 == 0) && (Math.abs(last.getY() - it.getY()) % 2 == 0)) {
                        this.cellBeanList.get(mayId).setHit(true);
                        this.hitIndexList.add(mayId);
                    }
                }
                it.setHit(true);
                this.hitIndexList.add(it.getId());
                this.hapticFeedback();
            }
        }
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
}
