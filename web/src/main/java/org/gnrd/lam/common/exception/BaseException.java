/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gnrd.lam.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 通用的异常
 *
 * @author WUGUOWEI
 * 2024年07月08日
 */
@Getter
public class BaseException extends RuntimeException {

    public BaseException(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public BaseException(ECode eCodeEnum) {
        this(HttpStatus.BAD_REQUEST, eCodeEnum);
    }

    public BaseException(HttpStatus status, ECode eCodeEnum) {
        this(status, eCodeEnum.getCode(), eCodeEnum.getMessage());
    }

    /**
     * Http请求状态码
     */
    private HttpStatus status;

    /**
     * 自定义状态码 [ 统一6位整数 ] 由请求状态码+自定义状态码
     */
    private String code;

    /**
     * 自定义相应信息
     */
    private String message;
}
