package com.sharfine.o2o.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.sharfine.o2o.dto.ImageHolder;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Position;
import net.coobird.thumbnailator.geometry.Positions;

public class ImageUtil {
	static String basePath = Thread.currentThread().getContextClassLoader()
			.getResource("").getPath();
	
	public static String generateThumbnail(ImageHolder thumbnail,String targetAddr) throws IOException {
		basePath=URLDecoder.decode(basePath,"utf-8");
		String realFileName = PathUtil.getRandomFileName();
		String extension = getFileExtension(thumbnail.getImageName());
		makeDirPath(targetAddr);
		String relativeAddr = targetAddr + realFileName + extension;
		File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
		try {
			//
			/*Thumbnails.of(thumbnail.getImage()).size(337, 640).toFile(dest);*/
			Thumbnails.of(thumbnail.getImage()).size(1000, 1000).watermark(Positions.BOTTOM_RIGHT,
					ImageIO.read(new File(basePath+"/watermark.jpg")),0.25f).outputQuality(0.25f).toFile(dest);
		} catch (IOException e) {
			throw new RuntimeException("创建缩略图失败：" + e.toString());
		}
		return relativeAddr;
	}
	public static String generateNormalImg(ImageHolder thumbnail, String targetAddr) {
		String realFileName = PathUtil.getRandomFileName();
		String extension = getFileExtension(thumbnail.getImageName());
		makeDirPath(targetAddr);
		String relativeAddr = targetAddr + realFileName + extension;
		File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
		try {
			Thumbnails.of(thumbnail.getImage()).size(337, 640).outputQuality(0.5f).toFile(dest);
		} catch (IOException e) {
			throw new RuntimeException("创建缩略图失败：" + e.toString());
		}
		return relativeAddr;
	}

	public static List<String> generateNormalImgs(List<ImageHolder> imgs, String targetAddr) {
		int count = 0;
		List<String> relativeAddrList = new ArrayList<String>();
		if (imgs != null && imgs.size() > 0) {
			makeDirPath(targetAddr);
			for (ImageHolder img : imgs) {
				String realFileName = PathUtil.getRandomFileName();
				String extension = getFileExtension(img.getImageName());
				String relativeAddr = targetAddr + realFileName + count + extension;
				File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
				count++;
				try {
					Thumbnails.of(img.getImage()).size(600, 300).outputQuality(0.5f).toFile(dest);
				} catch (IOException e) {
					throw new RuntimeException("创建图片失败：" + e.toString());
				}
				relativeAddrList.add(relativeAddr);
			}
		}
		return relativeAddrList;
	}

//创建目标路径
	private static void makeDirPath(String targetAddr) {
		String realFileParentPath = PathUtil.getImgBasePath() + targetAddr;
		File dirPath = new File(realFileParentPath);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}
	}
//.jpg扩展名
	private static String getFileExtension(String fileName) {
		
		return fileName.substring(fileName.lastIndexOf("."));
	}
}
