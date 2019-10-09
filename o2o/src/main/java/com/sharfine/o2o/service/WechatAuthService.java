package com.sharfine.o2o.service;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.sharfine.o2o.dto.ImageHolder;
import com.sharfine.o2o.dto.WechatAuthExecution;
import com.sharfine.o2o.entity.WechatAuth;

public interface WechatAuthService {

	/**
	 * 
	 * @param openId
	 * @return
	 */
	WechatAuth getWechatAuthByOpenId(String openId);

	/**
	 * 
	 * @param wechatAuth
	 * @param profileImg
	 * @return
	 * @throws RuntimeException
	 */
	WechatAuthExecution register(WechatAuth wechatAuth,
			ImageHolder profileImg) throws RuntimeException;

}
