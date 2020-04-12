package com.netent.demo.model.responseEntity;

import com.netent.demo.model.BookEntity;
import java.util.List;
import lombok.Data;

@Data
public class SearchResponse {

  private String book_id;
  private String isbn;
  private String author;
  private String title;

  public SearchResponse(BookEntity bookEntity) {
    this.book_id = bookEntity.getBookId();
    this.isbn = bookEntity.getIsbn();
    this.author = bookEntity.getAuthor();
    this.title = bookEntity.getTitle();
  }

}

