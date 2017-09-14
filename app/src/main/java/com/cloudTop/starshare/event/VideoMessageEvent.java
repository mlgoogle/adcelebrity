package com.cloudTop.starshare.event;

/**
 * eventBus消息类
 * Created by Administrator on 2017/4/4.
 */
public class VideoMessageEvent {
    private final String url;
    private final String frameUrl;
    private int position;
    public VideoMessageEvent(int position, String url,String frameUrl) {
        this.position = position;
        this.url = url;
        this.frameUrl = frameUrl;
    }

    public String getFrameUrl() {
        return frameUrl;
    }

    public String getUrl() {
        return url;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
