package com.sharfine.o2o.service;

public interface CacheService {
	//一句key的前缀匹配所有以此开头的key并删除
	void removeFromCache(String keyPrefix);

}
