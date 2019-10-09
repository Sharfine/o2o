package com.sharfine.o2o.web.shopadmin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.sharfine.o2o.dto.LocalAuthExecution;
import com.sharfine.o2o.entity.LocalAuth;
import com.sharfine.o2o.entity.PersonInfo;
import com.sharfine.o2o.enums.LocalAuthStateEnum;
import com.sharfine.o2o.service.LocalAuthService;
import com.sharfine.o2o.util.CodeUtil;
import com.sharfine.o2o.util.HttpServletRequestUtil;
import com.sharfine.o2o.util.MD5;

@Controller
@RequestMapping(value = "shopadmin", method = { RequestMethod.GET,
		RequestMethod.POST })
public class OwnerAuthController {
	@Autowired
	private LocalAuthService localAuthService;

	@RequestMapping(value = "/ownerlogincheck", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> ownerLoginCheck(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		boolean needVerify = HttpServletRequestUtil.getBoolean(request,
				"needVerify");
		if (needVerify && !CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}
		String userName = HttpServletRequestUtil.getString(request, "userName");
		String password = HttpServletRequestUtil.getString(request, "password");
		if (userName != null && password != null) {
			password = MD5.getMd5(password);
			LocalAuth localAuth = localAuthService
					.getLocalAuthByUserNameAndPwd(userName, password);
			if (localAuth != null) {
				System.out.println();
				if (localAuth.getPersonInfo().getShopOwnerFlag() == 1) {
					modelMap.put("success", true);
					request.getSession().setAttribute("user",
							localAuth.getPersonInfo());
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", "非管理员没有权限访问");
				}
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", "用户名或密码错误");
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "用户名和密码均不能为空");
		}
		return modelMap;
	}

	@RequestMapping(value = "/ownerregister", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> ownerRegister(HttpServletRequest request) throws IOException {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}
		ObjectMapper mapper = new ObjectMapper();
		LocalAuth localAuth = null;
		String localAuthStr = HttpServletRequestUtil.getString(request,
				"localAuthStr");
		MultipartHttpServletRequest multipartRequest = null;
		CommonsMultipartFile profileImg = null;
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (multipartResolver.isMultipart(request)) {
			multipartRequest = (MultipartHttpServletRequest) request;
			profileImg = (CommonsMultipartFile) multipartRequest
					.getFile("thumbnail");
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "上传图片不能为空");
			return modelMap;
		}
		try {
			localAuth = mapper.readValue(localAuthStr, LocalAuth.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		if (localAuth != null && localAuth.getPassword() != null
				&& localAuth.getUserName() != null) {
			try {
				PersonInfo user = (PersonInfo) request.getSession()
						.getAttribute("user");
				if (user != null && localAuth.getPersonInfo() != null) {
					localAuth.getPersonInfo().setUserId(user.getUserId());
				}
				localAuth.getPersonInfo().setShopOwnerFlag(1);
				localAuth.getPersonInfo().setAdminFlag(0);
				ImageHolder imageHolder = new ImageHolder(profileImg.getOriginalFilename(), profileImg.getInputStream());
				
				LocalAuthExecution le = localAuthService.register(localAuth,
						imageHolder);
				if (le.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", le.getStateInfo());
				}
			} catch (RuntimeException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入注册信息");
		}
		return modelMap;
	}

	@RequestMapping(value = "/bindlocalauth", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> bindLocalAuth(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}
		String userName = HttpServletRequestUtil.getString(request, "userName");
		String password = HttpServletRequestUtil.getString(request, "password");
		PersonInfo user = (PersonInfo) request.getSession()
				.getAttribute("user");
		if (userName != null && password != null && user != null
				&& user.getUserId() != null) {
			/*password = MD5.getMd5(password);*/
			LocalAuth localAuth = new LocalAuth();
			localAuth.setUserName(userName);
			localAuth.setPassword(password);
			localAuth.setUserId(user.getUserId());
			LocalAuthExecution le = localAuthService
					.bindLocalAuth(localAuth);
			if (le.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
				modelMap.put("success", true);
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", le.getStateInfo());
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "用户名和密码均不能为空");
		}
		return modelMap;
	}

	@RequestMapping(value = "/changelocalpwd", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> changeLocalPwd(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}
		String userName = HttpServletRequestUtil.getString(request, "userName");
		String password = HttpServletRequestUtil.getString(request, "password");
		String newPassword = HttpServletRequestUtil.getString(request,
				"newPassword");
		PersonInfo user = (PersonInfo) request.getSession()
				.getAttribute("user");
		long employeeId = 0;
		if (user != null && user.getUserId() != null) {
			employeeId = user.getUserId();
		} else {
			employeeId = 1;
		}
		if (userName != null && password != null && newPassword != null
				&& employeeId > 0 && !password.equals(newPassword)) {
			try {
				LocalAuthExecution le = localAuthService.modifyLocalAuth(
						employeeId, userName, password, newPassword);
				if (le.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", le.getStateInfo());
				}
			} catch (RuntimeException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入密码");
		}
		return modelMap;
	}

	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> ownerLogoutCheck(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		request.getSession().setAttribute("user", null);
		request.getSession().setAttribute("shopList", null);
		request.getSession().setAttribute("currentShop", null);
		modelMap.put("success", true);
		return modelMap;
	}
}
