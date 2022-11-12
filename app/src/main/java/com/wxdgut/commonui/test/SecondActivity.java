package com.wxdgut.commonui.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wxdgut.commonui.R;

public class SecondActivity extends BaseTestActivity implements View.OnClickListener {
    //存储参数
    private boolean flag;
    private String[] intentStr = null;
    //视图控件
    private Button btn_test;

    public static void startActivity(Activity mActivity, boolean flag, String... strings) {
        Intent intent = new Intent(mActivity, SecondActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtra("flag", flag);
        for (int i = 0; i < strings.length; i++) {
            bundle.putString("str" + i, strings[i]);
        }
        intent.putExtras(bundle);
        mActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        getIntentData();
        initView();
        initEvent();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        flag = intent.getBooleanExtra("flag", false);
        Bundle bundle = intent.getExtras();
        String str = "Bundle{\n";
        int size = bundle.keySet().size();
        int index = 1;
        if (intentStr == null) {
            intentStr = new String[size];
        }
        intentStr[0] = flag ? "true" : "false";
        for (String key : bundle.keySet()) {
            Object obj = bundle.get(key);
            if (obj == null) break;
            if (obj instanceof String) {
                intentStr[index++] = (String) obj;
            }
            str += key + " --> " + obj + ";\n";
        }
        str += "}\n";
        for (int i = 0; i < intentStr.length; i++) {
            e("intentStr[ " + i + " ] " + intentStr[i]);
        }
        e("getIntentData: \n" + str);
    }

    private void initView() {
        btn_test = findViewById(R.id.btn_test);
    }

    private void initEvent() {
        btn_test.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_test:
                toast("测试");
        }
    }
}