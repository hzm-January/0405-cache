package houzm.accumulation.cache.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Author: hzm_dream@163.com
 * Date:  2018/11/21 11:25
 * Modified By:
 * Description：
 */
@Entity
@Table(name = "T_PARAM")
public class TParam implements Serializable{

    private static final long serialVersionUID = 5404785194799835053L;
    @Id
    @Column(name = "P_ID", length = 100)
    private String id;
    @Column(name = "P_NAME")
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
