package com.hixtrip.sample.app.service;

import com.hixtrip.sample.app.api.OrderService;
import com.hixtrip.sample.app.convertor.CommandPayConverter;
import com.hixtrip.sample.app.convertor.OrderConvertor;
import com.hixtrip.sample.client.order.dto.CommandOrderCreateDTO;
import com.hixtrip.sample.client.order.dto.CommandPayDTO;
import com.hixtrip.sample.domain.order.OrderDomainService;
import com.hixtrip.sample.domain.order.model.Order;
import com.hixtrip.sample.domain.pay.PayDomainService;
import com.hixtrip.sample.domain.pay.model.CommandPay;
import com.hixtrip.sample.domain.pay.strategy.PayStatusStrategy;
import com.hixtrip.sample.domain.pay.strategy.PayStatusStrategyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * app层负责处理request请求，调用领域服务
 */
@Component
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDomainService orderDomainService;

    @Autowired
    private PayDomainService payDomainService;

    @Override
    public String createOrder(CommandOrderCreateDTO createDTO) {
        Order order = OrderConvertor.INSTANCE.CommandCreateDTOToOrder(createDTO);
        return orderDomainService.createOrder(order);
    }

    @Override
    public String payCallback(CommandPayDTO commandPayDTO) {
        CommandPay commandPay = CommandPayConverter.INSTANCE.CommandPayDTOToCommandPay(commandPayDTO);
        payDomainService.payRecord(commandPay);
        PayStatusStrategy strategy = PayStatusStrategyFactory.getStrategy(commandPay.getPayStatus());
        String executionResult = strategy.execution(commandPay);
        return executionResult;
    }
}
