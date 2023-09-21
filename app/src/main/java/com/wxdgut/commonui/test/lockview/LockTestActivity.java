package com.wxdgut.commonui.test.lockview;

import android.os.Bundle;

import com.wxdgut.commonui.R;
import com.wxdgut.commonui.test.BaseTestActivity;
import com.wxdgut.uilibrary.lockview.PatternIndicatorView;
import com.wxdgut.uilibrary.lockview.PatternLockerView;
import com.wxdgut.uilibrary.lockview.customized.MyLockerHitCellView;
import com.wxdgut.uilibrary.lockview.customized.MyLockerLineView;
import com.wxdgut.uilibrary.lockview.customized.MyLockerNormalCellView;
import com.wxdgut.uilibrary.lockview.im.IHitCellView;
import com.wxdgut.uilibrary.lockview.im.ILockerLinkedLineView;
import com.wxdgut.uilibrary.lockview.im.INormalCellView;
import com.wxdgut.uilibrary.lockview.im.OnPatternChangeListener;

import java.util.List;

public class LockTestActivity extends BaseTestActivity {

    private PatternLockerView lockerView;
    private PatternIndicatorView indicator_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_test);
        initView();
        initEvent();
    }

    private void initView() {
        lockerView = findViewById(R.id.lock_view);
        indicator_view = findViewById(R.id.indicator_view);
        //设置指示器初始图案
        indicator_view.updateState(1, 2, 3);

        INormalCellView normalCellView = new MyLockerNormalCellView(lockerView.getStyleDecorator());
        lockerView.setNormalCellView(normalCellView).build();

        IHitCellView hitCellView = new MyLockerHitCellView(lockerView.getStyleDecorator());
        lockerView.setHitCellView(hitCellView).build();

        ILockerLinkedLineView view = new MyLockerLineView(lockerView.getStyleDecorator());
        lockerView.setLinkedLineView(view).build();
    }

    private void initEvent() {
        lockerView.setOnPatternChangedListener(new OnPatternChangeListener() {
            @Override
            public void onChange(PatternLockerView view, List<Integer> hitIndexList) {
                e("onChange");
            }

            @Override
            public void onComplete(PatternLockerView view, List<Integer> hitIndexList) {
                e("onComplete");
                indicator_view.updateState(hitIndexList);
                if (hitIndexList.size() < 4) {
                    toast("手势密码不能少于4个点");
                    view.updateStatus(true);
                }
            }
        });
    }
}