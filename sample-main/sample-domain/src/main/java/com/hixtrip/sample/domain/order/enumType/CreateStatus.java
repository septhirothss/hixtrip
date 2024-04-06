package com.hixtrip.sample.domain.order.enumType;

/**
 * @author SepthIroth
 * @date 2024/4/6
 */
public enum CreateStatus {

    CREATE_SUCCESS(0, "创建成功"),
    CREATE_FAIL_INVALID_PARAMS(1, "创建失败_参数异常"),
    CREATE_FAIL_LACK_OF_INVENTORY(2, "创建失败_库存不足"),
    CREATE_FAIL_LOCK_FAIL(3, "创建失败_锁异常");

    private int value;
    private String comment;

    CreateStatus(int value, String comment) {
        this.value = value;
        this.comment = comment;
    }

    public int getValue() {
        return this.value;
    }

    public String getComment() {
        return this.comment;
    }

    public String getName() {
        return this.name();
    }
}
