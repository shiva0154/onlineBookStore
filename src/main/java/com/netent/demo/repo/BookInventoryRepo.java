package com.netent.demo.repo;

import com.netent.demo.Inventory.BookInventory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookInventoryRepo extends MongoRepository<BookInventory, String> {

  BookInventory findByBookId(String id);

  void deleteByBookId(String id);

}
