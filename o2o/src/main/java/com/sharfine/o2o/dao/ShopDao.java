package com.sharfine.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sharfine.o2o.entity.Shop;

public interface ShopDao {
	//插入店铺
	int insertShop(Shop shop);
    //更新店铺
	int updateShop(Shop shop);
	//通过shopid查询店铺
	Shop queryByShopId(long shopId);
	/**
	 * 分页查询店铺,可输入的条件有：店铺名（模糊），店铺状态，店铺Id,店铺类别,区域ID
	 * 
	 * @param shopCondition
	 * @param rowIndex
	 * @param pageSize
	 * @return
	 */
	List<Shop> queryShopList(@Param("shopCondition") Shop shopCondition,
			@Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);
	/**
	 * 返回queryShopList总数
	 * 
	 * @param shopCondition
	 * @return
	 */
	int queryShopCount(@Param("shopCondition") Shop shopCondition);
	
	List<Shop> queryByEmployeeId(long employeeId);

}
