package com.hixtrip.sample.infra.db.dataobject;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.mapstruct.Mapper;

/**
 * @author SepthIroth
 * @date 2024/4/6
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName(value = "order", autoResultMap = true)
@SuperBuilder(toBuilder = true)
public class OrderDO {

    /**
     * 订单号
     */
    @TableId
    private Long id;


    /**
     * 购买人
     */
    private String userId;


    /**
     * SkuId
     */
    private String skuId;

    /**
     * 购买数量
     */
    private Integer amount;

    /**
     * 购买金额
     */
    private BigDecimal money;

    /**
     * 卖家ID
     */
    private String merchantId;


    /**
     * 购买时间
     */
    private LocalDateTime payTime;

    /**
     * 支付状态
     */
    private String payStatus;


    /**
     * 删除标志（0代表存在 1代表删除）
     */
    @TableLogic
    private Long delFlag;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
