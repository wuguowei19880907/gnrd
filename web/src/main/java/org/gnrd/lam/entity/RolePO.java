package org.gnrd.lam.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "c_role")
@Setter
@Getter
public class RolePO extends BasePO {

    /**
     * 角色名称
     */
    @Column(name = "c_name")
    private String name;

    /**
     * 角色编码
     */
    @Column(name = "c_code")
    private String code;

    /**
     * 状态 0-禁用 1-启用
     */
    @Column(name = "c_state")
    private Integer state;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
}
