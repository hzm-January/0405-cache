package houzm.accumulation.cache.controller;

import houzm.accumulation.cache.entity.TParam;
import houzm.accumulation.cache.service.ParamService;
import houzm.cache.common.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @Autowired
    private ParamService paramService;

    @RequestMapping(value = "")
    public void cache() {
        System.out.println("cache ====");
        System.out.println(redisTemplate);
    }


    @GetMapping(value = "save")
    public void insert() {
        TParam param = new TParam();
        long id = IdWorker.id();
        param.setId(String.valueOf(id));
        param.setName("namefor".concat(param.getId()));
        paramService.save(param);
    }

    @GetMapping(value = "modify/{id}")
    public void modify(@PathVariable(value = "id") String id, @RequestParam(value = "name") String name) {
        paramService.modifyParam(name, id);
    }

    /**
     * 删除
     * @param id
     */
    @GetMapping(value = "delete/{id}")
    public void delete(@PathVariable(value = "id") String id) {
        paramService.deleteById(id);
    }

    @GetMapping(value = "find/{id}")
    public TParam findById(@PathVariable(value = "id") String id) {
        return paramService.findById(id);
    }


}
