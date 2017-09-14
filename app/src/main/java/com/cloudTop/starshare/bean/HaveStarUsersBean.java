package com.cloudTop.starshare.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sll on 2017/7/20.
 */

public class HaveStarUsersBean implements Parcelable {

    /**
     * "starcode":"1004",
     * "faccid":"15800879645",
     * "head_url":"http://tva2.sinaimg.cn/crop.0.0.180.180.180/71bf6552jw1e8qgp5bmzyj2050050aa8.jpg",
     * "nickname":"星享用戶167",
     * "ownseconds":9997,
     * "appoint":1,
     * "uid":167
     */

    private String starcode;
    private String faccid;
    private String head_url;
    private String nickname;
    private int ownseconds;
    private int appoint;
    private int uid;

    public String getStarcode() {
        return starcode;
    }

    public void setStarcode(String starcode) {
        this.starcode = starcode;
    }

    public String getFaccid() {
        return faccid;
    }

    public void setFaccid(String faccid) {
        this.faccid = faccid;
    }

    public String getHead_url() {
        return head_url;
    }

    public void setHead_url(String head_url) {
        this.head_url = head_url;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getOwnseconds() {
        return ownseconds;
    }

    public void setOwnseconds(int ownseconds) {
        this.ownseconds = ownseconds;
    }

    public int getAppoint() {
        return appoint;
    }

    public void setAppoint(int appoint) {
        this.appoint = appoint;
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
        dest.writeString(this.starcode);
        dest.writeString(this.faccid);
        dest.writeString(this.head_url);
        dest.writeString(this.nickname);
        dest.writeInt(this.ownseconds);
        dest.writeInt(this.appoint);
        dest.writeInt(this.uid);
    }

    public HaveStarUsersBean() {
    }

    protected HaveStarUsersBean(Parcel in) {
        this.starcode = in.readString();
        this.faccid = in.readString();
        this.head_url = in.readString();
        this.nickname = in.readString();
        this.ownseconds = in.readInt();
        this.appoint = in.readInt();
        this.uid = in.readInt();
    }

    public static final Creator<HaveStarUsersBean> CREATOR = new Creator<HaveStarUsersBean>() {
        @Override
        public HaveStarUsersBean createFromParcel(Parcel source) {
            return new HaveStarUsersBean(source);
        }

        @Override
        public HaveStarUsersBean[] newArray(int size) {
            return new HaveStarUsersBean[size];
        }
    };
}
