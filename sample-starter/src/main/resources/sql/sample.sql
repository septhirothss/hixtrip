#todo 你的建表语句,包含索引
CREATE TABLE d_order(
      ID VARCHAR(32) NOT NULL AUTO_INCREMENT  COMMENT '订单号' PRIMARY KEY,
      USER_ID VARCHAR(255) NOT NULL   COMMENT '用户ID' ,
      MERCHANT_ID VARCHAR(255) NOT NULL   COMMENT '卖家ID' ,
      SKU_ID VARCHAR(255)    COMMENT 'SKU_ID' ,
      AMOUNT INT    COMMENT '购买数量' ,
      MONEY DECIMAL(15,2)    COMMENT '购买金额' ,
      PAY_TIME BIGINT    COMMENT '购买时间' ,
      PAY_STATUS VARCHAR(20)  COMMENT '支付状态 0未支付 1已支付 2支付失败 3重复支付 4取消支付' ,
      DEL_FLAG VARCHAR(255)  COMMENT '删除标志 0未删除 1已删除' ,
      CREATE_BY VARCHAR(255)    COMMENT '创建用户ID' ,
      CREATE_TIME BIGINT    COMMENT '创建时间' ,
      UPDATE_BY VARCHAR(255)    COMMENT '更新用户ID' ,
      UPDATE_TIME BIGINT    COMMENT '更新时间' ,
      PAYMENT_METHOD INT COMMENT '支付方式'
)  COMMENT = '订单表';
-- 根据订单所属用户ID设置索引, 加速买家查询
create index IDX_USER_ID
    on d_order (USER_ID);
-- 根据订单所属商家ID设置索引, 加速卖家查询
create index IDX_MERCHANT_ID
    on d_order (MERCHANT_ID);

-- 水平分库分表策略
-- 对于用户端查询,考虑USER_ID取余
-- 如USER_ID为 10000 % 12 = 4 则查询d_order_4这张表 或查询 USE order_db_4这个数据库
-- 这样可以加速用户端的查询速度,但是对于卖家端查询时效果较差
-- 可以考虑用专门的中间表order_2_merchant来记录一些基本的订单信息,加速卖家端查询