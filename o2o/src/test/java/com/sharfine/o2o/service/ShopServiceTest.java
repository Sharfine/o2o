package com.sharfine.o2o.service;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.sharfine.o2o.BaseTest;
import com.sharfine.o2o.dto.ImageHolder;
import com.sharfine.o2o.dto.ShopExecution;
import com.sharfine.o2o.entity.Area;
import com.sharfine.o2o.entity.PersonInfo;
import com.sharfine.o2o.entity.Shop;
import com.sharfine.o2o.entity.ShopCategory;
import com.sharfine.o2o.enums.ShopStateEnum;

public class ShopServiceTest extends BaseTest{
	@Autowired
	private ShopService shopService;
	@Test
	
	public  void testmodifyShop() throws FileNotFoundException{
		
		Shop shop = new Shop();
		shop.setShopId(28l);
		shop.setOwnerId(9l);
		shop.setShopName("后的店铺");
		/*File shopImg = new File("F:/新建文件夹/0bad0fda57868175d9bd21fae8b0dcbc.jpg");
		InputStream is = new FileInputStream(shopImg);
		ImageHolder imageHolder = new ImageHolder("0bad0fda57868175d9bd21fae8b0dcbc.jpg", is);
		*/
		ShopExecution shopExecution=shopService.modifyShop(shop,null);
		System.out.println(shopExecution.getShop().getShopImg());
	}
	@Test

	public  void testAddShop() throws FileNotFoundException{
		Shop shop = new Shop();
		PersonInfo owner = new PersonInfo();
		Area area = new Area();
		shop.setOwnerId(5l);
		ShopCategory sc = new ShopCategory();
		owner.setUserId(1l);
		area.setAreaId(2);
		sc.setShopCategoryId(1L);
		shop.setOwner(owner);
		shop.setShopCategory(sc);
		shop.setArea(area);
		shop.setShopName("测试店铺Service修改后");
		shop.setShopDesc("mytest1");
		shop.setShopAddr("testaddr1");
		shop.setPhone("13810524526");
		shop.setShopImg("test1");
		shop.setCreateTime(new Date());
		shop.setLastEditTime(new Date());
		shop.setEnableStatus(ShopStateEnum.CHECK.getState());
		shop.setAdvice("审核中");
		File shopImg = new File("G:/upload/111.jpg");
		InputStream is = new FileInputStream(shopImg);
		ImageHolder imageHolder = new ImageHolder(shopImg.getName(), is);
		
		ShopExecution se = shopService.addShop(shop,imageHolder);
		assertEquals(ShopStateEnum.CHECK.getState(), se.getState());
	}
	@Test
	@Ignore
	public void testGetShopList() throws Exception {
		Shop shop = new Shop();
		ShopCategory sc = new ShopCategory();
		sc.setShopCategoryId(1l);
		shop.setShopCategory(sc);
		ShopExecution s = shopService.getShopList(shop, 1, 2);
		System.out.println(s.getShopList().size()+"======"+s.getCount());
		
		
	}

}
