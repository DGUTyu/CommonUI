package com.wxdgut.commonui.test.rv.pro;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.wxdgut.commonui.R;
import com.wxdgut.commonui.test.BaseTestActivity;
import com.wxdgut.commonui.test.rv.standard.RVTestDataUtils;
import com.wxdgut.uilibrary.rv.base.BaseAdapter;
import com.wxdgut.uilibrary.rv.CommonAdapter;
import com.wxdgut.uilibrary.rv.CommonRecyclerView;
import com.wxdgut.uilibrary.rv.CommonViewHolder;
import com.wxdgut.uilibrary.rv.test_model.RVTestModel;
import com.wxdgut.uilibrary.rv.view_creator.DefaultLoadCreator;
import com.wxdgut.uilibrary.rv.view_creator.DefaultRefreshCreator;
import com.wxdgut.uilibrary.rv.wrap.WrapRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RVProTestActivity extends BaseTestActivity implements CommonRecyclerView.OnRefreshListener, CommonRecyclerView.OnLoadMoreListener {
    private CommonRecyclerView mRecyclerView;
    private List<RVTestModel> mList = new ArrayList<>();
    private CommonAdapter<RVTestModel> commonAdapter;
    private FloatingActionButton fb_square_top;
    private ImageView iv_to_top;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rv_pro_test);
        mRecyclerView = findViewById(R.id.recycler_view);
        fb_square_top = findViewById(R.id.fb_square_top);
        iv_to_top = findViewById(R.id.iv_to_top);
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
        initListener();
        testStandardRvPro();
    }

    private void initListener() {
        mRecyclerView.addToTopView(iv_to_top);
        mRecyclerView.addFloatActionButton(fb_square_top, new WrapRecyclerView.FloatBtnListener() {
            @Override
            public void click() {
                toast("1");
            }
        });
    }

    private void testStandardRvPro() {
        // 设置布局管理器
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        // 设置正在获取数据页面和无数据页面
        mRecyclerView.addLoadingView(findViewById(R.id.load_view));
        mRecyclerView.addEmptyView(findViewById(R.id.empty_view));
        // 设置上拉下拉
//        mRecyclerView.addDefaultRefreshViewCreator();
//        mRecyclerView.setOnRefreshListener(this);
//        mRecyclerView.addDefaultLoadViewCreator();
//        mRecyclerView.setOnLoadMoreListener(this);

//        mRecyclerView.setOnRefreshListener(this, true);
//        mRecyclerView.setOnLoadMoreListener(this, true);

        mRecyclerView.setOnLoadRefreshListener(this, this);

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
        // 设置适配器
        mRecyclerView.setAdapter(commonAdapter);
        //设置监听事件
        commonAdapter.setOnItemClickListener(new BaseAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int listPosition, int layoutPosition, boolean isChange) {
                view.findViewById(R.id.split_v).setBackgroundColor(isChange ? getResources().getColor(R.color.blue) : getResources().getColor(R.color.black));
                toast("点击Item：" + listPosition + "/" + layoutPosition + "/" + commonAdapter.getListPosition(layoutPosition));
                e("onItemClick listPosition:" + listPosition + "，layoutPosition:" + layoutPosition + "，id:" + mList.get(listPosition).getId());
            }
        });
        commonAdapter.setOnItemLongClickListener(new BaseAdapter.MyItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int listPosition, int layoutPosition, boolean isChange) {
                view.setBackgroundColor(isChange ? getResources().getColor(R.color.blue) : getResources().getColor(R.color.black));
                toast("长按Item：" + listPosition + "/" + layoutPosition + "/" + commonAdapter.getListPosition(layoutPosition));
                e("onItemLongClick listPosition:" + listPosition + "，layoutPosition:" + layoutPosition + "，id:" + mList.get(listPosition).getId());
//                e(mList.get(listPosition).toString());
//                mList.remove(listPosition);
//                commonAdapter.notifyDataSetChanged();
            }
        });

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
        // 移除底部
        commonAdapter.removeFooterView(view2);
        //避免先监听，然后又移除了底部
        footers.valueAt(footers.size() - 1).setOnClickListener(v -> {
            toast("底部");
        });

        //模拟请求到数据
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mList.addAll(RVTestDataUtils.getMultipleList(1));
                commonAdapter.notifyDataSetChanged();

                RecyclerView.Adapter realAdapter = mRecyclerView.getRealAdapter();
                int itemCount = realAdapter.getItemCount();
                e("" + itemCount);
            }
        }, 2000);

        //**********************************有缺陷，删除后部分页面是空白的，没有及时填充
        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(new ItemTouchHelper.Callback() {
            //设置可拖动和可删除的Flags
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                //return 0; //return makeMovementFlags(ItemTouchHelper.LEFT, ItemTouchHelper.LEFT);
                //头部不可以移除
                int position = viewHolder.getLayoutPosition();
                if(commonAdapter.isHeaderOrFooterPosition(position)) return 0;

                // 获取触摸响应的方向   包含两个 1.拖动dragFlags 2.侧滑删除swipeFlags
                // 代表只能是向左侧滑删除，当前可以是这样ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT
                int swipeFlags = ItemTouchHelper.LEFT;
                int dragFlags = 0;
                if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                    // GridView 样式四个方向都可以
                    dragFlags = ItemTouchHelper.UP | ItemTouchHelper.LEFT |
                            ItemTouchHelper.DOWN | ItemTouchHelper.RIGHT;
                } else {
                    // ListView 样式不支持左右
                    dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                }
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            //拖动的时候不断的回调方法
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                //return false;
                return false;
            }

            //侧滑删除后会回调的方法,direction=4代表是左边删除
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int layoutPosition = viewHolder.getLayoutPosition();
                int listPosition = commonAdapter.getListPosition(layoutPosition);
                e("layoutPosition:" + layoutPosition + " listPosition:" + listPosition + " id:" + mList.get(listPosition).getId());
                e(mList.get(listPosition).toString());
                mList.remove(listPosition);
                commonAdapter.notifyDataSetChanged();
            }

            //拖动选择状态改变回调
            @Override
            public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
                //super.onSelectedChanged(viewHolder, actionState);
                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                    // ItemTouchHelper.ACTION_STATE_IDLE 看看源码解释就能理解了
                    // 侧滑或者拖动的时候背景设置为灰色
                    viewHolder.itemView.setBackgroundColor(Color.RED);
                }
            }

            //回到正常状态的时候回调
            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                //super.clearView(recyclerView, viewHolder);
                // 正常默认状态下背景恢复默认
                viewHolder.itemView.setBackgroundColor(0);
                viewHolder.itemView.setTranslationX(0);
            }
        });
        //attach
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
        //**********************************

    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.onStopRefresh();
            }
        }, 2000);
    }

    @Override
    public void onLoad() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                e("mList前:" + mList.size());
                mList.addAll(RVTestDataUtils.getList());
                e("mList后:" + mList.size());
                mRecyclerView.onStopLoad();
                commonAdapter.notifyDataSetChanged();
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        //返回true表示允许创建的菜单显示出来，如果返回了false，创建的菜单将无法显示
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.id_action_gridview:
                mRecyclerView.updateLayoutManager(new GridLayoutManager(this, 4));
                break;
            case R.id.id_action_listview:
                mRecyclerView.updateLayoutManager(new LinearLayoutManager(this));
                break;
        }
        return true;
    }
}