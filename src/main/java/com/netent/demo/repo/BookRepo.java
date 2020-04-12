package com.netent.demo.repo;

import com.netent.demo.model.BookEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookRepo extends MongoRepository<BookEntity, String> {

  BookEntity findByBookId(String id);

  BookEntity findByIsbn(String isbn);

  void deleteByBookId(String id);
}
