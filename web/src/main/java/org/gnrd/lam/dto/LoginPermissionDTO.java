package org.gnrd.lam.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class LoginPermissionDTO implements Serializable {

    /**
     * 权限名称
     */
    private String name;

    /**
     * 权限编码
     */
    private String code;
}
