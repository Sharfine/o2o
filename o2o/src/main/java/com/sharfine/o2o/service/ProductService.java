package com.sharfine.o2o.service;

import java.io.InputStream;
import java.util.List;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.sharfine.o2o.dto.ImageHolder;
import com.sharfine.o2o.dto.ProductExecution;
import com.sharfine.o2o.entity.Product;
import com.sharfine.o2o.exceptions.ProductOperationException;

public interface ProductService {
	ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize);

	Product getProductById(long productId);

	ProductExecution addProduct(Product product, ImageHolder thumbnail,List<ImageHolder> productImgList)
			throws ProductOperationException;

	ProductExecution modifyProduct(Product product, ImageHolder thumbnail,List<ImageHolder> productImgList) throws ProductOperationException;

}
