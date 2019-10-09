package com.sharfine.o2o.dao;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.sharfine.o2o.BaseTest;
import com.sharfine.o2o.entity.Area;
import com.sharfine.o2o.entity.PersonInfo;
import com.sharfine.o2o.entity.Shop;
import com.sharfine.o2o.entity.ShopCategory;

public class ShopDaoTest extends BaseTest{
	@Autowired
	private ShopDao shopDao;
	@Test
	@Ignore
	public void testQueryByShopID(){
		long shopId =15;
		Shop shop = shopDao.queryByShopId(shopId);
		System.out.println(shop);
		System.out.println(shop.getShopCategoryId());
		
		System.out.println(shop.getArea());
		System.out.println(shop.getOwnerId());
		System.out.println(shop);
	}
	@Test
	
	public void testemByID(){
		long em= 15;
		List<Shop> s = shopDao.queryByEmployeeId(14);
		System.out.println(s);
		;
	}
	@Test
	
	public void testInsertShop(){
		Shop shop = new Shop();
		PersonInfo owner = new PersonInfo();
		Area area = new Area();
		shop.setOwnerId(1l);
		
		owner.setUserId(1l);
		area.setAreaId(2);
		
		shop.setOwner(owner);
		shop.setShopCategoryId(2l);
		shop.setArea(area);
		shop.setShopName("测试店铺");
		shop.setShopDesc("mytest1");
		shop.setShopAddr("testaddr1");
		shop.setPhone("13810524526");
		shop.setShopImg("test1");
		shop.setCreateTime(new Date());
		shop.setLastEditTime(new Date());
		shop.setEnableStatus(0);
		shop.setAdvice("审核中");
		
		
		int effectedNum = shopDao.insertShop(shop);
		assertEquals(1, effectedNum);
	}
	@Test

	public void testUpdateShop(){
		Shop shop = new Shop();
		shop.setShopId(28l);
		
		shop.setShopDesc("更新的店铺1");
		shop.setShopAddr("test");
		shop.setLastEditTime(new Date());
		shop.setShopImg("111");
		int effectedNum = shopDao.updateShop(shop);
		assertEquals(1, effectedNum);
	}
	@Test
	@Ignore
	public void testBQueryShopList() throws Exception {
		Shop shop = new Shop();
		shop.setParentCategoryId(12l);
		ShopCategory sc = new ShopCategory();
		sc.setParentId(12l);
		shop.setParentCategory(sc);
		
		List<Shop> shopList = shopDao.queryShopList(shop, 0, 10);
	

}
}