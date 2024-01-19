package com.wxdgut.commonui.test.popup;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wxdgut.commonui.R;
import com.wxdgut.commonui.test.BaseTestActivity;
import com.wxdgut.uilibrary.bubble.BubbleTouchListener;
import com.wxdgut.uilibrary.bubble.BubbleView;
import com.wxdgut.uilibrary.dialog.AnimModel;
import com.wxdgut.uilibrary.dialog.BaseDialog;
import com.wxdgut.uilibrary.dialog.BaseDialogInitBean;
import com.wxdgut.uilibrary.like.LoveLayout;
import com.wxdgut.uilibrary.popup.CommonPopup;
import com.wxdgut.uilibrary.utils.CommonUtils;

public class PopupTestActivity extends BaseTestActivity implements View.OnClickListener {
    private LinearLayout ll_root;
    private TextView tv_pop;
    private Button btn1, btn2, btn3, btn4;
    private CommonPopup commonPopup;
    private EditText et;
    private ImageView iv_bomb;
    private TextView tv_bomb;
    private LoveLayout love_ll;

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
        iv_bomb = findViewById(R.id.iv_bomb);
        tv_bomb = findViewById(R.id.tv_bomb);
        love_ll = findViewById(R.id.love_ll);
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
        //commonPopup.showEverywhereInView(PopupTestActivity.this, ll_root);

        BubbleView.attach(iv_bomb, new BubbleTouchListener.BubbleDismissListener() {
            @Override
            public void dismiss(View view) {
                toast("消失了");
            }

            @Override
            public void onClick(View view) {
                toast("点击了");
            }
        });

        //BubbleView.attach(tv_bomb);

        tv_bomb.setOnClickListener(v->{
            love_ll.addLove(3);
        });

        love_ll.setExternalClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("再次点击");
            }
        });

        findViewById(R.id.tv_123).setOnClickListener(v->{
            toast("123");
        });
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
                CommonUtils.restartApp(this);
                break;
            case R.id.btn2:
                toast("2");
                CommonUtils.grayView(getWindow().getDecorView(), true);
                break;
            case R.id.btn3:
                toast("3");
                CommonUtils.grayView(getWindow().getDecorView(), false);
                break;
            case R.id.btn4:
                initPopu();
                commonPopup.showAsDropDown();
                break;
            case R.id.tv_pop:
                //toast("tv_pop");
                showDialog();
                break;
            case R.id.et:
                break;
        }
    }

    private void showDialog() {
//        int toBottom = CommonUtils.getViewDistanceToBottom(ll_root, tv_pop);
//        CommonDialog commonDialog = CommonDialog.newBuilder(this).layout(R.layout.dialog_fingerprint4)
//                .height(toBottom).widthMatch(true)
//                .showAsDropDown(tv_pop)
//                .autoDismiss(false)
//                .build();
//        commonDialog.showDialog();
        BaseDialogInitBean bean = new BaseDialogInitBean();
        bean.setDraggable(true);
        bean.setDimAmount(0.1f);
        bean.setLayout(R.layout.dialog_fingerprint2);
        BaseDialog baseDialog = new BaseDialog(this, bean);
        baseDialog.setAnimView(R.id.iv_fingerprint, new AnimModel(AnimModel.ROTATION));
        baseDialog.setAnimView(R.id.tv_cancel, new AnimModel(AnimModel.TRANSLATION_X));
        baseDialog.showDialog();

        baseDialog.setClick(R.id.tv_cancel, v -> {
            toast("666");
            baseDialog.dismissDialog();
        });

        baseDialog.setClick(R.id.tv_sure, (view, isChange) -> {
            toast("777");
            baseDialog.setAnimatorStatus(isChange ? BaseDialog.ANIM_PAUSE : BaseDialog.ANIM_RESUME, R.id.iv_fingerprint, R.id.tv_cancel);
        });

    }
}
