package com.hixtrip.sample.client.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建订单的请求 入参
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommandOrderCreateDTO {

    /**
     * 商品规格id
     */
    private String skuId;

    /**
     * 购买数量
     */
    private Integer amount;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 支付方式
     * 通常来说比较常见的订单结算处理流程为:
     * 1.根据上述参数进行结算生成订单
     * 2.返回生成的订单详情订单号等,用户在确认订单页面确认
     * 3.用户根据生成的订单详情订单号,选择支付方式生成支付单号
     * 4.用户根据支付单号跳转第三方支付或直接支付
     * 但考虑到此处流程简化,不考虑生成订单详情页面,模拟用户直接选好商品选好支付方式下单,生成订单的同时也生成支付单号的流程,
     * 所以补充支付方式参数
     */
    private Integer paymentMethod;

}
