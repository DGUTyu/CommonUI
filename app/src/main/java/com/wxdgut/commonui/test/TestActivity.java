package com.wxdgut.commonui.test;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wxdgut.commonui.R;
import com.wxdgut.uilibrary.iv.ImageViewPro;

public class TestActivity extends BaseTestActivity implements View.OnClickListener {
    //视图控件
    private TextView tv_welcome;
    private Button btn1, btn2, btn3, btn4;
    ImageView iv_business;
    ImageViewPro iv_man, iv_data, iv_shop;
    final boolean[] isClick = {false, false, false, false};

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
        iv_business = findViewById(R.id.iv_business);
        iv_shop = findViewById(R.id.iv_shop);
        iv_man = findViewById(R.id.iv_man);
        iv_data = findViewById(R.id.iv_data);
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
                changePngColor(this, iv_business, R.drawable.img_business, getResources().getColor(R.color.red));
                break;
            case R.id.btn2:
                //toast("btn2");
                changePngColor(this, iv_shop, R.drawable.shape_blue_btn_bg, getResources().getColor(R.color.red));
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

}