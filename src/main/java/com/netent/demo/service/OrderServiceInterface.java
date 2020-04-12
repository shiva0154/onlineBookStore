package com.netent.demo.service;


import com.netent.demo.Exceptions.InSufficientQuantityException;
import com.netent.demo.Exceptions.NoSuchBookFoundException;
import com.netent.demo.Exceptions.UnableToProcessOrderException;
import com.netent.demo.model.requestsEntity.OrderReq;
import com.netent.demo.model.responseEntity.OrderResponse;

public interface OrderServiceInterface {

  OrderResponse orderBook(OrderReq request)
      throws NoSuchBookFoundException, InSufficientQuantityException, UnableToProcessOrderException;
}
