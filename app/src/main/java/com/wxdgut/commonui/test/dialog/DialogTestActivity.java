package com.wxdgut.commonui.test.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wxdgut.commonui.R;
import com.wxdgut.commonui.test.BaseTestActivity;
import com.wxdgut.uilibrary.dialog.CommonDialog;


/**
 * 测试万能的Dialog
 */
public class DialogTestActivity extends BaseTestActivity implements View.OnClickListener {
    //视图控件
    private TextView tv_test_des;
    private Button btn1, btn2, btn3, btn4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_test);
        initView();
        initEvent();
    }

    //初始化视图控件
    private void initView() {
        tv_test_des = findViewById(R.id.tv_test_des);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
    }

    //初始化事件
    private void initEvent() {
        tv_test_des.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_test_des:
                //toast("tv_test_des");
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setIcon(R.drawable.img_rv_icon_1) //设置图标
                        .setTitle("我是传统的对话框") //设置标题
                        .setMessage("我是内容") //设置要显示的内容
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                toast("取消");
                            }
                        })
                        .setNeutralButton("第三方按钮", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                toast("第三方按钮");
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                toast("确定");
                            }
                        }).create(); //create（）方法创建对话框
                dialog.show(); //显示对话框
                dialog.setCancelable(true);
                break;
            case R.id.btn1:
                //toast("btn1");
                CommonDialog dialog1 = CommonDialog.newBuilder(baseContext)
                        .layout(R.layout.dialog_with_et)
                        //.widthMatch(false)
                        .gravity(Gravity.TOP)
                        .draggable(true)
                        .anim(R.style.anim_dialog_upIn_downOut)
                        .build();
                dialog1.show();
                dialog1.setMyCancelListener(new CommonDialog.MyCancelListener() {
                    @Override
                    public void afterCancel(int cancelType) {
                        toast("取消类型:" + cancelType);
                    }
                });
                dialog1.setClick(R.id.btn3, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.setEditTextStr(R.id.et1, R.string.app_name);
                    }
                });
                dialog1.setClick(R.id.btn4, new CommonDialog.MyListener() {
                    @Override
                    public void click(View view, boolean isChange) {
                        if (isChange) dialog1.setEditTextStr(R.id.et2, "我是可以记录点击状态的监听器");
                        else dialog1.clearEditText(R.id.et2);
                    }
                });
                break;
            case R.id.btn2:
                //toast("btn2");
                CommonDialog dialog2 = new CommonDialog.Builder(baseContext).cancelable(false).anim(R.style.anim_dialog_upIn_downOut).build();
                dialog2.show();
                dialog2.setImageResource(R.id.iv_fingerprint, R.drawable.img_rv_icon_3);
                dialog2.setClick(R.id.tv_cancel, v -> {
                    dialog2.dismissDialog();
                });
                break;
            case R.id.btn3:
                CommonDialog dialog3 = new CommonDialog.Builder(baseContext).draggable(true).build();
                dialog3.show();
                dialog3.setImageResource(R.id.iv_fingerprint, R.drawable.img_rv_icon_2);
                dialog3.setText(R.id.tv_title, "我在下面，设置了可移动");
                dialog3.setClick(R.id.tv_cancel, v -> {
                    dialog3.dismissDialog();
                });

                CommonDialog dialog4 = new CommonDialog.Builder(baseContext).build();
                dialog4.show();
                dialog4.setImageResource(R.id.iv_fingerprint, R.drawable.img_rv_icon_1);
                dialog4.setText(R.id.tv_title, "我在上面，设置了不可移动");
                dialog4.setClick(R.id.tv_cancel, v -> {
                    dialog4.dismissDialog();
                });
                break;
            case R.id.btn4:
                toast("btn4");
                break;
        }
    }
}