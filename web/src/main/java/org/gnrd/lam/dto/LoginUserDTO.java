package org.gnrd.lam.dto;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class LoginUserDTO implements Serializable {

    /**
     * 用户名称
     */
    private String name;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 状态 0-禁用 1-启用
     */
    private Integer state;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
