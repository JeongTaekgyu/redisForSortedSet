package com.example.pricecompareredis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class LowestPriceServiceImpl implements LowestPriceService {

    @Autowired
    private RedisTemplate myProdPriceRedis;

    public Set getZsetValue(String key) {
        Set myTempSet = new HashSet();

        myTempSet = myProdPriceRedis.opsForZSet().rangeWithScores(key, 0, 9);

        return myTempSet;
    }
}
