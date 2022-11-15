package com.wxdgut.commonui.test.rv.standard;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.wxdgut.commonui.R;
import com.wxdgut.commonui.test.BaseTestActivity;
import com.wxdgut.uilibrary.rv.standard.CommonAdapter;
import com.wxdgut.uilibrary.rv.standard.CommonItemModel;
import com.wxdgut.uilibrary.rv.standard.CommonViewHolder;
import com.wxdgut.uilibrary.rv.standard.RVTestModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试万能的RecyclerView
 */
public class RVTestActivity extends BaseTestActivity implements View.OnClickListener {
    //视图控件
    private TextView tv_test_des;
    private Button btn1, btn2, btn3, btn4;

    private RecyclerView recyclerView;
    private CommonAdapter<RVTestModel> commonAdapter;
    private List<RVTestModel> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rv_test);
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
        recyclerView = findViewById(R.id.rv);
        testStandardRv();
    }

    private void testStandardRv() {
        //mList = RVTestDataUtils.getList();

        RVTestModel titleModel = new RVTestModel();
        //titleModel.setType(CommonItemModel.Type.TITLE,3);
        titleModel.setType(CommonItemModel.Type.TITLE);
        titleModel.setInfo("这是我的标题");
        mList = RVTestDataUtils.getList(3, titleModel);

        //列表的实现
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //设置下划线
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        commonAdapter = new CommonAdapter<RVTestModel>(mList, new CommonAdapter.OnMoreBindDataListener<RVTestModel>() {
            @Override
            public int getItemType(int position) {
                return mList.get(position).getType().getValue();
            }

            @Override
            public int getLayoutId(int type) {
                if (type == CommonItemModel.Type.TITLE.getValue()) {
                    //title布局 type=0
                    return R.layout.layout_rv_item_title;
                } else if (type == CommonItemModel.Type.CONTENT.getValue()) {
                    //item布局 type=1
                    return R.layout.layout_rv_item_content;
                }
                return 0;
            }

            @Override
            public void onBindViewHolder(RVTestModel model, CommonViewHolder viewHolder, int type, int position) {
                if (type == CommonItemModel.Type.TITLE.getValue()) {
                    viewHolder.setText(R.id.tv_title, model.getInfo());
                    viewHolder.setClick(R.id.tv_title, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            toast("标题:" + position);
                        }
                    });
                } else if (type == CommonItemModel.Type.CONTENT.getValue()) {
                    //设置头像
                    viewHolder.setImageResource(R.id.iv_photo, model.getPhoto());
                    //设置性别
                    viewHolder.setImageResource(R.id.iv_sex,
                            model.isSex() ? R.drawable.img_rv_boy : R.drawable.img_rv_girl);
                    //设置昵称
                    viewHolder.setText(R.id.tv_nickname, model.getTokenNickName());
                    //年龄
                    viewHolder.setText(R.id.tv_age, model.getAge() + " 岁");
                    //用户名
                    viewHolder.setText(R.id.tv_name, model.getName());
                    //电话
                    viewHolder.setText(R.id.tv_phone, model.getPhone());
                    //设置描述
                    viewHolder.setText(R.id.tv_desc, model.getDesc());

                    //昵称点击事件
                    viewHolder.setClick(R.id.tv_nickname, new CommonViewHolder.MyListener() {
                        @Override
                        public void click(boolean isChange) {
                            //动态来回改变昵称字体颜色
                            viewHolder.setTextColor(R.id.tv_nickname, baseContext, isChange ? R.color.black : R.color.theme_color);
                        }
                    });
                }
            }
        });
        recyclerView.setAdapter(commonAdapter);
        //Item点击事件
        commonAdapter.setOnItemClickListener(new CommonAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                toast("点击Item：" + position);
            }
        });
        //Item长按事件
        commonAdapter.setOnItemLongClickListener(new CommonAdapter.MyItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                toast("长按Item：" + position);
            }
        });
        //commonAdapter.notifyDataSetChanged();
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
                toast("tv_test_des");
                break;
            case R.id.btn1:
                toast("btn1");
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