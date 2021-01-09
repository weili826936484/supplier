package com.water.supplier.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import com.water.supplier.dao.SysShopDao;
import com.water.supplier.util.Const;
import com.water.supplier.util.ResponseUtils;
import com.water.supplier.util.jddj.FindOrderFromJddj;

@Controller
public class SysShopController {
	
	@Resource
	private SysShopDao sysShopDao;
	
	/**
	 * 查询所有商户
	 * @param request
	 * @param response
	 * @param start
	 * @param limit
	 * @throws Exception 
	 */
	@RequestMapping("/shop/query")
	public void queryShopList(HttpServletRequest request, HttpServletResponse response,int start, int limit) throws Exception {
		 
		
		JSONObject params = new JSONObject();
		List<JSONObject> dataList = sysShopDao.queryShopList(params, start, limit);
		String orderInfo = FindOrderFromJddj.findOrderFromJddj("orderId");
		JSONObject json = new JSONObject(orderInfo);
		String resultData = json.optString("data");
		JSONObject datajson = new JSONObject(resultData);
		String result = datajson.optString("result");
		JSONObject outdata = new JSONObject();
		outdata.put("code", Const.CODE_SUCCESS);
        outdata.put("data",  result);
		ResponseUtils.putJsonResponse(response, outdata); 
	}
	
	/**
	 * 新增商户
	 * @param request
	 * @param response
	 */
	@RequestMapping("/shop/save")
	@Transactional
	public void saveShopInfo(HttpServletRequest request, HttpServletResponse response) {
		JSONObject outdata = new JSONObject();
		
		String shopInfo = request.getParameter("shopInfo");
		if(StringUtils.isBlank(shopInfo)) {
			outdata.put("code", Const.CODE_ERR_PARAM);
	        outdata.put("msg",  "参数错误。");
	        ResponseUtils.putJsonResponse(response, outdata); 
	        return;
		}
		
		try {
			JSONObject params = new JSONObject();
			params.put("shop_code", "10001");
			params.put("shop_source", "京东到家");
			params.put("shop_name", "哇哈哈经销处（安贞店）");
			params.put("shop_address", "北京市朝阳区安贞里街道腾飞大街204号");
			params.put("shop_leader", "张三丰");
			params.put("shop_tel", "13923456789");
			params.put("shop_status", "1");
			params.put("create_by", "admin");
			params.put("update_by", "admin");

			int n = sysShopDao.insertShop(params);
			if (n == 1) {
				outdata.put("code", Const.CODE_SUCCESS);
		        outdata.put("msg",  "编辑成功。");
			} else {
				outdata.put("code", Const.CODE_ERR_EXP);
		        outdata.put("msg",  "异常错误。");
			}
		} catch (Exception e) {
			outdata.put("code", Const.CODE_ERR_EXP);
	        outdata.put("msg",  "异常错误。");
		}
		
		ResponseUtils.putJsonResponse(response, outdata); 
	}
}
