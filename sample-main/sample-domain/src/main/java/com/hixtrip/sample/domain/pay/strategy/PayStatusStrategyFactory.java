package com.hixtrip.sample.domain.pay.strategy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

/**
 * 策略工厂类
 *
 * @author SepthIroth
 * @date 2024/4/6
 */
@Component
public class PayStatusStrategyFactory {

    //存放策略类
    private static Map<String, PayStatusStrategy> STRATEGY_MAP = new ConcurrentHashMap<>(255);

    //获取PayStatus对应策略
    public static PayStatusStrategy getStrategy(String payStatus) {
        return STRATEGY_MAP.get(payStatus);
    }

    //工厂初始化方法
    public static void init(String payStatys, PayStatusStrategy strategy) {
        STRATEGY_MAP.put(payStatys, strategy);
    }
}
