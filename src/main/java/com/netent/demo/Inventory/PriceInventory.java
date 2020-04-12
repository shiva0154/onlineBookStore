package com.netent.demo.Inventory;

import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Document("price_inventory")
public class PriceInventory {

  @NotNull
  @Indexed(unique = true)
  private String bookId;

  @NotNull
  private Double price;

  public PriceInventory() {
  }


  public PriceInventory(String bookId, double price) {
    this.bookId = bookId;
    this.price = price;
  }
}