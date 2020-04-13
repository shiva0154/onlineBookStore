package com.netent.demo.service.impl;


import com.google.gson.Gson;
import com.netent.demo.Exceptions.CreateBookException;
import com.netent.demo.Exceptions.DbUpdateException;
import com.netent.demo.Exceptions.NoEntryFoundException;
import com.netent.demo.Exceptions.NoSuchBookFoundException;
import com.netent.demo.Inventory.BookInventory;
import com.netent.demo.Inventory.PriceInventory;
import com.netent.demo.model.BookEntity;
import com.netent.demo.model.requestsEntity.BookReq;
import com.netent.demo.model.requestsEntity.SearchReq;
import com.netent.demo.model.responseEntity.BookResponse;
import com.netent.demo.model.responseEntity.SearchResponse;
import com.netent.demo.repo.repoManager.BookInventoryRepoService;
import com.netent.demo.repo.repoManager.BookRepoService;
import com.netent.demo.repo.repoManager.PriceRepoService;
import com.netent.demo.service.BookServiceInterface;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;


@Service
public class BookService implements BookServiceInterface {


  @Autowired
  private BookRepoService bookRepoService;

  @Autowired
  private BookInventoryRepoService bookInventoryRepoService;

  @Autowired
  private PriceRepoService priceRepoService;

  private Gson gson = new Gson();


  @Override
  public BookResponse processBookRequest(BookReq request) throws DuplicateKeyException,CreateBookException {

    try {
      String bookId = bookRepoService.getBookByParams(request);
      updateBookInventory(bookId, request.getQuantity(), false);

      return new BookResponse(bookId);
    } catch (DbUpdateException e2) {
      throw new CreateBookException();
    } catch (NoEntryFoundException e1) {
      try {
        String bookId = addBookToStore(request);
        updateBookInventory(bookId, request.getQuantity(), true);
        updatePriceInventory(bookId, request.getPrice(), true);
        return new BookResponse(bookId);
      }catch (DuplicateKeyException e2)
      {
        throw e2;
      }
      catch (Exception e3) {
        throw new CreateBookException();
      }
    }
  }

  @Override
  public List<SearchResponse> searchBook(SearchReq req) throws NoSuchBookFoundException {
    List<BookEntity> bookEntities = bookRepoService.findBooks(req);
    List<SearchResponse> response = new ArrayList<>();
   for(BookEntity bookEntity: bookEntities){
     response.add(new SearchResponse(bookEntity));
   }
   return response;
  }

  private String addBookToStore(BookReq request)  {
    BookEntity bookEntity = new BookEntity(request);
    bookRepoService.createBook(bookEntity);
    return bookEntity.getBookId();
  }

  private void updateBookInventory(String bookId, int quantity, boolean isNewEntry)
      throws DbUpdateException {
    if (isNewEntry) {
      BookInventory bookInventory = new BookInventory(bookId, quantity);
      bookInventoryRepoService.createInv(bookInventory);
    } else {
      bookInventoryRepoService.updateInv(bookId, quantity);
    }
  }

  private void updatePriceInventory(String bookId, double price, boolean isNewEntry) {
    if (isNewEntry) {
      PriceInventory priceInventory = new PriceInventory(bookId, price);
      priceRepoService.createPrice(priceInventory);
    } else {
      priceRepoService.updateInv(bookId, price);
    }
  }



}
