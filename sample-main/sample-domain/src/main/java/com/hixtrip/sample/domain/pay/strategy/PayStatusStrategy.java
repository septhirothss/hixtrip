package com.hixtrip.sample.domain.pay.strategy;

import com.hixtrip.sample.domain.pay.model.CommandPay;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author SepthIroth
 * @date 2024/4/6
 */
public interface PayStatusStrategy extends InitializingBean {

    String execution(CommandPay commandPay);
}
