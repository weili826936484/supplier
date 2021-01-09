package com.water.supplier.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.water.supplier.dao.SysShopDao;
import com.water.supplier.util.Const;
import com.water.supplier.util.ResponseUtils;

@Controller
public class SysShopController {
	
	@Resource
	private SysShopDao sysShopDao;
	
	@RequestMapping("/shop/query")
	public void queryShopList(HttpServletRequest request, HttpServletResponse response,int start, int limit) {
		
		JSONObject params = new JSONObject();
		List<JSONObject> dataList = sysShopDao.queryShopList(params, start, limit);
		
		JSONObject outdata = new JSONObject();
		outdata.put("code", Const.CODE_SUCCESS);
        outdata.put("data",  dataList);
		ResponseUtils.putJsonResponse(response, outdata); 
	}
}
