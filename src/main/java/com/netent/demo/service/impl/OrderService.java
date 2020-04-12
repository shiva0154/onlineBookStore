package com.netent.demo.service.impl;

import com.netent.demo.Exceptions.InSufficientQuantityException;
import com.netent.demo.Exceptions.NoSuchBookFoundException;
import com.netent.demo.Exceptions.UnableToProcessOrderException;
import com.netent.demo.Inventory.BookInventory;
import com.netent.demo.model.requestsEntity.OrderReq;
import com.netent.demo.model.responseEntity.OrderResponse;
import com.netent.demo.repo.repoManager.BookInventoryRepoService;
import com.netent.demo.repo.repoManager.BookRepoService;
import com.netent.demo.repo.repoManager.OrderRepoService;
import com.netent.demo.service.OrderServiceInterface;
import com.netent.demo.utils.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService implements OrderServiceInterface {

  @Autowired
  private OrderRepoService orderRepoService;

  @Autowired
  private BookInventoryRepoService bookInventoryRepoService;

  @Autowired
  private BookRepoService bookRepoService;


  @Override
  public OrderResponse orderBook(OrderReq request)
      throws NoSuchBookFoundException, UnableToProcessOrderException, InSufficientQuantityException {
    validateInventory(request);
    String orderId = orderRepoService.createOrder(request);
    try {
      processOrder(request);
      orderRepoService.updateStatus(orderId, OrderStatus.PROCESSING);
      completeOrder(orderId);
     return new OrderResponse(orderRepoService.getOrderById(orderId));

    } catch (Exception e) {
      orderRepoService.updateStatus(orderId, OrderStatus.CANCELLED);
      throw new UnableToProcessOrderException();
    }
  }

  private void validateInventory(OrderReq req)
      throws InSufficientQuantityException, NoSuchBookFoundException {
    bookRepoService.isBookPresent(req.getBookId());
    BookInventory inventory = bookInventoryRepoService.getById(req.getBookId());
    if (null == inventory) {
      throw new NoSuchBookFoundException();
    }
    if (inventory.getQuantity() < req.getQuantity()) {
      throw new InSufficientQuantityException();
    }
  }

  private void processOrder(OrderReq req) throws InSufficientQuantityException {
    orderRepoService.updateStatus(req.getBookId(), OrderStatus.PROCESSING);
    bookInventoryRepoService.blockInventory(req.getBookId(), req.getQuantity());
  }

  private void completeOrder(String orderId) {
    orderRepoService.updateStatus(orderId, OrderStatus.COMPLETED);
  }

}
