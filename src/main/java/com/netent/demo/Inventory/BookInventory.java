package com.netent.demo.Inventory;

import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("book_inventory")
public class BookInventory {

  @NotNull
  @Indexed(unique = true)
  private String bookId;

  @NotNull
  private int quantity;


  public BookInventory() {
  }

  public BookInventory(String bookId, int quantity) {
    this.bookId = bookId;
    this.quantity = quantity;
  }
}
