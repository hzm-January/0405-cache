package houzm.accumulation.cache.service;

import houzm.accumulation.cache.dao.ParamDao;
import houzm.accumulation.cache.entity.TParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Author: hzm_dream@163.com
 * Date:  2018/11/21 11:27
 * Modified By:
 * Description：
 */
@Service(value = "paramService")
public class ParamServiceImpl implements ParamService{
    @Autowired
    private ParamDao paramDao;
    @Override
    public TParam findByName(String name) {
        return paramDao.findByName(name);
    }

    @Override
    @CachePut(value = "param", key = "#id")
    public TParam modifyParam(String name, String id) {
        paramDao.modifyParam(name, id);
        TParam param = new TParam();
        param.setId(id);
        param.setName(name);
        return param;
    }

    @Override
    @Cacheable(value = "param", key = "#id")
    public TParam findById(String id) {
        System.out.println("----------invoke findbyid-----------");
        return paramDao.findById(id).get();
    }

    /**
     * allEntries
     * beforeInvocation 是否清空所有缓存内容，缺省为 false，如果指定为 true，则方法调用后将立即清空所有缓存
     * @param id 是否在方法执行前就清空，缺省为 false，如果指定为 true，则在方法还没有执行的时候就清空缓存，缺省情况下，如果方法执行抛出异常，则不会清空缓存
     */
    @Override
    @CacheEvict(value = "param", key = "#id", allEntries = false, beforeInvocation = false)
    public void deleteById(String id) {
        paramDao.deleteById(id);
    }

    @Override
    @Cacheable(value = "param", key = "#param.id")
    public TParam save(TParam param) {
        return paramDao.save(param);
    }
}
