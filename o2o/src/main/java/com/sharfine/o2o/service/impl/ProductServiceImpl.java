package com.sharfine.o2o.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.sharfine.o2o.dao.ProductDao;
import com.sharfine.o2o.dao.ProductImgDao;
import com.sharfine.o2o.dto.ImageHolder;
import com.sharfine.o2o.dto.ProductExecution;
import com.sharfine.o2o.entity.Product;
import com.sharfine.o2o.entity.ProductImg;
import com.sharfine.o2o.enums.ProductStateEnum;
import com.sharfine.o2o.exceptions.ProductOperationException;
import com.sharfine.o2o.service.ProductService;
import com.sharfine.o2o.util.PathUtil;
import com.sharfine.o2o.util.ImageUtil;
import com.sharfine.o2o.util.PageCalculator;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	private ProductDao productDao;
	@Autowired
	private ProductImgDao productImgDao;

	@Override
	public ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize) {
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		List<Product> productList = productDao.queryProductList(productCondition, rowIndex, pageSize);
		int count = productDao.queryProductCount(productCondition);
		ProductExecution pe = new ProductExecution();
		pe.setProductList(productList);
		pe.setCount(count);
		return pe;
	}

	@Override
	public Product getProductById(long productId) {
		return productDao.queryProductByProductId(productId);
	}

	@Override
	
	public ProductExecution addProduct(Product product, ImageHolder thumbnail,
			List<ImageHolder> productImgHolderList) throws ProductOperationException {
		if (product != null && product.getShop() != null && product.getShop().getShopId() != null) {
			product.setCreateTime(new Date());
			product.setLastEditTime(new Date());
			product.setEnableStatus(1);
			if (thumbnail != null) {
				try {
					addThumbnail(product, thumbnail);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				int effectedNum = productDao.insertProduct(product);
				if (effectedNum <= 0) {
					throw new ProductOperationException("创建商品失败");
				}
			} catch (Exception e) {
				throw new ProductOperationException("创建商品失败:" + e.toString());
			}
			if (productImgHolderList != null && productImgHolderList.size() > 0) {
				addProductImgs(product, productImgHolderList);
			}
			return new ProductExecution(ProductStateEnum.SUCCESS, product);
		} else {
			return new ProductExecution(ProductStateEnum.EMPTY);
		}
	}

	@Override
	@Transactional
	public ProductExecution modifyProduct(Product product, ImageHolder thumbnail,List<ImageHolder> productImgHolderList) throws RuntimeException {
		if (product != null && product.getShop() != null && product.getShop().getShopId() != null) {
			product.setLastEditTime(new Date());
			if (thumbnail != null) {
				Product tempProduct = productDao.queryProductByProductId(product.getProductId());
				if (tempProduct.getImgAddr() != null) {
					PathUtil.deleteFile(tempProduct.getImgAddr());
				}
				try {
					addThumbnail(product, thumbnail);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (productImgHolderList != null && productImgHolderList.size() > 0) {
				deleteProductImgs(product.getProductId());
				addProductImgs(product, productImgHolderList);
			}
			try {
				int effectedNum = productDao.updateProduct(product);
				if (effectedNum <= 0) {
					throw new RuntimeException("更新商品信息失败");
				}
				return new ProductExecution(ProductStateEnum.SUCCESS, product);
			} catch (Exception e) {
				throw new RuntimeException("更新商品信息失败:" + e.toString());
			}
		} else {
			return new ProductExecution(ProductStateEnum.EMPTY);
		}
	}

	private void addProductImgs(Product product, List<ImageHolder> productImgHolderList) {
		String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
		List<ProductImg> productImgList = new ArrayList<ProductImg>();
		
			for (ImageHolder productImgHolder: productImgHolderList) {
				String imgAddr;
				try {
					imgAddr = ImageUtil.generateThumbnail(productImgHolder, dest);
					ProductImg productImg = new ProductImg();
					productImg.setImgAddr(imgAddr);
					productImg.setProductId(product.getProductId());
					productImg.setCreateTime(new Date());
					productImgList.add(productImg);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
			try {
				int effectedNum = productImgDao.batchInsertProductImg(productImgList);
				if (effectedNum <= 0) {
					throw new RuntimeException("创建商品详情图片失败");
				}
			} catch (Exception e) {
				throw new RuntimeException("创建商品详情图片失败:" + e.toString());
			}
		
	}

	private void deleteProductImgs(long productId) {
		List<ProductImg> productImgList = productImgDao.queryProductImgList(productId);
		for (ProductImg productImg : productImgList) {
			PathUtil.deleteFile(productImg.getImgAddr());
		}
		productImgDao.deleteProductImgByProductId(productId);
	}

	private void addThumbnail(Product product, ImageHolder thumbnail) throws IOException {
		String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
		String thumbnailAddr = ImageUtil.generateThumbnail(thumbnail, dest);
		product.setImgAddr(thumbnailAddr);
	}
}
