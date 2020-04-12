package com.netent.demo.model;

import com.netent.demo.model.requestsEntity.OrderReq;
import com.netent.demo.utils.OrderStatus;
import java.time.Instant;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("order")
public class OrderEntity {

  @NotNull
  @Indexed(name = "order Id", unique = true)
  private String orderId;

  @NotNull
  private Long createdAt;

  @NotNull
  @Indexed
  private Long updatedAt;

  @NotNull
  private String bookId;

  @NotNull
  private String userId;

  @NotNull
  private double price;

  @NotNull
  private double totalCost;

  @NotNull
  private int quantity;

  @NotNull
  private String status;


  public OrderEntity() {
  }

  ;

  public OrderEntity(OrderReq request, double price) {
    this.orderId = UUID.randomUUID().toString();
    this.createdAt = Instant.now().toEpochMilli();
    this.updatedAt = Instant.now().toEpochMilli();
    this.bookId = request.getBookId();
    this.userId = request.getUserId();
    this.price = price;
    this.quantity = request.getQuantity();
    this.totalCost = price * quantity;
    this.status = OrderStatus.CREATED.getText();
  }

}
