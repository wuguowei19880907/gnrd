/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * Copyright 2023 gnrd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gnrd.lam.common.constants;

public enum UserStatusEnum {

    /**
     * 禁用
     */
    DISABLED(Constants.DISABLED),
    /**
     * 启用
     */
    ENABLED(Constants.ENABLED);

    private final int value;

    UserStatusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static final class Constants {
        /**
         * 禁用
         */
        public static final int DISABLED = 0;
        /**
         * 启用
         */
        public static final int ENABLED = 1;
    }
}
