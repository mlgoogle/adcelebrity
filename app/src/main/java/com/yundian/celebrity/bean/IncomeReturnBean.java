package com.yundian.celebrity.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sll on 2017/7/15.
 */

public class IncomeReturnBean implements Parcelable {

    /**
     "max_price":45.21,         最高价
     "min_price":45.21,         最低价
     "order_count":1,           订单总笔数
     "order_num":1,             订单总时间
     "orderdate":20170626,      日期
     "price":45.21,             订单总金额
     "profit":38.4285,          收益
     "starcode":"1013"          明星id
     */

    private double max_price;
    private double min_price;
    private int order_count;
    private int order_num;
    private int orderdate;
    private double price;
    private double profit;
    private String starcode;

    public double getMax_price() {
        return max_price;
    }

    public void setMax_price(double max_price) {
        this.max_price = max_price;
    }

    public double getMin_price() {
        return min_price;
    }

    public void setMin_price(double min_price) {
        this.min_price = min_price;
    }

    public int getOrder_count() {
        return order_count;
    }

    public void setOrder_count(int order_count) {
        this.order_count = order_count;
    }

    public int getOrder_num() {
        return order_num;
    }

    public void setOrder_num(int order_num) {
        this.order_num = order_num;
    }

    public int getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(int orderdate) {
        this.orderdate = orderdate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public String getStarcode() {
        return starcode;
    }

    public void setStarcode(String starcode) {
        this.starcode = starcode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.max_price);
        dest.writeDouble(this.min_price);
        dest.writeInt(this.order_count);
        dest.writeInt(this.order_num);
        dest.writeInt(this.orderdate);
        dest.writeDouble(this.price);
        dest.writeDouble(this.profit);
        dest.writeString(this.starcode);
    }

    public IncomeReturnBean() {
    }

    protected IncomeReturnBean(Parcel in) {
        this.max_price = in.readDouble();
        this.min_price = in.readDouble();
        this.order_count = in.readInt();
        this.order_num = in.readInt();
        this.orderdate = in.readInt();
        this.price = in.readDouble();
        this.profit = in.readDouble();
        this.starcode = in.readString();
    }

    public static final Creator<IncomeReturnBean> CREATOR = new Creator<IncomeReturnBean>() {
        @Override
        public IncomeReturnBean createFromParcel(Parcel source) {
            return new IncomeReturnBean(source);
        }

        @Override
        public IncomeReturnBean[] newArray(int size) {
            return new IncomeReturnBean[size];
        }
    };
}
