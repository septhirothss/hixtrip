package com.hixtrip.sample.infra.db.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hixtrip.sample.infra.db.dataobject.OrderDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author SepthIroth
 * @date 2024/4/6
 */
@Mapper
public interface OrderMapper extends BaseMapper<OrderDO> {
}
