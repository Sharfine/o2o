package com.sharfine.o2o.service;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.sharfine.o2o.BaseTest;
import com.sharfine.o2o.dto.LocalAuthExecution;
import com.sharfine.o2o.dto.WechatAuthExecution;
import com.sharfine.o2o.entity.LocalAuth;
import com.sharfine.o2o.entity.PersonInfo;
import com.sharfine.o2o.enums.WechatAuthStateEnum;
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LocalAuthServiceTest extends BaseTest  {
	@Autowired
	private LocalAuthService localAuthService;
	@Test
	public void bind(){
		LocalAuth l = new LocalAuth();
		/*PersonInfo p = new PersonInfo();
		p.setUserId(2l);
		l.setPersonInfo(p);*/
		l.setUserId(2l);
		l.setUserName("username");
		l.setPassword("123456789");
		l.setCreateTime(new Date());
		LocalAuthExecution lae = localAuthService.bindLocalAuth(l);
		assertEquals(WechatAuthStateEnum.SUCCESS.getState(), lae.getState());
		l = localAuthService.getLocalAuthByUserId(l.getUserId());
		System.out.println(l);
	}
	@Test
	public void modify(){
		long userId = 2l;
		localAuthService.modifyLocalAuth(2l, "username", "123456789", "123456");
	}
}
