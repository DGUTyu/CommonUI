package com.wxdgut.uilibrary.rv.standard;

/**
 * FileName: CommonItemModel
 * Founder: lu yao
 * User: Administrator
 * Date: 2022年11月
 * Copyright (c) 2022 http://wxdgut.com
 * Email: hello_luyao@163.com
 * Profile:万能的RecyclerView适配器的item model
 * 数据item model需要继承CommonItemModel
 */
public class CommonItemModel {
    //当前model对应的布局类型枚举
    private Type type;

    //记录当前类型时的携带信息，如当类型为标题时，可用于设置title。
    private String info;

    //记录当前类型时对应在数据列表中的下标位置，如可用于记录title类型时对应的下标
    private int index = -1;

    //布局类型枚举
    public enum Type {
        TITLE(1),
        CONTENT(2),
        OTHER(3);

        //布局类型枚举对应的值
        private int value;

        //枚举构造方法
        Type(int val) {
            this.value = val;
        }

        //获取该类型枚举对应的值
        public int getValue() {
            return value;
        }
    }

    //默认当前model对应的布局类型枚举为CONTENT
    public CommonItemModel() {
        this.type = Type.CONTENT;
    }

    //此构造方法可删
    public CommonItemModel(Type type) {
        this.type = type;
    }

    //获取当前model对应的布局类型枚举
    public Type getType() {
        return type;
    }

    //设置当前model对应的布局类型枚举
    public void setType(Type type) {
        this.type = type;
    }

    //设置当前model对应的布局类型枚举，也可以把当前位置下标传递进来
    public void setType(Type type, int index) {
        this.type = type;
        this.index = index;
    }

    //获取当前model对应的布局类型值
    public int getLayoutType() {
        return type.getValue();
    }

    //获取当前类型对应的信息，如果没设置过就返回当时传入的下标，如果下标也没传过，就返回-1
    public String getInfo() {
        return info == null ? String.valueOf(getIndex()) : info;
    }

    //设置当前类型对应的信息
    public void setInfo(String info) {
        this.info = info;
    }

    //获取设置类型时传入的下标位置
    public int getIndex() {
        return index;
    }

    //不对外公开，以防误用、被重写
    private void setIndex(int index) {
        //
    }
}
