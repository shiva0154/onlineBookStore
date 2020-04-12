package com.netent.demo.model.responseEntity;

import com.netent.demo.model.OrderEntity;
import java.util.Date;
import lombok.Data;

@Data
public class OrderResponse {

  private String order_id;
  private String user_id;
  private String book_id;
  private int quantity;
  private double book_price;
  private double total_cost;
  private String order_time;


  public OrderResponse() {
  }

  public OrderResponse(OrderEntity order)
  {
    this.order_id = order.getOrderId();
    this.user_id = order.getUserId();
    this.book_id = order.getBookId();
    this.quantity = order.getQuantity();
    this.book_price = order.getPrice();
    this.total_cost = order.getTotalCost();
    this.order_time = new Date(Long.parseLong(order.getUpdatedAt().toString())).toString();
  }
}
