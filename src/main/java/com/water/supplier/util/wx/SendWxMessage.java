package com.water.supplier.util.wx;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import com.water.supplier.util.wx.WeixinUtil;

/**
 * 微信推送消息
 * @author wang-ql
 *
 */
public class SendWxMessage {

	/**订单状态消息推送模板  **/
	private static String templatId = "P3Xanj20VkW52jV3jIG4kjmTBr86adhUSa_Q5GApGuU";
	
	/**
	 * 新订单推送
	 */
	public static String sendNewOrderInfo(JSONObject json) {
		StringBuffer weixinIds = new StringBuffer("oKAhQ6CXrJNZ_K-z7HmVNEw4V8Kw");
		if (StringUtils.isNotBlank(json.optString("WEIXIN_ID"))) {
			weixinIds.append(",").append(json.optString("WEIXIN_ID"));
        }
		String clickUrl = "";
        String topColor = "";
        JSONObject jsc = new JSONObject();
        
        JSONObject first = new JSONObject();
        first.put("value", "您有一条新订单（京东到家）");
        first.put("color", "#173177");
        jsc.put("first", first);
        
        JSONObject keyword1 = new JSONObject();
        keyword1.put("value", "2100483700000751");
        keyword1.put("color", "#173177");
        jsc.put("keyword1", keyword1);  //
        
        JSONObject keyword2 = new JSONObject();
        keyword2.put("value", "张学利18400604024,9054");
        keyword2.put("color", "#173177");
        jsc.put("keyword2", keyword2);
        
        JSONObject keyword3 = new JSONObject();
        keyword3.put("value", "北京丰台区四环到五环之间高立庄路康润家园13号楼401");
        keyword3.put("color", "#173177");
        jsc.put("keyword3", keyword3);
        
        JSONObject keyword4 = new JSONObject();
        keyword4.put("value", "2021-01-06 16:00:00");
        keyword4.put("color", "#173177");
        jsc.put("keyword4", keyword4);
        
        JSONObject keyword5 = new JSONObject();
        keyword5.put("value", "农夫山泉 饮用天然水1.5L*12瓶/组 ，数量*1。【JD】");
        keyword5.put("color", "#173177");
        jsc.put("keyword5", keyword5);
        
        JSONObject remark = new JSONObject();
        remark.put("value", "请您在服务之间之前完成水单配送！");
        remark.put("color", "#173177");
        jsc.put("remark", remark);
        
        JSONObject content = jsc;
        
        String result =  WeixinUtil.sendNotify("oKAhQ6CXrJNZ_K-z7HmVNEw4V8Kw", templatId, clickUrl, topColor, content);
        return result;
	}
	
	
}
