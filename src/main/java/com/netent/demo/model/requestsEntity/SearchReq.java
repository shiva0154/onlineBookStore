package com.netent.demo.model.requestsEntity;

import lombok.Data;

@Data
public class SearchReq {

  private String isbn;
  private String title;
  private String author;

  public SearchReq(String isbn, String title, String author) {
    this.isbn = isbn;
    this.title = title;
    this.author = author;
  }
}
