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
@Table(name = "c_role_permission")
@Setter
@Getter
public class RolePermissionPO extends BasePO {

    /**
     * 角色id
     */
    @Column(name = "c_role_id")
    private Long roleId;

    /**
     * 权限id
     */
    @Column(name = "c_permission_id")
    private Long permissionId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
}
