package org.gnrd.lam.common.exception;

import lombok.Getter;

@Getter
public enum ECode {

    E_000001("000001", "用户名或密码错误"),
    E_999998("999998", "参数检验异常"),
    E_999999("999999", "业务异常");

    private String code;

    private String message;

    ECode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
