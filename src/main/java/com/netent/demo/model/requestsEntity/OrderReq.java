package com.netent.demo.model.requestsEntity;

import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
public class OrderReq {

  @NotNull
  private String userId;

  @NotNull
  private String bookId;

  @Builder.Default
  private int quantity = 1;


  public OrderReq(@NotNull String userId,
      @NotNull String bookId, int quantity) {
    this.userId = userId;
    this.bookId = bookId;
    this.quantity = quantity;
  }
}
