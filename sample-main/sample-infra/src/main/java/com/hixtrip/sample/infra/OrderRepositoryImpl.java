package com.hixtrip.sample.infra;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hixtrip.sample.domain.order.enumType.DelFlag;
import com.hixtrip.sample.domain.order.model.Order;
import com.hixtrip.sample.domain.order.repository.OrderRepository;
import com.hixtrip.sample.infra.db.convertor.OrderDOConvertor;
import com.hixtrip.sample.infra.db.dataobject.OrderDO;
import com.hixtrip.sample.infra.db.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author SepthIroth
 * @date 2024/4/6
 */
@Component
public class OrderRepositoryImpl implements OrderRepository {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public Order createOrder(Order order) {
        OrderDO orderDO = OrderDOConvertor.INSTANCE.domainToDo(order);
        int successCount = orderMapper.insert(orderDO);
        if(successCount == 0){
            return null;
        }
        OrderDO orderResult = orderMapper.selectOne(
                new QueryWrapper<OrderDO>().lambda()
                        .eq(OrderDO::getId,orderDO.getId())
                        .eq(OrderDO::getDelFlag, DelFlag.EXIST.getValue()));
        return OrderDOConvertor.INSTANCE.doToDomain(orderResult);
    }

    @Override
    public Order getOrder(String orderId) {
        OrderDO orderDO = orderMapper.selectById(orderId);
        if(orderDO == null) {
            return null;
        }
        return OrderDOConvertor.INSTANCE.doToDomain(orderDO);
    }

    @Override
    public Order updateOrder(Order order) {
        OrderDO orderDO = OrderDOConvertor.INSTANCE.domainToDo(order);
        int successCount = orderMapper.updateById(orderDO);
        if(successCount == 0){
            return null;
        }
        OrderDO findOrder = orderMapper.selectOne(
                new QueryWrapper<OrderDO>().lambda()
                        .eq(OrderDO::getId,orderDO.getId()));
        return OrderDOConvertor.INSTANCE.doToDomain(findOrder);
    }
}
