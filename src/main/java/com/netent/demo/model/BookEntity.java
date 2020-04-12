package com.netent.demo.model;

import com.netent.demo.model.requestsEntity.BookReq;
import java.time.Instant;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("Book")
public class BookEntity {


  @NotNull
  @Indexed(name = "book_id", unique = true)
  private String bookId;

  @NotNull
  @Indexed(name = "title_index")
  private String title;

  @NotNull
  @Indexed(name = "isbn_index", unique = true)
  private String isbn;

  @NotNull
  @Indexed(name = "author_index")
  private String author;

  @NotNull
  private Long createdAt;

  @NotNull
  private Long updatedAt;

  @NotNull
  private boolean isActive;

  public BookEntity() {
  }

  public BookEntity(BookReq bookReq) {
    this.author = bookReq.getAuthor();
    this.title = bookReq.getTitle();
    this.isbn = bookReq.getIsbn();
    this.createdAt = Instant.now().getEpochSecond();
    this.updatedAt = this.createdAt;
    this.bookId = UUID.randomUUID().toString();
    this.isActive = true;

  }
}
