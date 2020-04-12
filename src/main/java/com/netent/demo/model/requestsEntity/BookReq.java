package com.netent.demo.model.requestsEntity;


import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;


@Data
public class BookReq {

  @NotNull
  private String isbn;

  @NotNull
  private String title;

  @NotNull
  private String author;

  @NotNull
  private double price;

  @Builder.Default
  private int quantity = 1;

  public BookReq(@NotNull String isbn, @NotNull String title,
      @NotNull String author, @NotNull double price, int quantity) {
    this.isbn = isbn;
    this.title = title;
    this.author = author;
    this.price = price;
    this.quantity = quantity;
  }
}
