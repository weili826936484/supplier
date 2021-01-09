package com.water.supplier.util.wx;
/**
 * Created by Administrator on 2018/3/16.
 */
public class SNSUserInfo {

    //用户标识
    private String openId;

    //用户昵称
    private String nickname;

    
    //头像
    private String headImgUrl;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


    public String getHeadImgUrl() {
        return headImgUrl;
    }

    @Override
    public String toString() {
        return "{openId:'" + openId + "', nickname:'" + nickname + "', headImgUrl:'" + headImgUrl +"'}";
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }
}
