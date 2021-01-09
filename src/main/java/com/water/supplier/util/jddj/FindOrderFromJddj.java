package com.water.supplier.util.jddj;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;


public class FindOrderFromJddj {
	
	/** 时间格式化*/ 
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
     * *根据单号获取订单详细信息
     * @param request
     * @param response
     * @throws Exception
     */

    public static String findOrderFromJddj(String orderId) throws Exception {
		Date now = new Date();
		orderId = "2100585138000031";
		//2025629235000232
		// 应用授权信息
		String appKey = "c152839ee1184c36aba334e99e5cc06d";
	    String appSecret = "0b052b75bdbb4411b89c4718a230b2ae";
	    String token = "d2585bf9-4cd9-4d73-af1d-1e10ef748cb9";
	    String format = "json";
	    String v = "1.0";	    
	    String timestamp = sdf.format(now);
	    
	    JSONObject reqparams = new JSONObject();
        reqparams.put("pageNo", "1");
        reqparams.put("pageSize", "1");
        reqparams.put("orderId", orderId);
        String jd_param_json = reqparams.toString();
        
	    String url = "https://openo2o.jd.com/djapi/order/es/query";
	    
	    // 计算签名实体
	    WebRequestDTO webReqeustDTO = new WebRequestDTO();
	    webReqeustDTO.setApp_key(appKey);
	    webReqeustDTO.setFormat(format);
	    webReqeustDTO.setJd_param_json(jd_param_json);
	    webReqeustDTO.setTimestamp(timestamp);
	    webReqeustDTO.setToken(token);
	    webReqeustDTO.setV(v);
	    
	    String sign = SignUtils.getSignByMD5(webReqeustDTO, appSecret);
	    System.out.println("md5 sign:" + sign);
	    
	    
	    Map<String, Object> param = new HashMap<String, Object>();
	    param.put("token", token);
	    param.put("app_key", appKey);
	    param.put("timestamp", timestamp);
	    param.put("sign", sign);
	    param.put("format", format);
	    param.put("v", v);
	    param.put("jd_param_json", jd_param_json);
	    
	    //根据单号获取订单信息  
	    String orderInfo = HttpUtil.sendJdPostRequest(url, param);
	    System.out.println("result:" + orderInfo);
	    
	    
        
		return orderInfo;
        
        
	}
}
