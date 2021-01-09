package com.water.supplier.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.water.supplier.util.ResponseUtils;

/**
 *  接收Token
 * @author wang-ql
 *
 */
@Controller
public class TokenAPI {

	  	@RequestMapping("/api/jddjToken/receive")
	    public void receiveToken(HttpServletRequest request, HttpServletResponse response) {
	        String token = request.getParameter("token");
	        System.out.println("Token=" + token);
	        System.out.println("将token保存到数据库。。。。。。。。。");
	        
	        JSONObject outData = new JSONObject();
	        outData.put("code", "0");
	        outData.put("message",  "success");
	        outData.put("data",  "");
	        
	        ResponseUtils.putJsonResponse(response, outData); 
	    }
}
