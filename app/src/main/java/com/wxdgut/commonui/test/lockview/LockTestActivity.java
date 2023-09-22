package com.wxdgut.commonui.test.lockview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.wxdgut.commonui.R;
import com.wxdgut.commonui.test.BaseTestActivity;
import com.wxdgut.uilibrary.lockview.PatternIndicatorView;
import com.wxdgut.uilibrary.lockview.PatternLockerView;
import com.wxdgut.uilibrary.lockview.customized.MyLockerHitCellView;
import com.wxdgut.uilibrary.lockview.customized.MyLockerHitImgView;
import com.wxdgut.uilibrary.lockview.customized.MyLockerLineView;
import com.wxdgut.uilibrary.lockview.customized.MyLockerNormalCellView;
import com.wxdgut.uilibrary.lockview.customized.MyLockerNormalImgView;
import com.wxdgut.uilibrary.lockview.decorator.DefaultStyleDecorator;
import com.wxdgut.uilibrary.lockview.im.IHitCellView;
import com.wxdgut.uilibrary.lockview.im.ILockerLinkedLineView;
import com.wxdgut.uilibrary.lockview.im.INormalCellView;
import com.wxdgut.uilibrary.lockview.im.OnPatternChangeListener;

import java.util.List;

public class LockTestActivity extends BaseTestActivity {

    private PatternLockerView lockerView, lockerView2;
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
        lockerView2 = findViewById(R.id.lock_view2);
        indicator_view = findViewById(R.id.indicator_view);
        //设置指示器初始图案
        indicator_view.updateState(1, 2, 3);

        INormalCellView normalCellView = new MyLockerNormalCellView(lockerView.getStyleDecorator());
        lockerView.setNormalCellView(normalCellView).build();

        IHitCellView hitCellView = new MyLockerHitCellView(lockerView.getStyleDecorator());
        lockerView.setHitCellView(hitCellView).build();

        ILockerLinkedLineView view = new MyLockerLineView(lockerView.getStyleDecorator());
        lockerView.setLinkedLineView(view).build();

        DefaultStyleDecorator styleDecorator2 = lockerView2.getStyleDecorator();
        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.img_share);
        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.img_success);
        Bitmap bitmap3 = BitmapFactory.decodeResource(getResources(), R.drawable.img_remind);
        styleDecorator2.setNormalBitmap(bitmap1);
        styleDecorator2.setHitBitmap(bitmap2);
        styleDecorator2.setErrorBitmap(bitmap3);
        MyLockerNormalImgView normalImgView = new MyLockerNormalImgView(styleDecorator2);
        lockerView2.setNormalCellView(normalImgView).build();

        MyLockerHitImgView hitImgView = new MyLockerHitImgView(styleDecorator2);
        lockerView2.setHitCellView(hitImgView).build();

        ILockerLinkedLineView lineView = new MyLockerLineView(styleDecorator2);
        lockerView2.setLinkedLineView(lineView).build();
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

        lockerView2.setOnPatternChangedListener(new OnPatternChangeListener() {
            @Override
            public void onChange(PatternLockerView view, List<Integer> hitIndexList) {
                e("onChange");
            }

            @Override
            public void onComplete(PatternLockerView view, List<Integer> hitIndexList) {
                e("onComplete");
                if (hitIndexList.size() < 4) {
                    toast("手势密码不能少于4个点");
                    view.updateStatus(true);
                }
            }
        });
    }
}