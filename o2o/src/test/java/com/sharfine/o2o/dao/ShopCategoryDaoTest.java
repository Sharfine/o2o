package com.sharfine.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.sharfine.o2o.BaseTest;
import com.sharfine.o2o.entity.ShopCategory;

public class ShopCategoryDaoTest extends BaseTest {
	@Autowired
	private ShopCategoryDao shopCategoryDao;

	@Test
	public void testBQueryShopCategory() throws Exception {
		/*ShopCategory sc = new ShopCategory();
		List<ShopCategory> shopCategoryList = shopCategoryDao
				.queryShopCategory(sc);
		assertEquals(18, shopCategoryList.size());
		sc.setParentId(1L);
		shopCategoryList = shopCategoryDao.queryShopCategory(sc);
		assertEquals(1, shopCategoryList.size());*/
		List<ShopCategory> list = shopCategoryDao.queryShopCategory(null);
		System.out.println(list);
		

	}


}
