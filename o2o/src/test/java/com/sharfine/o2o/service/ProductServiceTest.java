package com.sharfine.o2o.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.sharfine.o2o.BaseTest;
import com.sharfine.o2o.dto.ImageHolder;
import com.sharfine.o2o.dto.ProductExecution;
import com.sharfine.o2o.entity.Product;
import com.sharfine.o2o.entity.ProductCategory;
import com.sharfine.o2o.entity.Shop;
import com.sharfine.o2o.enums.ProductStateEnum;
import com.sharfine.o2o.exceptions.ProductOperationException;
import com.sharfine.o2o.exceptions.ShopOperationException;

public class ProductServiceTest extends BaseTest {
	@Autowired
	private ProductService productService;
	@Test
	public void testAddProduct()throws ShopOperationException, FileNotFoundException{
		Product product = new Product();
		Shop shop = new Shop();
		shop.setShopId(1l);
		
		ProductCategory pc = new ProductCategory();
		pc.setProductCategoryId(9l);
		product.setShop(shop);
		product.setProductCategory(pc);
		product.setProductName("aaaa");
		product.setProductDesc("sfese");
		product.setPriority(20);
		product.setCreateTime(new Date());
		product.setEnableStatus(ProductStateEnum.SUCCESS.getState());
		File t = new File("C:/Users/Sharfine/Desktop/暴风截图2019811052036437.jpg");
		InputStream is = new FileInputStream(t);
		ImageHolder ih = new ImageHolder(t.getName(),is);
		File a = new File("G:/upload/1.jpg");
		System.out.println(1);
		InputStream is1 = new FileInputStream(a);
		File b = new File("G:/upload/111.jpg");
		InputStream is2 = new FileInputStream(b);
		List<ImageHolder> list = new ArrayList<ImageHolder>();
		System.out.println(1);
		list.add(new ImageHolder(a.getName(),is1));
		list.add(new ImageHolder(b.getName(),is2));
		System.out.println(1);
		ProductExecution pe =productService.addProduct(product, ih, list);
		System.out.println(1);
		assertEquals(ProductStateEnum.SUCCESS.getState(), pe.getState());
		
	}

}
