package com.sharfine.o2o.web.shopadmin;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/shopadmin")
public class ShopAdminController {
	@RequestMapping("/shopoperation")
	public String shopOperation(){
		//html路径
		return "shop/shopedit";
		
	}
	@RequestMapping(value = "/ownerbind", method = RequestMethod.GET)
	private String ownerBind() {
		return "shop/ownerbind";
	}
	@RequestMapping(value = "/ownerlogin")
	public String ownerLogin(HttpServletRequest request) {
		return "shop/ownerlogin";
	}
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	private String register() {
		return "shop/register";
	}
	@RequestMapping("/shoplist")
	public String shopList(){
		//html路径
		return "shop/shoplist";
		
	}
	@RequestMapping("/shopmanage")
	public String shopmanagement(){
		//html路径
		return "shop/shopmanage";
		
	}
	@RequestMapping("/productcategorymanage")
	public String productCategoryManage(){
		//html路径
		return "shop/productcategorymanage";
		
	}
	@RequestMapping("/productedit")
	public String productEdit(){
		//html路径
		return "shop/productedit";
		
	}
	@RequestMapping("/productmanage")
	public String productManage(){
		//html路径
		return "shop/productmanage";
		
	}
	@RequestMapping(value = "/changepsw", method = RequestMethod.GET)
	private String changePsw() {
		return "shop/changepsw";
	}

}
