package com.wxdgut.commonui.test.rv.pro;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wxdgut.commonui.R;
import com.wxdgut.commonui.test.BaseTestActivity;
import com.wxdgut.commonui.test.rv.standard.RVTestDataUtils;
import com.wxdgut.uilibrary.rv.free.CommonAdapter;
import com.wxdgut.uilibrary.rv.standard.CommonViewHolder;
import com.wxdgut.uilibrary.rv.standard.RVTestModel;

import java.util.ArrayList;
import java.util.List;

public class RVProTestActivity extends BaseTestActivity {
    private RecyclerView mRecyclerView;
    private List<RVTestModel> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r_v_pro_test);
        mRecyclerView = findViewById(R.id.recycler_view);
        // 设置显示分割 ListView样式
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 添加分割线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        testStandardRvPro();
    }

    private void testStandardRvPro() {
        mList = RVTestDataUtils.getMultipleList(0);
        CommonAdapter<RVTestModel> commonAdapter = new CommonAdapter<>(mList, new CommonAdapter.OnBindDataListener<RVTestModel>() {
            @Override
            public int getLayoutId(int layoutType) {
                return R.layout.layout_rv_item_content;
            }

            @Override
            public void onBindViewHolder(RVTestModel model, CommonViewHolder viewHolder, int type, int position) {
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
            }
        });
        mRecyclerView.setAdapter(commonAdapter);

        commonAdapter.setOnItemClickListener(new CommonAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                toast("点击Item：" + position);
            }
        });
        commonAdapter.setOnItemLongClickListener(new CommonAdapter.MyItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                toast("长击Item：" + position);
            }
        });
    }
}