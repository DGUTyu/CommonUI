package com.wxdgut.commonui.test.popup;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wxdgut.commonui.R;
import com.wxdgut.commonui.test.BaseTestActivity;
import com.wxdgut.uilibrary.popup.CommonPopup;

public class PopupTestActivity extends BaseTestActivity implements View.OnClickListener {
    private LinearLayout ll_root;
    private TextView tv_pop;
    private Button btn1, btn2, btn3, btn4;
    private CommonPopup commonPopup;
    private EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_test);
        initView();
        initListener();
    }

    private void initView() {
        ll_root = findViewById(R.id.ll_root);
        et = findViewById(R.id.et);
        tv_pop = findViewById(R.id.tv_pop);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        initPopu();
    }

    private void initPopu() {
        commonPopup = CommonPopup.newInstance().setContentView(PopupTestActivity.this, R.layout.popup_layout)
                .setBackgroundDimEnable(true)
                .setDimValue(0.2f)
                .setFocusAndOutsideEnable(true)
                .setOutsideTouchable(true)
                //.setDimColor(Color.BLACK)
                //.setDimValue(0.5f)
                .setAnchorView(btn3)
                .setCancelListener(new CommonPopup.CancelListener() {
                    @Override
                    public void afterCancel(int cancelType) {
                        toast("取消");
                    }
                })
                .apply();
        //任意地方弹出
        commonPopup.showEverywhereInView(PopupTestActivity.this, ll_root);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initListener() {
        tv_pop.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                toast("1");
                break;
            case R.id.btn2:
                toast("2");
                break;
            case R.id.btn3:
                toast("3");
                break;
            case R.id.btn4:
                initPopu();
                commonPopup.showAsDropDown();
                break;
            case R.id.tv_pop:
                toast("tv_pop");
                break;
            case R.id.et:
                break;
        }
    }
}
