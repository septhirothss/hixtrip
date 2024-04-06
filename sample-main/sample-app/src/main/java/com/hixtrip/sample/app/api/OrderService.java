package com.hixtrip.sample.app.api;

import com.hixtrip.sample.client.order.dto.CommandOrderCreateDTO;
import com.hixtrip.sample.client.order.dto.CommandPayDTO;

/**
 * 订单的service层
 */
public interface OrderService {

    String createOrder(CommandOrderCreateDTO commandOrderCreateDTO);

    String payCallback(CommandPayDTO commandPayDTO);
}
