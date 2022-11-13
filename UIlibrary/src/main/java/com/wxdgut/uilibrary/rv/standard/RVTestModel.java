package com.wxdgut.uilibrary.rv.standard;

/**
 * FileName: TestModel
 * Founder: lu yao
 * User: Administrator
 * Date: 2022年11月
 * Copyright (c) 2022 http://wxdgut.com
 * Email: hello_luyao@163.com
 * Profile:
 */
public class RVTestModel extends CommonItemModel {
    //Token属性
    //获取Token的头像地址
    private String tokenPhoto;
    //获取Token的昵称
    private String tokenNickName;

    //基本属性 用户名和密码
    //用户名
    private String name;
    //密码
    private String password;
    //本地资源头像
    private int photo;

    //其他属性
    //性别 true = 男  false = 女
    private boolean sex = true;
    //简介
    private String desc;
    //电话
    private String phone;

    //年龄
    private int age = 0;
    //生日
    private String birthday;
    //星座
    private String constellation;
    //爱好
    private String hobby;
    //单身状态
    private String status;

    public String getTokenPhoto() {
        return tokenPhoto;
    }

    public void setTokenPhoto(String tokenPhoto) {
        this.tokenPhoto = tokenPhoto;
    }

    public String getTokenNickName() {
        return tokenNickName;
    }

    public void setTokenNickName(String tokenNickName) {
        this.tokenNickName = tokenNickName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
