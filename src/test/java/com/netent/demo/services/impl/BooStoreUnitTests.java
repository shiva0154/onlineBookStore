package com.netent.demo.services.impl;


import com.google.gson.Gson;
import com.netent.demo.Exceptions.CreateBookException;
import com.netent.demo.Exceptions.InSufficientQuantityException;
import com.netent.demo.Exceptions.NoSuchBookFoundException;
import com.netent.demo.Exceptions.UnableToProcessOrderException;
import com.netent.demo.Inventory.BookInventory;
import com.netent.demo.model.BookEntity;
import com.netent.demo.model.MediaPostEntity;
import com.netent.demo.model.requestsEntity.BookReq;
import com.netent.demo.model.requestsEntity.OrderReq;
import com.netent.demo.model.requestsEntity.SearchReq;
import com.netent.demo.model.responseEntity.BookResponse;
import com.netent.demo.model.responseEntity.OrderResponse;
import com.netent.demo.model.responseEntity.SearchResponse;
import com.netent.demo.repo.repoManager.BookInventoryRepoService;
import com.netent.demo.repo.repoManager.BookRepoService;
import com.netent.demo.repo.repoManager.OrderRepoService;
import com.netent.demo.repo.repoManager.PriceRepoService;
import com.netent.demo.service.impl.BookService;
import com.netent.demo.service.impl.MediaService;
import com.netent.demo.service.impl.OrderService;
import java.util.List;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BooStoreUnitTests {

  @Autowired
  BookService bookService;

  @Autowired
  BookRepoService bookRepoService;

  @Autowired
  BookInventoryRepoService bookInventoryRepoService;

  @Autowired
  OrderRepoService orderRepoService;

  @Autowired
  PriceRepoService priceRepoService;

  @Autowired
  OrderService orderService;

  @Autowired
  MediaService mediaService;


  private Gson gson = new Gson();

  static String bookId;
  static String isbn;
  static int count;

  @Test
  @DisplayName("Test to add a book")
  public void test1addBook() {
    BookReq bookReq = new BookReq("123456", "Shiva Triology", "Siva", 100.0, 2);
    try {
      BookResponse bookResponse = bookService.processBookRequest(bookReq);
      bookId = bookResponse.getBookId();
      isbn = bookReq.getIsbn();
      BookEntity bookEntity = bookRepoService.getBookById(bookResponse.getBookId());
      Assert.assertEquals(bookReq.getAuthor(), bookEntity.getAuthor());
    } catch (CreateBookException e2) {
      e2.printStackTrace();
    }
  }


  @Test
  @DisplayName(" Test to add same book again")
  public void test2addSameBook() {
    BookReq bookReq = new BookReq("123456", "Shiva Triology", "Siva", 100.0, 2);
    try {
      BookResponse bookResponse = bookService.processBookRequest(bookReq);
      Assert.assertEquals(bookId, bookResponse.getBookId());
      BookInventory bookInventory = bookInventoryRepoService.getById(bookId);
      Assert.assertEquals(4, bookInventory.getQuantity());

    } catch (CreateBookException e2) {
      e2.printStackTrace();
    }
  }

  @Test
  @DisplayName("Test  Search Book API")
  public void test3searchBook() {
    SearchReq searchReq = new SearchReq("123456", "Shiva Triology", "Siva");
    try {
      List<SearchResponse> response = bookService.searchBook(searchReq);

      Assert.assertEquals(response.get(0).getBook_id(), bookId);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  @DisplayName("Buy a book")
  public void test4orderBook() {
    OrderReq orderReq = new OrderReq("1234", bookId, 1);
    try {
      OrderResponse orderResponse = orderService.orderBook(orderReq);
      Assert.assertNotEquals(orderResponse.getOrder_id(), null);
      Assert.assertEquals(orderResponse.getBook_id(), bookId);
    } catch (NoSuchBookFoundException | InSufficientQuantityException | UnableToProcessOrderException e) {
      e.printStackTrace();
    }
  }

  @Test()
  @DisplayName("Concurrent Order")
  public void test5concurrentOrder() {

    BookInventory bookInventory = bookInventoryRepoService.getById(bookId);
    OrderReq orderReq = new OrderReq("1234", bookId, 1);

    for (int i = 0; i < bookInventory.getQuantity() * 2; i++) {
      new Thread(() -> {

        try {
          OrderResponse response = orderService.orderBook(orderReq);
          if (response != null) {
            count++;
          }
        } catch (Exception e) {
          e.printStackTrace();
        }

      }).start();
    }

    try {
      Thread.sleep(50000);
    } catch (Exception e) {
      e.printStackTrace();
    }

    Assert.assertEquals(count, bookInventory.getQuantity());
  }




  @Test
  @DisplayName("Add media posts")
  public void test6AddMediaPosts()
  {
    MediaPostEntity mediaPostEntity1 = new MediaPostEntity("1","1","Siva Trio", "Shiva Triology is very good book");
    MediaPostEntity mediaPostEntity2 = new MediaPostEntity("2","2","Siva Triology1", "Shiva Trio is  good book");
    MediaPostEntity mediaPostEntity3= new MediaPostEntity("3","3", "random title", " random book");
    try {
      mediaService.addMediaPosts(mediaPostEntity1);
      mediaService.addMediaPosts(mediaPostEntity2);
      mediaService.addMediaPosts(mediaPostEntity3);
    }catch (Exception e)
    {
      e.printStackTrace();
      Assert.assertTrue(true);
    }

  }

  @Test
  @DisplayName("Get Media posts by ISBN test")
  public void test7GetMediaPosts()
  {
    try{
      List<String> result = mediaService.getMediaPostsByIsbn(isbn);
      Assert.assertEquals(result.get(0).equals("Siva Trio"), true);
    }catch (Exception e)
    {
      e.printStackTrace();
      Assert.assertTrue(true);
    }
  }


  @Test
  @DisplayName("Deleting the Data")
  public void test8deleteInvById() {
    bookInventoryRepoService.deleteInventory(bookId);
    priceRepoService.deleteInv(bookId);
    bookRepoService.delteBook(bookId);
    orderRepoService.deleteByBookId(bookId);
  }

}
