package com.water.supplier.util.wx;


import org.json.JSONObject;

import com.water.supplier.util.Cfg;

public class WeixinUtil {
    
    /**
     * 
     * <B>方法名称：</B>微信接口令牌地址<BR>
     * <B>概要说明：</B><BR>
     * @return TokenJson 令牌 
     */
    private static TokenJson getAccess_token() {
        try {
            JSONObject rqJsonObject = HttpGetRequest.doGet(Cfg.getConfig("web.access_token_url").replace("APPID", Cfg.getConfig("web.appId")).replace("APPSECRET", Cfg.getConfig("web.appSecret")));

            TokenJson tokenJson = new TokenJson();
            tokenJson.setAccess_token(rqJsonObject.optString("access_token"));
            tokenJson.setExpires_in(7200);
            return tokenJson;
        } catch (Exception e) {
            e.printStackTrace();
            
            return null;
        }
    }
        
    /**
     * 设置令牌
     * 
     * @author
     * @date 创建时间 2016年7月28日 上午11:25:01
     * @return boolean
     */
    private static boolean setToken() {
        
        TokenJson tokenJson = getAccess_token();
        if (tokenJson != null) {
            WxParams.token = tokenJson.getAccess_token();
            WxParams.tokenTime = System.currentTimeMillis() + "";
            WxParams.tokenExpires = tokenJson.getExpires_in() + "";
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * 
     * <B>方法名称：</B>获取getAccessToken<BR>
     * <B>概要说明：</B><BR>
     * @return WxParams.token 
     */
    public static synchronized String getAccessToken() {     
        
        if (WxParams.token == null || WxParams.tokenTime == null || WxParams.tokenExpires == null) {            
            setToken();
            return WxParams.token;
            
        } else {
            long tokenTimeLong = Long.parseLong(WxParams.tokenTime);
            long tokenExpiresLong = Long.parseLong(WxParams.tokenExpires);
            long differ = (System.currentTimeMillis() - tokenTimeLong) / 1000;
           
            if (differ > (tokenExpiresLong - 1800)) {
                // token失效                
                setToken();                
                return WxParams.token;
            } else {
                return WxParams.token; 
            }
        } 
    }
    
    /**
     * 网页授权认证
     * @param appId 微信用户ID
     * @param appSecret 微信公众号串
     * @param code 获取的code
     * @return Oaut2TokenJson
     */
    public static Oaut2TokenJson getOauth2AccessToken(String appId, String appSecret, String code) {

       String  requestUrl = Cfg.getConfig("web.oauthAccessTokenUrl").replace("APPID", appId).replace("SECRET", appSecret).replace("CODE", code);
       try {
           
             //发送请求获取网页授权凭证
               JSONObject jsonObject = HttpGetRequest.doGet(requestUrl);
               Oaut2TokenJson wxo = new Oaut2TokenJson();
               wxo.setAccess_token(jsonObject.optString("access_token"));
               wxo.setExpires_in(7200);
               wxo.setRefresh_token(jsonObject.optString("refresh_token"));
               wxo.setOpenid(jsonObject.optString("openid"));
               wxo.setScope(jsonObject.optString("scope"));
               wxo.setUnionid(jsonObject.optString("unionid"));

               return wxo;

             
        } catch (Exception e) {
             e.printStackTrace();
             return null;
        }
        

    }
    
    /**
     * 获取用户的基本信息
     * @param accessToken 网页授权token
     * @param openId 微信用户ID
     * @return SNSUserInfo
     */
    public static SNSUserInfo getSNSUserInfo(String accessToken, String openId) {
        String requestUrl = Cfg.getConfig("web.oauthGetUserInfoUrl").replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId);
        
        try {
            
            //通过网页授权获取用户信息
            JSONObject jsonObject = HttpGetRequest.doGet(requestUrl);
            SNSUserInfo snsuserinfo = new SNSUserInfo();
            snsuserinfo.setOpenId(jsonObject.optString("openid"));
            snsuserinfo.setNickname(jsonObject.optString("nickname"));
            
            //snsuserinfo.setPrivilegeList(JSONArray._fromArray(jsonObject.getString("privilege"),String.class));
            return snsuserinfo;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
    }
    /**
     ** 发送通知给用户
     * @param touser 用户ID
     * @param templatId 模板ID
     * @param clickurl 点击地址
     * @param topcolor 颜色
     * @param data 内容
     * @return String
     */
    public static String sendNotify(String touser, String templatId, String clickurl, String topcolor, JSONObject data) {
        
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";     
          
        try {
           //将更新后的access_token,替换上去
           requestUrl = requestUrl.replace("ACCESS_TOKEN", getAccessToken());
                
           JSONObject json = new JSONObject();
           json.put("touser", touser);
           json.put("template_id", templatId);
           json.put("url", clickurl);
           json.put("color", topcolor);
           json.put("data", data);
                
           //通过网页授权获取用户信息
           JSONObject jsonObject = HttpGetRequest.doGet(requestUrl, "POST", json.toString());
            System.out.println(jsonObject);  
            if ("ok".equals(jsonObject.optString("errmsg"))) {  //如果为errmsg为ok，则代表发送成功，公众号推送信息给用户了。
                    return "success";
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return "error";
        
    }

}
