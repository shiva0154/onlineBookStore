package com.netent.demo.repo.repoManager;


import com.netent.demo.Inventory.PriceInventory;
import com.netent.demo.repo.PriceInventoryRepo;
import com.netent.demo.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class PriceRepoService {

  @Autowired
  PriceInventoryRepo priceInventoryRepo;


  public void createPrice(PriceInventory priceInventory) {
    priceInventoryRepo.save(priceInventory);
  }

  public double getPriceById(String id) {
    return priceInventoryRepo.findByBookId(id).getPrice();
  }

  public void updateInv(String bookId, double price) {
    Query query = new Query();
    query.addCriteria(Criteria.where(Constants.BOOK_ID).is(bookId));
    Update update = new Update();
    update.set(Constants.QUANTITY, price);
  }

  public void deleteInv(String id) {
    priceInventoryRepo.deleteByBookId(id);
  }
}
