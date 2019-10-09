package com.sharfine.o2o.service;

import java.util.List;

import com.sharfine.o2o.dto.ProductCategoryExecution;
import com.sharfine.o2o.entity.ProductCategory;
import com.sharfine.o2o.exceptions.ProductCategoryOperationException;

public interface ProductCategoryService {
	/**
	 * 查询指定某个店铺下的所有商品类别信息
	 * 
	 * @param long shopId
	 * @return List<ProductCategory>
	 */
	List<ProductCategory> getByShopId(long shopId);

	/**
	 * 
	 * @param productCategory
	 * @return
	 * @throws RuntimeException
	 */
	ProductCategoryExecution batchAddProductCategory(
			List<ProductCategory> productCategoryList) throws ProductCategoryOperationException;

	/**
	 * 
	 * @param productCategoryId
	 * @param shopId
	 * @return
	 * @throws RuntimeException
	 */
	ProductCategoryExecution deleteProductCategory(long productCategoryId,
			long shopId) throws ProductCategoryOperationException;
}
