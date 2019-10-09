package com.sharfine.o2o.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sharfine.o2o.cache.JedisUtil;
import com.sharfine.o2o.service.CacheService;
@Service
public class CacheServiceImpl implements CacheService {
	@Autowired
	private JedisUtil.Keys JedisKeys;
	@Override
	public void removeFromCache(String keyPrefix) {
		Set<String> keySet = JedisKeys.keys(keyPrefix+"*");
		for (String key : keySet) {
			JedisKeys.del(key);
		}

	}

}
