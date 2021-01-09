package com.water.supplier.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.water.supplier.util.ResponseUtils;
import com.water.supplier.util.jddj.FindOrderFromJddj;
import com.water.supplier.util.wx.SendWxMessage;
/**
 *        接收第三方消息推送
 * @author wang-ql
 *
 */
@Controller
public class OrderAPI {
    
	@RequestMapping("/api/jddj/djsw/newOrder")
	public void receiveOrderFromjd(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String token = request.getParameter("token");
        String app_key = request.getParameter("app_key");
        String sign = request.getParameter("sign");
        String timestamp = request.getParameter("timestamp");
        String format = request.getParameter("format");
        String v = request.getParameter("v");
        String jd_param_json = request.getParameter("jd_param_json");
        System.out.println("token=" + token);
        System.out.println("app_key=" + app_key);
        System.out.println("sign=" + sign);
        System.out.println("timestamp=" + timestamp);
        System.out.println("format=" + format);
        System.out.println("v=" + v);
        System.out.println("jd_param_json=" + jd_param_json);

        // 根据单号获取订单详细信息
        String orderInfo = FindOrderFromJddj.findOrderFromJddj("orderId");
        
        // 将订单信息保存到数据库
        
        //将新订单消息推送到指定微信用户
	    String send_result = SendWxMessage.sendNewOrderInfo(null);	    

        JSONObject expireData = new JSONObject();
        expireData.put("code", "0");
        expireData.put("message",  "success");
        expireData.put("data",  "");
        ResponseUtils.putJsonResponse(response, expireData); 
	}
}
