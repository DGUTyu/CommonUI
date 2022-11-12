package com.wxdgut.commonui.test;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wxdgut.commonui.R;

import java.text.SimpleDateFormat;

public class TestActivity extends BaseTestActivity implements View.OnClickListener {
    //视图控件
    private TextView tv_welcome;
    private Button btn1, btn2, btn3, btn4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initView();
        initEvent();
    }

    //初始化视图控件
    private void initView() {
        tv_welcome = findViewById(R.id.tv_welcome);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
    }

    //初始化事件
    private void initEvent() {
        tv_welcome.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_welcome:
                String date = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(System.currentTimeMillis());
                e(date + " onClick");
                TestLibrary.test("测试TestActivity");
                break;
            case R.id.btn1:
                //show("btn1");
                SecondActivity.startActivity(baseContext, false, "one", "two");
                break;
            case R.id.btn2:
                toast("btn2");
                break;
            case R.id.btn3:
                toast("btn3");
                break;
            case R.id.btn4:
                toast("btn4");
                break;
        }
    }
}