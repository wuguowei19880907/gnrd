package org.gnrd.lam.vo.admin;

import lombok.Getter;
import lombok.Setter;

/**
 * 角色列表记录
 *
 * @author WUGUOWEI
 * 2024年09月09日
 */
@Setter
@Getter
public class RoleItemVO {

    /**
     * 角色id
     *
     * @mock 100010
     */
    private String id;

    /**
     * 角色名
     *
     * @mock 创建xx
     */
    private String name;

    /**
     * 角色编码
     *
     * @mock common_user
     */
    private String code;

    /**
     * 状态；0-禁用 1-启用
     *
     * @mock 1
     */
    private Integer state;
}
