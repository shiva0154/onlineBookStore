package com.netent.demo.repo.repoManager;

import com.netent.demo.Exceptions.DbUpdateException;
import com.netent.demo.Exceptions.InSufficientQuantityException;
import com.netent.demo.Inventory.BookInventory;
import com.netent.demo.repo.BookInventoryRepo;
import com.netent.demo.repo.MongoUtil;
import com.netent.demo.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class BookInventoryRepoService {

  @Autowired
  private BookInventoryRepo repo;

  @Autowired
  private MongoUtil mongoUtil;


  public void createInv(BookInventory bookInventory) {
    repo.save(bookInventory);
  }

  public BookInventory getById(String id) {
    return repo.findByBookId(id);
  }

  public void updateInv(String bookId, int quantity) throws DbUpdateException {
    Query query = new Query();
    query.addCriteria(Criteria.where(Constants.BOOK_ID).is(bookId));
    Update update = new Update();
    update.inc(Constants.QUANTITY, quantity);
    try {
      mongoUtil.findAndUpdate(query, update, BookInventory.class);
    } catch (Exception e) {
      throw new DbUpdateException();
    }
  }

  public void blockInventory(String id, int quantity) throws InSufficientQuantityException {
    Query query = new Query();
    query.addCriteria(Criteria.where(Constants.BOOK_ID).is(id));
    query.addCriteria(Criteria.where(Constants.QUANTITY).gte(quantity));
    Update update = new Update();
    update.inc(Constants.QUANTITY, quantity * (- 1));
    mongoUtil.findAndUpdate(query, update, BookInventory.class);
  }

  public void deleteInventory(String bookId) {
    repo.deleteByBookId(bookId);
  }
}
