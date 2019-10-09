package com.sharfine.o2o.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.sharfine.o2o.dao.ShopDao;
import com.sharfine.o2o.dto.ImageHolder;
import com.sharfine.o2o.dto.ShopExecution;
import com.sharfine.o2o.entity.Shop;
import com.sharfine.o2o.enums.ShopStateEnum;
import com.sharfine.o2o.exceptions.ShopOperationException;
import com.sharfine.o2o.service.ShopService;
import com.sharfine.o2o.util.ImageUtil;
import com.sharfine.o2o.util.PageCalculator;
import com.sharfine.o2o.util.PathUtil;

import ch.qos.logback.core.util.FileUtil;
@Service
public class ShopServiceImpl implements ShopService {
	@Autowired
	private ShopDao shopDao;
	
	@Override
	public ShopExecution getShopList(Shop shopCondition, int pageIndex,
			int pageSize) {
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		List<Shop> shopList = shopDao.queryShopList(shopCondition, rowIndex,
				pageSize);
		int count = shopDao.queryShopCount(shopCondition);
		ShopExecution se = new ShopExecution();
		if (shopList != null) {
			se.setShopList(shopList);
			se.setCount(count);
		} else {
			se.setState(ShopStateEnum.INNER_ERROR.getState());
		}
		return se;
	}
	@Override
	public ShopExecution getByEmployeeId(long employeeId)
			throws RuntimeException {
		List<Shop> shopList = shopDao.queryByEmployeeId(employeeId);
		ShopExecution se = new ShopExecution();
		se.setShopList(shopList);
		return se;
	}
	@Override
	@Transactional
	public ShopExecution addShop(Shop shop, ImageHolder thumbnail)throws ShopOperationException {
		//传入店铺是否是空值
		if (shop == null){
			return new ShopExecution(ShopStateEnum.NULL_SHOP_INFO);
					
		}
		try {
			//赋初始值
			shop.setEnableStatus(0);
			shop.setCreateTime(new Date());
			shop.setLastEditTime(new Date());
			//插入店铺返回成功条数
			int effectedNum=shopDao.insertShop(shop);
			if(effectedNum<=0){
				throw new ShopOperationException("店铺创建失败");
			}
			else{
				if(thumbnail.getImage() != null){
					//存储图片
					try {
						addShopImg(shop,thumbnail);
					} catch (Exception e) {
						throw new ShopOperationException("addShopImgError"+e.getMessage());
						
					}
					//存储后更新
					effectedNum =shopDao.updateShop(shop);
					if(effectedNum<=0){
						throw new ShopOperationException("图片地址更新失败");
				}
			}
			}
		} catch (Exception e) {
			throw new ShopOperationException("addShopError"+e.getMessage());
				}
		return new ShopExecution(ShopStateEnum.CHECK,shop);
	}
	private void addShopImg(Shop shop, ImageHolder thumbnail) throws IOException {
		//获取图片相对路径
		String dest = PathUtil.getShopImagePath(shop.getShopId());
		String shopImgAddr = ImageUtil.generateThumbnail(thumbnail,dest);
		shop.setShopImg(shopImgAddr);
	}
	@Override
	public Shop getByShopId(long shopId) {
		return shopDao.queryByShopId(shopId);
	}
	@Override
	@Transactional
	public ShopExecution modifyShop(Shop shop, ImageHolder thumbnail)
			throws RuntimeException {
		if (shop == null || shop.getShopId() == null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOPID);
		} else {
			
				if(thumbnail== null){
					shop.setLastEditTime(new Date());
					
					
					int effectedNum = shopDao.updateShop(shop);
					
					if (effectedNum <= 0) {
						return new ShopExecution(ShopStateEnum.INNER_ERROR);
					} else {// 创建成功
						shop = shopDao.queryByShopId(shop.getShopId());
						return new ShopExecution(ShopStateEnum.SUCCESS, shop);
					}
				} 
			else{
				try {
				if (thumbnail.getImage() != null) {
					Shop tempShop = shopDao.queryByShopId(shop.getShopId());
					if (tempShop.getShopImg() != null) {
						PathUtil.deleteFile(tempShop.getShopImg());
					}
					addShopImg(shop,thumbnail);
				}
				//更新
				shop.setLastEditTime(new Date());
				
			
				int effectedNum = shopDao.updateShop(shop);
				
				if (effectedNum <= 0) {
					return new ShopExecution(ShopStateEnum.INNER_ERROR);
				} else {// 创建成功
					shop = shopDao.queryByShopId(shop.getShopId());
					return new ShopExecution(ShopStateEnum.SUCCESS, shop);
				}
			} catch (Exception e) {
				throw new RuntimeException("modifyShop error: "
						+ e.getMessage());
			}}
		}
	}
}

