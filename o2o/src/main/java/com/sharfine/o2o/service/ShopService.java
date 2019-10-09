package com.sharfine.o2o.service;

import java.io.File;
import java.io.InputStream;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.sharfine.o2o.dto.ImageHolder;
import com.sharfine.o2o.dto.ShopExecution;
import com.sharfine.o2o.entity.Shop;
import com.sharfine.o2o.exceptions.ShopOperationException;

public interface ShopService {
	ShopExecution addShop(Shop shop,ImageHolder thumbnail)throws ShopOperationException ;
	/**
	 * 更新店铺信息（从店家角度）
	 * 
	 * @param areaId
	 * @param shopAddr
	 * @param phone
	 * @param shopImg
	 * @param shopDesc
	 * @return
	 * @throws RuntimeException
	 */
	ShopExecution modifyShop(Shop shop,ImageHolder thumbnail) throws ShopOperationException;
	
	/**
	 * 查询指定店铺信息
	 * 
	 * @param long
	 *            shopId
	 * @return Shop shop
	 */
	Shop getByShopId(long shopId);
	
	ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize);

	ShopExecution getByEmployeeId(long employeeId) throws RuntimeException;
}


