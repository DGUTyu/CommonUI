package com.wxdgut.commonui.test;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wxdgut.commonui.R;
import com.wxdgut.uilibrary.btn.CommonButton;
import com.wxdgut.uilibrary.dialog.AnimModel;
import com.wxdgut.uilibrary.dialog.CommonDialog;
import com.wxdgut.uilibrary.dialog.DialogQueueManager;
import com.wxdgut.uilibrary.iv.ImageViewPro;
import com.wxdgut.uilibrary.iv.CommonImageView;
import com.wxdgut.uilibrary.switchview.SwitchView;
import com.wxdgut.uilibrary.utils.CommonUtils;

import java.util.Queue;
import java.util.Random;

public class TestActivity extends BaseTestActivity implements View.OnClickListener {
    //视图控件
    private TextView tv_welcome;
    private Button btn1, btn2, btn3, btn4;
    ImageView iv_business;
    ImageViewPro iv_man, iv_data, iv_shop,img_rv_icon_1;
    final boolean[] isClick = {false, false, false, false};
    private CommonButton common_btn1, common_btn2, common_btn3, common_btn4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initView();
        initEvent();
        SwitchView sw3 = findViewById(R.id.sw3);
        sw3.setClickListener(new SwitchView.ClickListener() {
            @Override
            public void onSwitchStateChanged(boolean isChecked) {
                Log.i("SwitchView", "onSwitchStateChanged: " + isChecked);
            }
        });
        CommonImageView commonImageView = findViewById(R.id.my_iv);
        boolean[] flag = {false};
        commonImageView.setOnClickListener(v -> {
            flag[0] = !flag[0];
            v.setSelected(flag[0]);
        });
    }

    private DialogQueueManager<CommonDialog> dialogQueueManager;

    //初始化视图控件
    private void initView() {
        dialogQueueManager = new DialogQueueManager<>();
        tv_welcome = findViewById(R.id.tv_welcome);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        iv_business = findViewById(R.id.iv_business);
        iv_shop = findViewById(R.id.iv_shop);
        iv_man = findViewById(R.id.iv_man);
        iv_data = findViewById(R.id.iv_data);
        img_rv_icon_1 = findViewById(R.id.img_rv_icon_1);
        ImageViewPro imageViewPro = new ImageViewPro(this);
        imageViewPro.changeOtherImgByColorId(R.color.red, R.drawable.img_reward, R.drawable.img_no_data);
        common_btn1 = findViewById(R.id.common_btn1);
        common_btn2 = findViewById(R.id.common_btn2);
        common_btn3 = findViewById(R.id.common_btn3);
        common_btn4 = findViewById(R.id.common_btn4);
        common_btn3.setClick(new CommonButton.MyListener() {
            @Override
            public void click(View view, boolean isChange) {
                e("onClick：" + isChange);
                toast("onClick：" + isChange);
            }
        });
        common_btn3.setTouchListener(new CommonButton.TouchListener() {
            @Override
            public void onTouch(MotionEvent event) {
                e("onTouch：" + event.getAction());
                toast("onTouch：" + event.getAction());
            }
        });
        CommonUtils.getLocationInWindow(btn2, (x, y) -> {
            //e("Location X: " + x + ", Y: " + y);
            Random random = new Random();
            int ran1 = random.nextInt(5) + 1; // 生成1到5之间的随机数
            int ran2 = random.nextInt(5) + 1; // 生成1到5之间的随机数
            //e("随机数 ran1：" + ran1 + "  ran2：" + ran2);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(ran1 * 1000); // delayMillis是延时的毫秒数
                        // 在这里执行您想要延时执行的代码
                        runOnUiThread(() -> {
                            CommonDialog.newBuilder(TestActivity.this).layout(R.layout.dialog_chat).widthMatch(true).showAsDropDown(btn2, Gravity.LEFT, 0, 0).dimAmount(0.0f).anim(CommonDialog.DEFAULT_ANIM).priority(2).build();
                            //CommonDialog build = CommonDialog.newBuilder(TestActivity.this).layout(R.layout.dialog_chat).widthMatch(true).showAsDropDown(btn2, Gravity.LEFT, 0, 0).dimAmount(0.0f).anim(CommonDialog.DEFAULT_ANIM).build();
                            //e("add 聊天");
                            //dialogQueueManager.addDialog(build);
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(ran2 * 1000); // delayMillis是延时的毫秒数
                        // 在这里执行您想要延时执行的代码
                        runOnUiThread(() -> {
                            CommonDialog.newBuilder(TestActivity.this).priority(3).maxCount(3).build();
                            //CommonDialog build = CommonDialog.newBuilder(TestActivity.this).build();
                            //e("add 指纹");
                            //dialogQueueManager.addDialog(build);
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        });
        CommonDialog dialog2 = CommonDialog.newBuilder(TestActivity.this).layout(R.layout.dialog_ad).cancelable(false).dimAmount(0.8f).anim(CommonDialog.DEFAULT_ANIM).priority(4).build();
        //CommonDialog dialog2 = CommonDialog.newBuilder(TestActivity.this).layout(R.layout.dialog_ad).cancelable(false).dimAmount(0.8f).anim(CommonDialog.DEFAULT_ANIM).build();
        //e("add 广告");
        //dialogQueueManager.addDialog(dialog2);
        dialog2.setClick(R.id.iv_close, v -> {
            dialog2.dismissDialog();
        });
        CommonUtils.setClick(common_btn4, (view, isChange) -> {
            e("common_btn4 click: " + isChange);
        });
        //testCommonBtn(common_btn1);
        //testCommonBtn(common_btn2);
    }

    private void testCommonBtn(CommonButton btn) {
        btn.setText("123456");
        btn.setFillet(true);
        btn.setRadius(30);
        btn.setTvColor(Color.parseColor("#000000"));
        btn.setTvColorPress(Color.parseColor("#00ffff"));
        btn.setBgColor(Color.parseColor("#ff0000"));
        btn.setBgColorPress(Color.parseColor("#0000ff"));
        btn.setBgDrawable(getResources().getDrawable(R.drawable.img_rv_icon_2));
        btn.setBgDrawablePress(getResources().getDrawable(R.drawable.img_rv_icon_3));
    }

    //初始化事件
    private void initEvent() {
        tv_welcome.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        iv_data.setOnClickListener(this);
        iv_shop.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_welcome:
                //String date = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(System.currentTimeMillis());
                //e(date + " onClick");
                //TestLibrary.test("测试TestActivity");
                SecondActivity.startActivity(baseContext, false, "one", "two");
                break;
            case R.id.btn1:
                //show("btn1");
                //SecondActivity.startActivity(baseContext, false, "one", "two");
//                changePngColor(this, iv_business, R.drawable.img_business, getResources().getColor(R.color.red));
//                img_rv_icon_1.setImageResourceWithColorId(R.drawable.img_reward,R.color.red);
                CommonDialog dialog1 = CommonDialog.newBuilder(this).layout(R.layout.dialog_fingerprint).draggable(true).cancelable(false).priority(1).build();
                dialog1.setClick(R.id.tv_sure, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toast("1111");
                        dialog1.dismissDialog();
                    }
                });
                dialog1.setClick(R.id.tv_cancel, new CommonDialog.MyListener() {
                    @Override
                    public void click(View view, boolean isChange) {
                        ObjectAnimator animator = dialog1.getAnimator(R.id.iv_fingerprint);
                        if (animator == null) return;
                        if (isChange) animator.pause();
                        else animator.start();
                    }
                });
                AnimModel model = new AnimModel(1);
                dialog1.setAnimView(R.id.iv_fingerprint, model);
                //dialog1.showDialog();

                CommonDialog dialog2 = CommonDialog.newBuilder(this).layout(R.layout.dialog_fingerprint2).priority(0).maxCount(3).gravity(Gravity.TOP).build();
                dialog2.setClick(R.id.tv_sure, new CommonDialog.MyListener() {
                    @Override
                    public void click(View view, boolean isChange) {
                        toast("2222");
                        dialog2.dismissDialog();
                    }
                });
                //dialog2.showDialog();
                try {
                    //模拟网络异常
                    int i = Integer.parseInt("");
                    CommonDialog dialog3 = CommonDialog.newBuilder(this).anim(CommonDialog.DEFAULT_ANIM).build();
                    dialog3.setClick(R.id.tv_sure, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            toast("3333");
                            dialog3.dismissDialog();
                        }
                    });
                    //dialog3.showDialog();
                } catch (Exception e) {
                    CommonDialog.newBuilder(this).widthMatch(true).buildError(true);
                }
                break;
            case R.id.btn2:
                //toast("btn2");
                //changePngColor(this, iv_shop, R.drawable.shape_blue_btn_bg, getResources().getColor(R.color.red));
                iv_shop.setColorId(R.color.red);
                img_rv_icon_1.setDefault();
                break;
            case R.id.btn3:
                //toast("btn3");
                iv_man.setColorId(R.color.red);
                iv_man.setColor(Color.parseColor("#CF5FC0")); //粉色
                iv_data.setColorId(R.color.red);
                break;
            case R.id.btn4:
                iv_man.setImageResourceWithColor(R.drawable.img_man, Color.parseColor("#FCC424")); //橙色
                //iv_man.setImageResourceWithColor(R.drawable.img_man, Color.RED);
                //iv_data.setImageResourceWithColorId(R.drawable.img_man, R.color.blue); //会覆盖img_man的上一次颜色的效果
                iv_data.setImageResourceWithColorId(R.drawable.img_reward, R.color.blue);
                //toast("btn4");
                break;
            case R.id.iv_data:
                isClick[0] = !isClick[0];
                iv_data.setColorId(isClick[0] ? R.color.theme_color : R.color.img_default);
                break;
            case R.id.iv_shop:
                isClick[1] = !isClick[1];
                iv_shop.setColorId(isClick[1] ? R.color.theme_color : R.color.img_default);
                break;
            default:
                break;
        }
    }

    /**
     * 修改PNG图片颜色
     *
     * @param context   上下文
     * @param imageView ImageView
     * @param pngId     需要修改的PNG图片ID
     * @param color     需要改成的颜色
     */
    public static void changePngColor(Context context, ImageView imageView, int pngId, int color) {
        if (context != null && imageView != null) {
            Drawable originalDrawable = context.getResources().getDrawable(pngId); // 加载原始图片
            PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP); // 创建颜色过滤器
            originalDrawable.setColorFilter(colorFilter); // 应用颜色过滤器

            Drawable modifiedDrawable = originalDrawable.mutate(); // 创建一个可变的 Drawable
            modifiedDrawable.setColorFilter(colorFilter); // 应用颜色过滤器

            // 将修改后的图片设置为 ImageView 的 src 属性
            imageView.setImageResource(0); // 首先清除 ImageView 的现有图片
            imageView.setImageDrawable(modifiedDrawable);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 如果需要在页面销毁时清空队列，可以在这里调用
        dialogQueueManager.clearQueue();
        CommonDialog.clearQueue();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Queue<CommonDialog> dialogQueue = CommonDialog.getDialogQueue();
        toast("" + dialogQueue.size());
        CommonDialog.updateMaxCountAndStopInitMore(true);
    }
}