package com.sharfine.o2o.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharfine.o2o.cache.JedisUtil;
import com.sharfine.o2o.dao.ShopCategoryDao;

import com.sharfine.o2o.entity.ShopCategory;

import com.sharfine.o2o.service.ShopCategoryService;
import com.sharfine.o2o.util.PathUtil;
import com.sharfine.o2o.util.ImageUtil;

@Service
public class ShopCategoryServiceImpl implements ShopCategoryService {
	@Autowired
	private JedisUtil.Strings jedisStrings;
	@Autowired
	private JedisUtil.Keys jedisKeys;
	@Autowired
	private ShopCategoryDao shopCategoryDao;
	
	
	public List<ShopCategory> getFirstLevelShopCategoryList()
			throws IOException {
		String key = SCLISTKEY;
		List<ShopCategory> shopCategoryList = null;
		ObjectMapper mapper = new ObjectMapper();
		if (!jedisKeys.exists(key)) {
			ShopCategory shopCategoryCondition = new ShopCategory();
			// 当shopCategoryId不为空的时候，查询的条件会变为 where parent_id is null
			shopCategoryCondition.setShopCategoryId(-1L);
			shopCategoryList = shopCategoryDao
					.queryShopCategory(shopCategoryCondition);
			String jsonString = mapper.writeValueAsString(shopCategoryList);
			jedisStrings.set(key, jsonString);
		} else {
			String jsonString = jedisStrings.get(key);
			JavaType javaType = mapper.getTypeFactory()
					.constructParametricType(ArrayList.class,
							ShopCategory.class);
			shopCategoryList = mapper.readValue(jsonString, javaType);
		}
		return shopCategoryList;
	}
	@Override
	public List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition)
			throws IOException {
		
		return shopCategoryDao.queryShopCategory(shopCategoryCondition);
	}
	@Override
	public ShopCategory getShopCategoryById(Long shopCategoryId) {
		List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
		
		for (ShopCategory sc : shopCategoryList) {
			if (shopCategoryId == sc.getShopCategoryId()) {
				return sc;
			}
		}
		ShopCategory sc = shopCategoryDao.queryShopCategoryById(shopCategoryId);
		if (sc != null) {
			return sc;
		} else {
			return null;
		}

	}


	
};
