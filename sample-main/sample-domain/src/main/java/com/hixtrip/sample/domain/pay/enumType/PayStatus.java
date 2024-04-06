package com.hixtrip.sample.domain.pay.enumType;

/**
 * @author SepthIroth
 * @date 2024/4/6
 */
public enum PayStatus {

    UNPAID(0, "未支付"),
    PAID(1, "已支付"),
    FAIL(2, "支付失败"),
    DUPLICATED(3, "重复支付"),
    CANCEL(4, "取消支付");

    private int value;
    private String comment;

    PayStatus(int value, String comment) {
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
