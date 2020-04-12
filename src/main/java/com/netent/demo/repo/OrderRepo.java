package com.netent.demo.repo;

import com.netent.demo.model.OrderEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepo extends MongoRepository<OrderEntity, String> {

  OrderEntity findByOrderId(String id);

  void deleteByBookId(String id);
}
