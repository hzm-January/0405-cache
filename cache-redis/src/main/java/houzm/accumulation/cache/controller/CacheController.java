package houzm.accumulation.cache.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Package: houzm.accumulation.cache.controller
 * Author: hzm_dream@163.com
 * Date: Created in 2018/11/14 10:45
 * Copyright: Copyright (c) 2018
 * Version: 0.0.1
 * Modified By:
 * Description： 缓存测试
 */
@RestController
@RequestMapping(value = "/api/cache/")
public class CacheController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @RequestMapping(value = "")
    public void cache() {
        System.out.println("cache ====");
        System.out.println(redisTemplate);
    }
}
