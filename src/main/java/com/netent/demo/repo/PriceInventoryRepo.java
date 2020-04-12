package com.netent.demo.repo;

import com.netent.demo.Inventory.PriceInventory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PriceInventoryRepo extends MongoRepository<PriceInventory, String> {

  PriceInventory findByBookId(String id);

  void deleteByBookId(String id);
}
