package houzm.accumulation.cache.service;

import houzm.accumulation.cache.entity.TParam;

/**
 * Author: hzm_dream@163.com
 * Date:  2018/11/21 11:14
 * Modified By:
 * Description：业务处理
 */
public interface ParamService {

    public TParam findByName(String name);

    public TParam modifyParam(String name, String id);

    public TParam findById(String id);

    public void deleteById(String id);

    public TParam save(TParam param);

}
