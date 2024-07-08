package org.gnrd.lam.ro;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
public class LoginRO {

    /**
     * 用户名
     *
     * @mock aAa
     */
    @NotBlank(message = "用户名不可位空")
    private String username;

    /**
     * 登录密码
     *
     * @mock 123456
     */
    @NotBlank(message = "用户名不可位空")
    private String password;
}
