package com.bonc.channel_edit.bean;

/**
 * Created by leeky on 2017/11/2.
 */

public class ChanelBean {

    String src;
    int typeView;
    String channelId;
    String isFixed;    //已添加的置成不能编辑
    String androidImgUrl;
    String iosImgUrl;
    String belongId;

    public ChanelBean(String src, String androidImgUrl, String iosImgUrl, String channelId, String belongId , int typeView) {
        this.src = src;
        this.androidImgUrl = androidImgUrl;
        this.iosImgUrl = iosImgUrl;
        this.channelId = channelId;
        this.belongId = belongId;
        this.typeView = typeView;
    }
    public ChanelBean(String src, String channelId, int typeView) {
        this.src = src;
        this.typeView = typeView;
        this.channelId = channelId;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public int getTypeView() {
        return typeView;
    }

    public void setTypeView(int typeView) {
        this.typeView = typeView;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getIsFixed() {
        return isFixed;
    }

    public void setIsFixed(String isFixed) {
        this.isFixed = isFixed;
    }

    public String getIosImgUrl() {
        return iosImgUrl;
    }

    public void setIosImgUrl(String iosImgUrl) {
        this.iosImgUrl = iosImgUrl;
    }

    public String getAndroidImgUrl() {
        return androidImgUrl;
    }

    public void setAndroidImgUrl(String androidImgUrl) {
        this.androidImgUrl = androidImgUrl;
    }

    public String getBelongId() {
        return belongId;
    }

    public void setBelongId(String belongId) {
        this.belongId = belongId;
    }
}
