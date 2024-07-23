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

    private static final class Constants {
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
