package com.hixtrip.sample.domain.order.enumType;

/**
 * @author SepthIroth
 * @date 2024/4/6
 */
public enum DelFlag {

    DELETE(0L, "已删除"),
    EXIST(1L, "未删除");

    private Long value;
    private String comment;

    DelFlag(Long value, String comment) {
        this.value = value;
        this.comment = comment;
    }

    public Long getValue() {
        return this.value;
    }

    public String getComment() {
        return this.comment;
    }

    public String getName() {
        return this.name();
    }
}
