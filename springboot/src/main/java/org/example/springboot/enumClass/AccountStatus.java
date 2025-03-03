package org.example.springboot.enumClass;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "用户账号状态枚举")
public enum AccountStatus {
    @Schema(description = "禁用")
    DISABLED(0),

    @Schema(description = "启用")
    ENABLED(1);

    private final int value;

    AccountStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static AccountStatus fromValue(int value) {
        for (AccountStatus status : AccountStatus.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的账号状态: " + value);
    }
}