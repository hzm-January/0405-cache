package houzm.accumulation.cache.dao;

import houzm.accumulation.cache.entity.TParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * Author: hzm_dream@163.com
 * Date:  2018/11/21 11:14
 * Modified By:
 * Description：业务处理
 */
public interface ParamDao extends JpaRepository<TParam, String> {
    /**
     * 通过名字进行查询
     * select * from T_PARAM where name = 'name';
     *
     * @param name
     * @return
     */
    public TParam findByName(String name);

    /**
     * 使用Modifying和Query组合
     * 事件更新
     *
     * @param name
     * @param id
     * @return
     */
    @Modifying
    @Transactional
//    @Query("update TParam p set p.name= ?1 where p.id= ?2")
    @Query("update TParam p set p.name= :name where p.id= :id")
    public int modifyParam(@Param(value = "name") String name, @Param(value = "id") String id);

}
