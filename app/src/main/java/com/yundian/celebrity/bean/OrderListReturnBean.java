package com.yundian.celebrity.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sll on 2017/7/19.
 */

public class OrderListReturnBean implements Parcelable {

    /**
     * "mid":3,                #id
     * "name":"录制节目",      #名称
     * "price":10000.0,        #价格
     * "showpic_url":""        #图片
     */

    private int mid;
    private String name;
    private double price;
    private String showpic_url;
    private String starcode;
    private boolean isCheck = false;

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getShowpic_url() {
        return showpic_url;
    }

    public void setShowpic_url(String showpic_url) {
        this.showpic_url = showpic_url;
    }

    public String getStarcode() {
        return starcode;
    }

    public void setStarcode(String starcode) {
        this.starcode = starcode;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mid);
        dest.writeString(this.name);
        dest.writeDouble(this.price);
        dest.writeString(this.showpic_url);
        dest.writeString(this.starcode);
        dest.writeByte(this.isCheck ? (byte) 1 : (byte) 0);
    }

    public OrderListReturnBean() {
    }

    protected OrderListReturnBean(Parcel in) {
        this.mid = in.readInt();
        this.name = in.readString();
        this.price = in.readDouble();
        this.showpic_url = in.readString();
        this.starcode = in.readString();
        this.isCheck = in.readByte() != 0;
    }

    public static final Creator<OrderListReturnBean> CREATOR = new Creator<OrderListReturnBean>() {
        @Override
        public OrderListReturnBean createFromParcel(Parcel source) {
            return new OrderListReturnBean(source);
        }

        @Override
        public OrderListReturnBean[] newArray(int size) {
            return new OrderListReturnBean[size];
        }
    };
}
