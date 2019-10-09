package com.sharfine.o2o.dao;

import java.util.Date;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.sharfine.o2o.BaseTest;
import com.sharfine.o2o.entity.LocalAuth;
import com.sharfine.o2o.entity.PersonInfo;
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LocalAuthDaoTest extends BaseTest {
@Autowired LocalAuthDao localAuthDao;
private static final String username = "testusername";
private static final String password = "testpassword";


	@Test
	public void testInsertLocalAuth(){
		LocalAuth localauth = new LocalAuth();
		
		
		localauth.setUserId(1l);
		
		localauth.setUserName(username);
		localauth.setPassword(password);
		localauth.setCreateTime(new Date());
		int a =localAuthDao.insertLocalAuth(localauth);
		System.out.println(a);
}

//按账号密码查信息
	@Test
	public void queryLocalAuth(){
		LocalAuth localAuth  = localAuthDao.queryLocalByUserNameAndPwd("username", "password");
		System.out.println(localAuth);
	}
	@Test
	public void queryLocalAuthbyid(){
		LocalAuth localAuth  = localAuthDao.queryLocalByUserId(1);
		System.out.println(localAuth);
	}
	@Test
	public void update(){
		Date now = new Date();
		int a  = localAuthDao.updateLocalAuth(1l, username,"123456789","59yqs2q2656l296559y9050y0l652s5l" , now);
		
	}
	
}
