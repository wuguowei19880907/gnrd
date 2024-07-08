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
@Table(name = "c_user")
@Setter
@Getter
public class UserPO extends BasePO {

    /**
     * 用户名称
     */
    @Column(name = "c_name")
    private String name;

    /**
     * 手机号
     */
    @Column(name = "c_phone")
    private String phone;

    /**
     * 登录密码
     */
    @Column(name = "c_password")
    private String password;

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
