package com.wxdgut.commonui.test.rv.pro;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;

import com.wxdgut.commonui.R;
import com.wxdgut.commonui.test.BaseTestActivity;
import com.wxdgut.commonui.test.rv.standard.RVTestDataUtils;
import com.wxdgut.uilibrary.rv.base.BaseAdapter;
import com.wxdgut.uilibrary.rv.CommonAdapter;
import com.wxdgut.uilibrary.rv.CommonRecyclerView;
import com.wxdgut.uilibrary.rv.CommonViewHolder;
import com.wxdgut.uilibrary.rv.test_model.RVTestModel;
import com.wxdgut.uilibrary.rv.wrap.WrapRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RVProTestActivity extends BaseTestActivity implements CommonRecyclerView.OnRefreshListener, CommonRecyclerView.OnLoadMoreListener {
    private WrapRecyclerView mRecyclerView;
    private List<RVTestModel> mList = new ArrayList<>();
    private CommonAdapter<RVTestModel> commonAdapter;
    private List<RVTestModel> multipleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rv_pro_test);
        mRecyclerView = findViewById(R.id.recycler_view);
        /*
        // 设置显示分割 ListView样式
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 添加分割线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
         */
        /*
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
         */
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //mRecyclerView.addItemDecoration(new CategoryItemDecoration(ContextCompat.getDrawable(this, R.drawable.category_list_divider_red)));
        //mRecyclerView.addItemDecoration(new CategoryItemDecoration(getResources().getColor(R.color.black),20));
        //mRecyclerView.addItemDecoration(new CategoryItemDecoration(getResources().getDrawable(R.drawable.category_list_divider_blue)));
        //mRecyclerView.addItemDecoration(new CategoryItemDecoration(getResources().getColor(R.color.white),10));
        testStandardRvPro();
    }

    private void testStandardRvPro() {
        multipleList = RVTestDataUtils.getMultipleList(0);
        //mList = RVTestDataUtils.getMultipleList(0); //此种写法，上拉加载时mList始终不会变
        mList.addAll(multipleList);
        commonAdapter = new CommonAdapter<>(mList, new BaseAdapter.OnBindDataListener<RVTestModel>() {
            @Override
            public int getLayoutId(int layoutType) {
                return R.layout.layout_rv_item_content;
            }

            @Override
            public void onBindViewHolder(RVTestModel model, CommonViewHolder viewHolder, int type, int listPosition) {
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
                //设置分割线颜色
                viewHolder.setBackgroundColor(R.id.split_v, "#00ff00");
                e("onBindViewHolder listPosition:" + listPosition);
            }
        });
        mRecyclerView.setAdapter(commonAdapter);

        commonAdapter.setOnItemClickListener(new BaseAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int listPosition, int layoutPosition, boolean isChange) {
                view.findViewById(R.id.split_v).setBackgroundColor(isChange ? getResources().getColor(R.color.blue) : getResources().getColor(R.color.black));
                //toast("点击Item：" + listPosition + "/" + layoutPosition + ",头部" + commonAdapter.getHeaderCounts() + ",底部" + commonAdapter.getFooterCounts());
                e("onItemClick listPosition:" + listPosition + "，layoutPosition:" + layoutPosition + "，id:" + mList.get(listPosition).getId());
            }
        });
        commonAdapter.setOnItemLongClickListener(new BaseAdapter.MyItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int listPosition, int layoutPosition, boolean isChange) {
                view.setBackgroundColor(isChange ? getResources().getColor(R.color.blue) : getResources().getColor(R.color.black));
                toast("长按Item：" + listPosition + "/" + layoutPosition);
                e("onItemLongClick listPosition:" + listPosition + "，layoutPosition:" + layoutPosition + "，id:" + mList.get(listPosition).getId());
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //mRecyclerView.addItemDecoration(new CategoryItemDecoration(getResources().getDrawable(R.drawable.category_list_divider_blue)));

//        mRecyclerView.addRefreshViewCreator(new DefaultRefreshCreator());
//        mRecyclerView.setOnRefreshListener(this);
//        mRecyclerView.addLoadViewCreator(new DefaultLoadCreator());
//        mRecyclerView.setOnLoadMoreListener(this);
        // 设置正在获取数据页面和无数据页面
        mRecyclerView.addLoadingView(findViewById(R.id.load_view));
        mRecyclerView.addEmptyView(findViewById(R.id.empty_view));

        // 添加头部和底部

        View titleView = LayoutInflater.from(this).inflate(R.layout.layout_rv_item_title, mRecyclerView, false);
        titleView.setOnClickListener(v -> {
            toast("头部" + commonAdapter.getHeaderCounts() + ",底部" + commonAdapter.getFooterCounts());
        });
        commonAdapter.addHeaderView(titleView);
        commonAdapter.addFooterView(LayoutInflater.from(this).inflate(R.layout.layout_rv_item_title, mRecyclerView, false));
        View view2 = LayoutInflater.from(this).inflate(R.layout.dialog_fingerprint, mRecyclerView, false);
        commonAdapter.addFooterView(view2);
        SparseArray<View> footers = commonAdapter.getFooters();
        footers.valueAt(0).setOnClickListener(v -> {
            toast("底部");
        });
        commonAdapter.removeFooterView(view2);

    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //mRecyclerView.onStopRefresh();
            }
        }, 2000);
    }

    @Override
    public void onLoad() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                e("mList前:" + mList.size());
                mList.addAll(RVTestDataUtils.getMultipleList(0));
                e("mList后:" + mList.size());
                //mRecyclerView.onStopLoad();
                commonAdapter.notifyDataSetChanged();
            }
        }, 2000);
    }
}