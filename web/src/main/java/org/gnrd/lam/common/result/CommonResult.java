package org.gnrd.lam.common.result;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class CommonResult<T> implements Serializable {

    private final String SUCCESS = "000000";

    private String code;

    private String message;

    private T result;

    public CommonResult(T result) {
        this.code = SUCCESS;
        this.message = "success";
        this.result = result;
    }

    public CommonResult(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
