package com.hixtrip.sample.domain.pay.strategy;

import javax.annotation.PostConstruct;

import com.hixtrip.sample.domain.order.OrderDomainService;
import com.hixtrip.sample.domain.pay.enumType.PayStatus;
import com.hixtrip.sample.domain.pay.model.CommandPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 重复支付策略类
 *
 * @author SepthIroth
 * @date 2024/4/6
 */
@Service
public class DuplicatePayStrategy implements PayStatusStrategy {

    @Autowired
    private OrderDomainService orderDomainService;

    @Override
    public String execution(CommandPay commandPay) {
        return orderDomainService.orderPayFail(commandPay);
    }

    // Spring 启动时候调用此方法
    @Override
    public void afterPropertiesSet() {
        PayStatusStrategyFactory.init(PayStatus.DUPLICATED.getName(), this);
    }
}
