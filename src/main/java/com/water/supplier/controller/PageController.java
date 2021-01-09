package com.water.supplier.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PageController {
	
	@RequestMapping("/index")
	public String index() {
        return "redirect:/wx/tologin.html?openId=2010220"; 		
	}
	
	/**
	 * 登录页
	 * @param request
	 * @param map
	 * @param model
	 * @param openId
	 * @return
	 */
	@RequestMapping("/wx/login.html")
	public String toLogin(HttpServletRequest request, HashMap<String, Object> map, Model model, String openId){ 
		System.out.println("===========登录页");	

	    map.put("openId", openId);
        return "login";
    }
	
	/**
	 * 功能页
	 * @return
	 */
	@RequestMapping("/wx/function.html")
	public String toMenuList(){ 
		System.out.println("----------功能页");
        return "function";
    }
	
	/**
	 * 新订单页
	 * @return
	 */
	@RequestMapping("/wx/newOrderList.html")
	public String toNewOrderList(){ 
		System.out.println("----------新订单页");
        return "newOrderList";
    }
	
	/**
	 *全部订单页
	 * @return
	 */
	@RequestMapping("/wx/orderList.html")
	public String toOrderList(){ 
		System.out.println("----------全部订单页");
        return "orderList";
    }
	/**
	 *订单详情页
	 * @return
	 */
	@RequestMapping("/wx/orderDetail.html")
	public String toOrderDetail(){ 
		System.out.println("----------订单详情页");
        return "orderDetail";
    }
	
	/**
	 * 个人中心页
	 * @return
	 */
	@RequestMapping("/wx/account.html")
	public String toAccount(){ 
		System.out.println("----------个人中心页");
        return "account";
    }
	

}
