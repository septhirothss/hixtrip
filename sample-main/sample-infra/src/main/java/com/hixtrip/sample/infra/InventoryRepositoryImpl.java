package com.hixtrip.sample.infra;

import com.hixtrip.sample.domain.inventory.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * infra层是domain定义的接口具体的实现
 */
@Component
public class InventoryRepositoryImpl implements InventoryRepository {

    private static final String INVENTORY_PREFIX = "inventory_";

    private static final String SELLABLE = "sellable_";
    private static final String WITHHOLD = "withhold_";
    private static final String OCCUPIED = "occupied_";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Integer getSellableQuantity(String skuId) {
        String cacheKey = INVENTORY_PREFIX + SELLABLE + skuId;
        return Integer.parseInt(redisTemplate.opsForValue().get(cacheKey).toString());
    }

    @Override
    public Integer getWithholdingQuantity(String skuId) {
        String cacheKey = INVENTORY_PREFIX + WITHHOLD + skuId;
        return Integer.parseInt(redisTemplate.opsForValue().get(cacheKey).toString());

    }

    @Override
    public Integer getOccupiedQuantity(String skuId) {
        String cacheKey = INVENTORY_PREFIX + OCCUPIED + skuId;
        return Integer.parseInt(redisTemplate.opsForValue().get(cacheKey).toString());

    }

    /**
     * 修改库存
     *
     * @param skuId
     * @param sellableQuantity    可售库存
     * @param withholdingQuantity 预占库存
     * @param occupiedQuantity    占用库存
     * @return
     */
    @Override
    public Boolean changeInventory(String skuId, Long sellableQuantity, Long withholdingQuantity, Long occupiedQuantity) {
        try {
            //变更库存
            redisTemplate.opsForValue().set(INVENTORY_PREFIX + SELLABLE + skuId, sellableQuantity + "");
            redisTemplate.opsForValue().set(INVENTORY_PREFIX + WITHHOLD + skuId, withholdingQuantity + "");
            redisTemplate.opsForValue().set(INVENTORY_PREFIX + OCCUPIED + skuId, occupiedQuantity + "");
        } catch (RuntimeException exception) {
            return false;
        }
        return true;
    }
}



//    /**
//     * 修改预占库存
//     *
//     * @param skuId
//     * @param withholdingQuantity 预占库存
//     * @param occupiedQuantity    占用库存
//     * @return
//     */
//    @Override
//    public Boolean changewithholdingQuantity(String skuId, Long withholdingQuantity, Long occupiedQuantity) {
//
//        String lockKey = "lockKey";
//
//        // 获取到redisson锁对象
//        RLock redissonLock = redisson.getLock(lockKey);
//        try {
//            // ========= 添加redisson锁并实现锁续命功能 =============
//            /**
//             *  主要执行一下几个操作
//             *
//             *  1、将localKey设置到Redis服务器上，默认过期时间是30s
//             *  2、每10s触发一次锁续命功能
//             */
//            redissonLock.lock();
//
//            // ======== 扣减库存业务员开始 ============
//
//            //1. 从redis获取可售库存数量
//            int stock = getWithholdingQuantity(skuId);
//            // 如果扣除后库存数量大于0
//            int realStock =stock -occupiedQuantity.intValue();
//
//            if (realStock >= 0) {
//                // 相当于jedis.set(key, value)
//                redisTemplate.opsForValue().set(getWithholdingQuantityKey(skuId), realStock + "");
//                System.out.println("修改成功，剩余预占库存：" + realStock + "");
//                return true;
//            } else { // 如果库存数量小于0
//                System.out.println("修改失败，剩余预占库存不足！");
//                return false;
//            }
//
//            // ======== 扣减库存业务员结束 ============
//        } finally { // 防止异常导致锁无法释放！！！
//            // ============= 释放redisson锁 ==========
//            redissonLock.unlock();
//        }
//    }

