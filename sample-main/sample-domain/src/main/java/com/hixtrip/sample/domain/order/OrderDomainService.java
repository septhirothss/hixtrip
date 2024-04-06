package com.hixtrip.sample.domain.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import com.hixtrip.sample.domain.commodity.CommodityDomainService;
import com.hixtrip.sample.domain.inventory.repository.InventoryRepository;
import com.hixtrip.sample.domain.order.enumType.CreateStatus;
import com.hixtrip.sample.domain.order.enumType.DelFlag;
import com.hixtrip.sample.domain.order.model.Order;
import com.hixtrip.sample.domain.order.repository.OrderRepository;
import com.hixtrip.sample.domain.pay.enumType.PayStatus;
import com.hixtrip.sample.domain.pay.model.CommandPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 订单领域服务
 */
@Component
public class OrderDomainService {


    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private CommodityDomainService commodityDomainService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 创建待付款订单
     * 简化设计, 如果创建成功, 返回支付单号, 如果失败, 返回失败原因
     * 正常情况应该返回一个通用的Result包含错误CODE和异常/正常MSG
     */
    public String createOrder(Order createParam) {
        BigDecimal singlePrice = commodityDomainService.getSkuPrice(createParam.getSkuId());
        createParam.setMoney(singlePrice.multiply(new BigDecimal(createParam.getAmount())));
        createParam.setCreateBy(createParam.getUserId());
        //默认值设置
        createParam.setMerchantId("TEMP_MERCHANT");
        createParam.setPayStatus(PayStatus.UNPAID.getName());
        createParam.setDelFlag(0L);
        createParam.setPayTime(LocalDateTime.now());
        createParam.setCreateTime(LocalDateTime.now());
        //调用创建订单接口
        Order order = orderRepository.createOrder(createParam);
        if (order == null) {
            createParam.setId("FAIL_SIGNAL");
            return CreateStatus.CREATE_FAIL_INVALID_PARAMS.getComment();
        }
        //加锁扣减库存(占用库存+), 这里因为没有使用redisson等客户端, 所以手动进行重试
        Boolean decResult;
        final Integer MAX_RETRIES = 3;
        final Integer RETRY_INTERVAL_MS = 100;
        final String lockKey = "decInventory_" + createParam.getSkuId();
        for (int i = 0; i < MAX_RETRIES; i++) {
            Boolean lockAcquired = redisTemplate.opsForValue().setIfAbsent(lockKey, "1");
            if (Boolean.TRUE.equals(lockAcquired)) {
                try {
                    //扣减库存, 此处选择在生成订单而非支付成功时候进行扣减, 如果支付失败, 后续在订单过期的定时任务和取消订单的接口中进行库存释放
                    decResult = inventoryRepository.changeInventory(order.getSkuId(),
                            (long) (inventoryRepository.getSellableQuantity(order.getSkuId()) - order.getAmount()),
                            (long) (inventoryRepository.getWithholdingQuantity(order.getSkuId()) + order.getAmount()),
                            (long) (inventoryRepository.getOccupiedQuantity(createParam.getSkuId())));
                    if (Boolean.FALSE.equals(decResult)) {
                        //扣减失败 删除失效订单
                        order.setDelFlag(DelFlag.DELETE.getValue());
                        return CreateStatus.CREATE_FAIL_LACK_OF_INVENTORY.getComment();
                    }
                    //扣减成功, 模拟生成的支付单号
                    final String paymentNo = "s00358624143135997952";
                    redisTemplate.opsForValue().set("order:" + order.getId(), paymentNo, 30, TimeUnit.SECONDS);
                    return paymentNo;
                } finally {
                    redisTemplate.delete(lockKey);
                }
            } else {
                try {
                    Thread.sleep(RETRY_INTERVAL_MS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        //锁获取失败
        order.setDelFlag(DelFlag.DELETE.getValue());
        return CreateStatus.CREATE_FAIL_LOCK_FAIL.getComment();
    }

    /**
     * 待付款订单支付成功
     */
    public String orderPaySuccess(CommandPay commandPay) {
        return updateOrderPayStatus(commandPay, PayStatus.PAID);
    }

    /**
     * 待付款订单支付失败
     */
    public String orderPayFail(CommandPay commandPay) {
        return updateOrderPayStatus(commandPay, PayStatus.FAIL);
    }

    private String updateOrderPayStatus(CommandPay commandPay, PayStatus payStatus) {
        Order order = orderRepository.getOrder(commandPay.getOrderId());
        if(order == null) {
            //参数异常, 固定返回失败
            return PayStatus.FAIL.getComment();
        }
        order.setPayStatus(payStatus.getName());
        order.setUpdateTime(LocalDateTime.now());
        orderRepository.updateOrder(order);
        return payStatus.getComment();
    }

}
