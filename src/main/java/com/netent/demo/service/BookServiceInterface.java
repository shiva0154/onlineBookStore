package com.netent.demo.service;


import com.netent.demo.Exceptions.CreateBookException;
import com.netent.demo.Exceptions.NoSuchBookFoundException;
import com.netent.demo.Exceptions.UpdateBookException;
import com.netent.demo.model.requestsEntity.BookReq;
import com.netent.demo.model.requestsEntity.SearchReq;
import com.netent.demo.model.responseEntity.BookResponse;
import com.netent.demo.model.responseEntity.SearchResponse;
import java.util.List;


public interface BookServiceInterface {

  BookResponse processBookRequest(BookReq bookReq) throws CreateBookException, UpdateBookException;

  List<SearchResponse> searchBook(SearchReq req) throws NoSuchBookFoundException;

}
