package com.netent.demo.repo.repoManager;

import com.netent.demo.model.OrderEntity;
import com.netent.demo.model.requestsEntity.OrderReq;
import com.netent.demo.repo.MongoUtil;
import com.netent.demo.repo.OrderRepo;
import com.netent.demo.utils.Constants;
import com.netent.demo.utils.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class OrderRepoService {

  @Autowired
  PriceRepoService priceRepoService;

  @Autowired
  OrderRepo orderRepo;

  @Autowired
  MongoUtil mongoUtil;

  public String createOrder(OrderReq request) {
    OrderEntity orderEntity = new OrderEntity(request,
        priceRepoService.getPriceById(request.getBookId()));
    orderRepo.save(orderEntity);
    return orderEntity.getOrderId();
  }

  public void updateStatus(String orderId, OrderStatus status) {
    Query query = new Query();
    query.addCriteria(Criteria.where(Constants.ORDER_ID).is(orderId));
    Update update = new Update();
    update.set(Constants.ORDER_STATUS, status.getText());
    try {
      mongoUtil.findAndUpdate(query, update, OrderEntity.class);
    } catch (Exception e) {
      //ToDO Log: Status update is failing
    }

  }

  public OrderEntity getOrderById(String id) {
    return orderRepo.findByOrderId(id);
  }

  public void deleteByBookId(String bookId) {
    orderRepo.deleteByBookId(bookId);
  }


}
