package com.water.supplier.util.wx;

/**
 * Created by Administrator on 2018/3/16.
 */
public class Oaut2TokenJson {

    private String access_token;
    private int expires_in;
    private String refresh_token;
    private String openid;
    private String scope;
    private String time;
    private String unionid;
    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAccess_token() {
        return access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public String getOpenid() {
        return openid;
    }

    public String getScope() {
        return scope;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public String toString() {
        return "Oaut2TokenJson [access_token=" + access_token + ", expires_in=" + expires_in + ", refresh_token="
                + refresh_token + ", openid=" + openid + ", scope=" + scope + ", time=" + time + ", unionid=" + unionid
                + "]";
    }
    
}
