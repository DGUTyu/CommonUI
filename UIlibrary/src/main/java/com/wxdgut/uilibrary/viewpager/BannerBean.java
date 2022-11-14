package com.wxdgut.uilibrary.viewpager;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * FileName: BannerView
 * Founder: yu
 * User: Administrator
 * Date: 2022年10月
 * Copyright (c) 2022 http://wxdgut.com
 * Email: hello_luyao@163.com
 * Profile: 轮播图实体类
 */
public class BannerBean implements Parcelable {
    //跳转类型 0跳转H5,1跳转小程序
    int urlType = 0;
    //描述
    private String title;
    //图片url
    private String imgUrl;
    //链接url 备用
    private String linkUrl;
    //小程序原始id
    private String miniId;
    //小程序页面url或h5 url
    private String contentUrl;

    public BannerBean(int urlType, String imgUrl) {
        this.urlType = urlType;
        this.imgUrl = imgUrl;
        enSureNotNull(null, imgUrl, null, null, null);
    }

    public BannerBean(int urlType, String imgUrl, String miniId, String contentUrl) {
        this.urlType = urlType;
        this.imgUrl = imgUrl;
        this.miniId = miniId;
        this.contentUrl = contentUrl;
        enSureNotNull(null, imgUrl, null, miniId, contentUrl);
    }

    //确保字段不为空
    private void enSureNotNull(String title, String imgUrl, String linkUrl, String miniId, String contentUrl) {
        if (title == null) this.title = "";
        if (imgUrl == null) this.imgUrl = "";
        if (linkUrl == null) this.linkUrl = "";
        if (miniId == null) this.miniId = "";
        if (contentUrl == null) this.contentUrl = "";
    }

    public BannerBean(String title, String imgUrl, int urlType, String linkUrl, String miniId, String contentUrl) {
        this.title = title;
        this.imgUrl = imgUrl;
        this.urlType = urlType;
        this.linkUrl = linkUrl;
        this.miniId = miniId;
        this.contentUrl = contentUrl;
        enSureNotNull(title, imgUrl, linkUrl, miniId, contentUrl);
    }

    public String getTitle() {
        if (title == null) return "";
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        if (imgUrl == null) return "";
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getUrlType() {
        return urlType;
    }

    public void setUrlType(int urlType) {
        this.urlType = urlType;
    }

    private String getLinkUrl() {
        if (linkUrl == null) return "";
        return linkUrl;
    }

    private void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getMiniId() {
        if (miniId == null) return "";
        return miniId;
    }

    public void setMiniId(String miniId) {
        this.miniId = miniId;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getContentUrl() {
        if (contentUrl == null) return "";
        return contentUrl;
    }

    protected BannerBean(Parcel in) {
        title = in.readString();
        imgUrl = in.readString();
        urlType = in.readInt();
        linkUrl = in.readString();
        miniId = in.readString();
        contentUrl = in.readString();
    }

    public static final Creator<BannerBean> CREATOR = new Creator<BannerBean>() {
        @Override
        public BannerBean createFromParcel(Parcel in) {
            return new BannerBean(in);
        }

        @Override
        public BannerBean[] newArray(int size) {
            return new BannerBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(imgUrl);
        dest.writeInt(urlType);
        dest.writeString(linkUrl);
        dest.writeString(miniId);
        dest.writeString(contentUrl);
    }

    @Override
    public String toString() {
        return "BannerBean{" +
                "urlType=" + urlType +
                ", title='" + title + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", linkUrl='" + linkUrl + '\'' +
                ", miniId='" + miniId + '\'' +
                ", contentUrl='" + contentUrl + '\'' +
                '}';
    }
}
