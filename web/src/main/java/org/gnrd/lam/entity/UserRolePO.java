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
@Table(name = "c_user_role")
@Setter
@Getter
public class UserRolePO extends BasePO {

    /**
     * 用户id
     */
    @Column(name = "c_user_id")
    private Long userId;

    /**
     * 角色id
     */
    @Column(name = "c_role_id")
    private Long roleId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
}
