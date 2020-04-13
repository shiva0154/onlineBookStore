package com.netent.demo.repo.repoManager;


import com.netent.demo.Exceptions.NoEntryFoundException;
import com.netent.demo.Exceptions.NoSuchBookFoundException;
import com.netent.demo.model.BookEntity;
import com.netent.demo.model.requestsEntity.BookReq;
import com.netent.demo.model.requestsEntity.SearchReq;
import com.netent.demo.repo.BookRepo;
import com.netent.demo.repo.MongoUtil;
import com.netent.demo.utils.Constants;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class BookRepoService {

  @Autowired
  private BookRepo bookRepo;

  @Autowired
  private MongoUtil mongoUtil;

  public void createBook(BookEntity bookEntity){
      bookRepo.save(bookEntity);
  }

  public BookEntity getBookById(String id) {
    return bookRepo.findByBookId(id);
  }

  public void isBookPresent(String id) throws NoSuchBookFoundException {
    BookEntity bookEntity = getBookById(id);
    if (bookEntity == null) {
      throw new NoSuchBookFoundException();
    }
  }

  public BookEntity getBookByIsbn(String isbn) throws NoSuchBookFoundException {
    BookEntity bookEntity = bookRepo.findByIsbn(isbn);
    if (null == bookEntity) {
      throw new NoSuchBookFoundException();
    }
    return bookEntity;
  }

  public String getBookByParams(BookReq request) throws NoEntryFoundException {
    Query query = new Query();
    query.addCriteria(Criteria.where(Constants.ISBN).is(request.getIsbn()));
    query.addCriteria(Criteria.where(Constants.TITLE).is(request.getTitle()));
    query.addCriteria(Criteria.where(Constants.AUTHOR).is(request.getAuthor()));
    BookEntity book = mongoUtil.findOne(query, BookEntity.class);
    return book.getBookId();
  }

  public List<BookEntity> findBooks(SearchReq req) throws NoSuchBookFoundException {
    Query query = new Query();
    if (req.getIsbn() != null) {
      query.addCriteria(Criteria.where(Constants.ISBN).is(req.getIsbn()));
    }
    if (req.getTitle() != null) {
      query.addCriteria(Criteria.where(Constants.TITLE).regex(req.getTitle()));
    }
    if (req.getAuthor() != null) {
      query.addCriteria(Criteria.where(Constants.AUTHOR).regex(req.getAuthor()));
    }
    try {
      return mongoUtil.find(query, BookEntity.class);
    } catch (Exception e) {
      throw new NoSuchBookFoundException();
    }
  }

  public void delteBook(String bookId) {
    bookRepo.deleteByBookId(bookId);
  }


}
