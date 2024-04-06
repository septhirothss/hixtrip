package com.hixtrip.sample.domain.inventory.repository;

/**
 *
 */
public interface InventoryRepository {

    Integer getSellableQuantity(String skuId);
    Integer getWithholdingQuantity(String skuId);
    Integer getOccupiedQuantity(String skuId);

    Boolean changeInventory(String skuId, Long sellableQuantity, Long withholdingQuantity, Long occupiedQuantity);
}
