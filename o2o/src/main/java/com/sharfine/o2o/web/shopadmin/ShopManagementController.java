package com.sharfine.o2o.web.shopadmin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharfine.o2o.dto.ImageHolder;
import com.sharfine.o2o.dto.ShopExecution;
import com.sharfine.o2o.entity.Area;
import com.sharfine.o2o.entity.PersonInfo;
import com.sharfine.o2o.entity.Shop;
import com.sharfine.o2o.entity.ShopCategory;
import com.sharfine.o2o.enums.ShopStateEnum;
import com.sharfine.o2o.exceptions.ShopOperationException;
import com.sharfine.o2o.service.AreaService;
import com.sharfine.o2o.service.ShopCategoryService;
import com.sharfine.o2o.service.ShopService;
import com.sharfine.o2o.util.CodeUtil;
import com.sharfine.o2o.util.HttpServletRequestUtil;
import com.sharfine.o2o.util.PathUtil;

@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {
	@Autowired
	private ShopService shopService;
	@Autowired
	private ShopCategoryService shopCategoryService;
	@Autowired
	private AreaService areaService;
	@RequestMapping(value = "/getshopmanagementinfo", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopManagementInfo(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		
		Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		if(shopId<0){
			Object currentShopObj = request.getSession().getAttribute("currentShop");
		
			if(currentShopObj==null){
				modelMap.put("redirect",true);
				modelMap.put("url", "/o2o/shopadmin/shoplist");//?
			}else{
				Shop currentShop = (Shop) currentShopObj;
				modelMap.put("redirect",false);
				modelMap.put("shopId",currentShop.getShopId());
			}
	}else{
		Shop currentShop = new Shop();
		currentShop.setShopId(shopId);
		request.getSession().setAttribute("currentShop",currentShop);
		modelMap.put("redirect",false);
	}
		System.out.println(modelMap);
		return modelMap;
		}
	@RequestMapping(value = "/getshoplist", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopList(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		PersonInfo user = (PersonInfo) request.getSession()
				.getAttribute("user");
		long employeeId = user.getUserId();
		/*if (hasAccountBind(request, employeeId)) {
			modelMap.put("hasAccountBind", true);
		} else {
			modelMap.put("hasAccountBind", false);
		}*/
		List<Shop> list = new ArrayList<Shop>();
		try {
			ShopExecution shopExecution = shopService
					.getByEmployeeId(employeeId);
			list = shopExecution.getShopList();
			modelMap.put("shopList", list);
			modelMap.put("user", user);
			modelMap.put("success", true);
			// 列出店铺成功之后，将店铺放入session中作为权限验证依据，即该帐号只能操作它自己的店铺
			request.getSession().setAttribute("shopList", list);
		} catch (Exception e) {
			e.printStackTrace();
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
		}
		return modelMap;
	}


	@RequestMapping(value = "/getshopbyid", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopById(
			HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		
		
		Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		if (shopId != null && shopId > -1) {
			Shop shop = shopService.getByShopId(shopId);
			
			modelMap.put("shop", shop);
			request.getSession().setAttribute("currentShop",shop);
			try {
				List<Area>  areaList = areaService.getAreaList();
				/*List<ShopCategory> shopCategoryList = shopCategoryService
						.getShopCategoryList(new ShopCategory());
				modelMap.put("shopCategoryList", shopCategoryList);*/
				shop.getShopCategory().setShopCategoryName(
						shopCategoryService.getShopCategoryById(
								shop.getShopCategory().getShopCategoryId())
								.getShopCategoryName());
				modelMap.put("areaList", areaList);
				modelMap.put("success", true);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", "错误!");
			}
			
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty shopId");
		}
		return modelMap;
	}
	@RequestMapping(value="/getshopinitinfo")
	@ResponseBody
	private Map<String,Object> getShopInitInfo(){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
		List<Area> areaList = new ArrayList<Area>();
		try {
			shopCategoryList = shopCategoryService
					.getShopCategoryList(new ShopCategory());
			areaList = areaService.getAreaList();
			modelMap.put("shopCategoryList", shopCategoryList);
			modelMap.put("areaList", areaList);
			modelMap.put("success", true);
		} catch (IOException e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "错误啦!");
		}
		
		
		return modelMap;
	}
	@RequestMapping(value = "/modifyshop", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyShop(HttpServletRequest request) {
		
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入了错误的验证码2");
			return modelMap;
		}
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		MultipartHttpServletRequest multipartRequest = null;
		CommonsMultipartFile shopImg = null;
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (multipartResolver.isMultipart(request)) {
			multipartRequest = (MultipartHttpServletRequest) request;
			shopImg = (CommonsMultipartFile) multipartRequest
					.getFile("shopImg");
		}
		try {
			shop = mapper.readValue(shopStr, Shop.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "数据读取错误");
			return modelMap;
		}
		Shop currentShop = (Shop) request.getSession().getAttribute(
				"currentShop");
		
		shop.setShopId(currentShop.getShopId());
		if (shop != null && shop.getShopId() != null) {
			PersonInfo owner = new PersonInfo();
			owner.setUserId(1l);
			shop.setOwner(owner);
			ShopExecution se;
			try {
				if(shopImg==null){
					 se = shopService.modifyShop(shop, null);
				}else{
						ImageHolder imageHolder = new ImageHolder(shopImg.getOriginalFilename(), shopImg.getInputStream());
						
						 se = shopService.modifyShop(shop,imageHolder);
					}
				if (se.getState() == ShopStateEnum.SUCCESS
						.getState()) {
					modelMap.put("success", true);
					
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", "店铺状态错误");
				}
			} catch (IOException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", "运行时错误");
				
			}catch (ShopOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", "店铺操作错误");
				} 
			System.out.println(modelMap);
			return modelMap;
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入店铺信息");
			return modelMap;
		}
		
	}
	@RequestMapping(value="/registershop" ,method = RequestMethod.POST)
	@ResponseBody
	private Map<String,Object> registerShop(HttpServletRequest request){
		Map<String,Object> Map = new HashMap<String,Object>();
		//接收并转化对应参数包括店铺信息及图片
		if (!CodeUtil.checkVerifyCode(request)) {
			Map.put("success", false);
			Map.put("errMsg", "输入了错误的验证码1");
			return Map;
		}
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		try {
			shop = mapper.readValue(shopStr, Shop.class);
		} catch (Exception e) {
			Map.put("success", false);
			Map.put("errMs", "数据读取错误");
			return Map;
		}
		CommonsMultipartFile shopImg = null;
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (multipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			shopImg = (CommonsMultipartFile) multipartRequest
					.getFile("shopImg");
		} else {
			Map.put("success", false);
			Map.put("errM", "上传图片不能为空");
			return Map;
		}
		
		//注册店铺
		if (shop != null && shopImg != null) {
			
				
				PersonInfo user = (PersonInfo) request.getSession()
						.getAttribute("user");
				shop.setOwnerId(user.getUserId());
				ShopExecution se;
					try {
						ImageHolder imageHolder = new ImageHolder(shopImg.getOriginalFilename(), shopImg.getInputStream());
						se = shopService.addShop(shop, imageHolder);
					
				if (se.getState() == ShopStateEnum.CHECK.getState()) {
					Map.put("success", true);
				@SuppressWarnings("unchecked")
					List<Shop> shopList = (List<Shop>) request.getSession()
							.getAttribute("shopList");
				    if (shopList != null && shopList.size() > 0) {
					shopList.add(se.getShop());
					request.getSession().setAttribute("shopList", shopList);
				    } else {
					shopList = new ArrayList<Shop>();
					shopList.add(se.getShop());
					request.getSession().setAttribute("shopList", shopList);
				}
				
					
				} else {
					Map.put("success", false);
					Map.put("errMsg", "店铺状态错误");
				}} 
				catch (ShopOperationException e) {
					Map.put("success", false);
					Map.put("errMsg", "店铺操作错误");	
				}
			    catch (IOException e) {
				Map.put("success", false);
				Map.put("errMsg",  "运行时错误");
			    }
					return Map;
		} else {
			Map.put("success", false);
			Map.put("errMsg", "请输入店铺信息");
			return Map;
		}
		
	}
		//返回结果
	
	

}
