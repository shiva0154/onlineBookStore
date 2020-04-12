package com.netent.demo.model.responseEntity;

import lombok.Data;

@Data
public class BookResponse {

  private String book_id;

  public BookResponse(String bookId) {
    this.book_id = bookId;
  }
}
