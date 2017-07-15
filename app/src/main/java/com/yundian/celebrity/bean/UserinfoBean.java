package com.yundian.celebrity.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/5/9.
 */

public class UserinfoBean implements Parcelable {
    /**
     "agentName": "编程浪子",
     "avatar_Large": "http://tva2.sinaimg.cn/crop.0.0.180.180.180/71bf6552jw1e8qgp5bmzyj2050050aa8.jpg",
     "balance": 0,
     "id": 145,
     "phone": "17682310986",
     "type": 0
     */

    private double balance;
    private int id;
    private String phone;
    private int type;
    private String agentName;
    private String avatar_Large;

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getAvatar_Large() {
        return avatar_Large;
    }

    public void setAvatar_Large(String avatar_Large) {
        this.avatar_Large = avatar_Large;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.balance);
        dest.writeInt(this.id);
        dest.writeString(this.phone);
        dest.writeInt(this.type);
        dest.writeString(this.agentName);
        dest.writeString(this.avatar_Large);
    }

    public UserinfoBean() {
    }

    protected UserinfoBean(Parcel in) {
        this.balance = in.readDouble();
        this.id = in.readInt();
        this.phone = in.readString();
        this.type = in.readInt();
        this.agentName = in.readString();
        this.avatar_Large = in.readString();
    }

    public static final Creator<UserinfoBean> CREATOR = new Creator<UserinfoBean>() {
        @Override
        public UserinfoBean createFromParcel(Parcel source) {
            return new UserinfoBean(source);
        }

        @Override
        public UserinfoBean[] newArray(int size) {
            return new UserinfoBean[size];
        }
    };

    @Override
    public String toString() {
        return "UserinfoBean{" +
                "balance=" + balance +
                ", id=" + id +
                ", phone='" + phone + '\'' +
                ", type=" + type +
                ", agentName='" + agentName + '\'' +
                ", avatar_Large='" + avatar_Large + '\'' +
                '}';
    }
}
