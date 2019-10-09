package com.sharfine.o2o.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.sharfine.o2o.entity.ShopCategory;

public interface ShopCategoryService {
	public static final String SCLISTKEY = "shopcategorylist";
	/**
	 * 查询指定某个店铺下的所有商品类别信息
	 * 
	 * @param long shopId
	 * @return List<ProductCategory>
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	
	List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition) throws IOException;
	
	ShopCategory getShopCategoryById(Long shopCategoryId);
	
	/*List<ShopCategory> getFirstLevelShopCategoryList() throws IOException;
	*/
	/*List<ShopCategory> getShopCategoryList(Long parentId) throws IOException;
	*/}