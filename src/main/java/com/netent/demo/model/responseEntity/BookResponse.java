package com.netent.demo.model.responseEntity;

import lombok.Data;

@Data
public class BookResponse {

  private String bookId;

  public BookResponse(String bookId) {
    this.bookId = bookId;
  }
}
