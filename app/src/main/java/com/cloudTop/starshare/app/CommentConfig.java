package com.cloudTop.starshare.app;


/**
 * Created by yiwei on 16/3/2.
 */
public class CommentConfig {
    public static enum Type{
        PUBLIC("public"), REPLY("reply");

        private String value;
        private Type(String value){
            this.value = value;
        }

    }

    public int circlePosition;
    public int commentPosition;
    public Type commentType;
//    public User replyUser;
    public long uid;
    public String user_name;
    public String symbol_name;
    public String symbol_code;
    public long Circle_id;

    @Override
    public String toString() {
        String replyUserStr = "";
//        if(replyUser != null){
//            replyUserStr = replyUser.toString();
//        }
        return "circlePosition = " + circlePosition
                + "; commentPosition = " + commentPosition
                + "; commentType ＝ " + commentType
                + "; replyUser = " + replyUserStr;
    }
}
