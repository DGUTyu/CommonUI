package com.wxdgut.commonui.test.rv.standard;

import com.wxdgut.commonui.R;
import com.wxdgut.uilibrary.rv.test_model.RVTestModel;

import java.util.ArrayList;
import java.util.List;

/**
 * FileName: SimulationData
 * Founder: lu yao
 * User: Administrator
 * Date: 2022年11月
 * Copyright (c) 2022 http://wxdgut.com
 * Email: hello_luyao@163.com
 * Profile: 产生模拟数据的工具类
 */
public class RVTestDataUtils {
    private static List<RVTestModel> list = new ArrayList<>();

    private static final String[] tokenPhoto = {
            "http://b-ssl.duitang.com/uploads/item/201704/20/20170420191558_YCic5.jpeg",
            "https://b-ssl.duitang.com/uploads/item/201803/04/20180304165637_RzBQW.thumb.700_0.jpeg",
            "http://pic.baike.soso.com/ugc/baikepic2/29883/20150811164729-997575824.jpg/0",
            "http://b-ssl.duitang.com/uploads/item/201802/25/20180225184943_ZRAdx.thumb.700_0.jpeg",
            "https://b-ssl.duitang.com/uploads/item/201802/16/20180216162338_mUtHA.thumb.700_0.jpeg",
            "http://b-ssl.duitang.com/uploads/item/201810/18/20181018162951_kgwzm.thumb.700_0.jpeg",
            "http://pic.rmb.bdstatic.com/3f0d86862e2545242a3d0cea252298ab.jpeg",
            "http://b-ssl.duitang.com/uploads/item/201503/31/20150331155006_kd5rr.jpeg",
            "http://b-ssl.duitang.com/uploads/item/201612/08/20161208204750_rS8N4.jpeg",
            "https://b-ssl.duitang.com/uploads/item/201803/10/20180310112233_wwtah.jpeg"};

    private static final String[] tokenNickName = {
            "吃货最怕饿梦",
            "爱做梦的小孩",
            "超萌杀手",
            "甜心教主",
            "偏执的傲",
            "半呆半萌半幼稚",
            "蠢萌蠢萌的二货",
            "开启童真模式",
            "画个圈圈鄙视你",
            "萌量不足"};

    private static final String[] username = {
            "张三", "李四", "王五", "赵六", "小二",
            "小白", "慧慧", "小吴", "李白", "小孙"};

    private static final int[] photo = {
            R.drawable.img_rv_icon_1,
            R.drawable.img_rv_icon_2,
            R.drawable.img_rv_icon_3,
            R.drawable.img_rv_icon_4,
            R.drawable.img_rv_icon_5,
            R.drawable.img_rv_icon_6,
            R.drawable.img_rv_icon_7,
            R.drawable.img_rv_icon_8,
            R.drawable.img_rv_icon_9,
            R.drawable.img_rv_icon_10,
    };

    private static final String[] des = {
            "叮咚，你有新的爱意请注意查收。",
            "对你，漫山遍野的喜欢。",
            "我希望你做个甜甜的梦，我的意思是梦到我。",
            "喜欢你，比喜欢肥宅快乐水还多。",
            "累吗，点开乐园又看到我这个你得不到的女人。",
            "请收好今晚的月亮和我爱你。",
            "我真是太可爱了，连蚊子都要亲我一口。",
            "最近很想你，最远也是。",
            "其实我想一路小跑，跳到你的身上，吧唧亲你一口。",
            "我行我素，超甜还酷。"};

    private static final String[] phone = {
            "18970962301", "18970964402",
            "18970964403", "18970964404",
            "18970964405", "18970964406",
            "18970964407", "18970964409",
            "18970964411", "18970964421"};

    private static final int[] age = {16, 17, 18, 19, 20, 21, 22, 23, 24, 25};

    private static final String[] birthday = {
            "1991-8-8", "1997-5-13",
            "1998-9-23", "1991-10-21",
            "1996-6-15", "1995-8-2",
            "1993-7-8", "1994-2-24",
            "2000-7-20", "2001-9-3"};

    private static final String[] constellation = {
            "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座",
            "处女座", "天秤座", "天蝎座", "射手座", "摩羯座"};

    private static final String[] hobby = {
            "运动", "音乐", "电影", "绘画", "小说",
            "冒险", "拼图", "收藏", "游戏", "发呆"};

    private static final String[] status = {
            "已婚", "单身", "热恋", "单身", "单身",
            "单身", "单身", "单身", "已婚", "单身"};
    private static int idNum = 2000;

    //返回数据集list
    public static List<RVTestModel> getList() {
        return baseGetList(-1, null);
    }

    //返回数据集list
    public static List<RVTestModel> getMultipleList(int multiple) {
        List<RVTestModel> list = baseGetList(-1, null);
        List<RVTestModel> listTemp = list;
        for (int i = 0; i < multiple; i++) {
            list.addAll(listTemp);
        }
        return list;
    }

    //返回数据集list
    public static List<RVTestModel> getList(int specialHandlePos, RVTestModel specialModel) {
        return baseGetList(specialHandlePos, specialModel);
    }

    //返回数据集list
    public static List<RVTestModel> baseGetList(int specialHandlePos, RVTestModel specialModel) {
        list.clear();
        //性别按照 女男顺序 交替赋值
        for (int i = 0; i < tokenPhoto.length; i++) {
            createData(specialHandlePos, specialModel, i, tokenPhoto[i], tokenNickName[i], username[i], photo[i], i % 2 == 1,
                    des[i], phone[i], age[i], birthday[i], constellation[i], hobby[i], status[i]);
        }
        return list;
    }

    private static void createData(int specialPos, RVTestModel specialModel, int i, String photoUrl, String nickName, String name, int photo, boolean sex,
                                   String desc, String phone, int age, String birthday, String constellation, String hobby, String status) {
        baseCreateData(String.valueOf(idNum++), specialPos, specialModel, i, photoUrl, nickName, name, photo, sex,
                desc, phone, age, birthday, constellation, hobby, status);
    }


    //生成model并添加到列表中
    private static void baseCreateData(String id, int specialPos, RVTestModel specialModel, int i, String photoUrl, String nickName, String name, int photo, boolean sex,
                                       String desc, String phone, int age, String birthday, String constellation, String hobby, String status) {
        RVTestModel model = new RVTestModel();
        model.setId(id);
        model.setTokenPhoto(photoUrl);
        model.setTokenNickName(nickName);

        model.setName(name);
        model.setPassword("123456");

        model.setPhoto(photo);
        model.setSex(sex);
        model.setDesc(desc);
        model.setPhone(phone);

        model.setAge(age);
        model.setBirthday(birthday);
        model.setConstellation(constellation);
        model.setHobby(hobby);
        model.setStatus(status);
        //特殊处理
        if (specialPos == i && specialModel != null) model = specialModel;
        //添加到列表中
        list.add(model);
    }

}
