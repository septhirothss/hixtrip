package com.hixtrip.sample.app.convertor;

import com.hixtrip.sample.client.order.dto.CommandPayDTO;
import com.hixtrip.sample.domain.pay.model.CommandPay;
import org.mapstruct.factory.Mappers;

/**
 * @author SepthIroth
 * @date 2024/4/6
 */
public interface CommandPayConverter {

    CommandPayConverter INSTANCE = Mappers.getMapper(CommandPayConverter.class);

    CommandPay CommandPayDTOToCommandPay(CommandPayDTO commandPayDTO);
}
