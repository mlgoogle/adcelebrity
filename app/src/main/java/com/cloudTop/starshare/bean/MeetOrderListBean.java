package com.cloudTop.starshare.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 约见订单列表
 * Created by sll on 2017/7/20.
 */

public class MeetOrderListBean implements Parcelable {

    /**
     * "appoint_time":"2017-06-19",
     * "comment":"sdf..",
     * "headurl":"http://tva2.sinaimg.cn/crop.0.0.180.180.180/71bf6552jw1e8qgp5bmzyj2050050aa8.jpg",
     * "id":1,
     * "meet_city":"石家庄市",
     * "meet_type":1,      #'1-待确认 2-已拒绝 3-已完成 4-已同意；',
     * "mid":1,
     * "name":"拍mv",
     * "nickname":"星享用戶",
     * "order_time":"2017-06-19 15:51:10",
     * "starcode":"1001",
     * "uid":143
     */

    private String appoint_time;
    private String comment;
    private String headurl;
    private int id;
    private String meet_city;
    private int meet_type;
    private int mid;
    private String name;
    private String nickname;
    private String order_time;
    private String starcode;
    private int uid;

    public String getAppoint_time() {
        return appoint_time;
    }

    public void setAppoint_time(String appoint_time) {
        this.appoint_time = appoint_time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getHeadurl() {
        return headurl;
    }

    public void setHeadurl(String headurl) {
        this.headurl = headurl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMeet_city() {
        return meet_city;
    }

    public void setMeet_city(String meet_city) {
        this.meet_city = meet_city;
    }

    public int getMeet_type() {
        return meet_type;
    }

    public void setMeet_type(int meet_type) {
        this.meet_type = meet_type;
    }

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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getStarcode() {
        return starcode;
    }

    public void setStarcode(String starcode) {
        this.starcode = starcode;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.appoint_time);
        dest.writeString(this.comment);
        dest.writeString(this.headurl);
        dest.writeInt(this.id);
        dest.writeString(this.meet_city);
        dest.writeInt(this.meet_type);
        dest.writeInt(this.mid);
        dest.writeString(this.name);
        dest.writeString(this.nickname);
        dest.writeString(this.order_time);
        dest.writeString(this.starcode);
        dest.writeInt(this.uid);
    }

    public MeetOrderListBean() {
    }

    protected MeetOrderListBean(Parcel in) {
        this.appoint_time = in.readString();
        this.comment = in.readString();
        this.headurl = in.readString();
        this.id = in.readInt();
        this.meet_city = in.readString();
        this.meet_type = in.readInt();
        this.mid = in.readInt();
        this.name = in.readString();
        this.nickname = in.readString();
        this.order_time = in.readString();
        this.starcode = in.readString();
        this.uid = in.readInt();
    }

    public static final Creator<MeetOrderListBean> CREATOR = new Creator<MeetOrderListBean>() {
        @Override
        public MeetOrderListBean createFromParcel(Parcel source) {
            return new MeetOrderListBean(source);
        }

        @Override
        public MeetOrderListBean[] newArray(int size) {
            return new MeetOrderListBean[size];
        }
    };
}
