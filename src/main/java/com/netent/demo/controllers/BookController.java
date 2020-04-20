package com.netent.demo.controllers;


import com.fasterxml.jackson.databind.JsonNode;
import com.netent.demo.Exceptions.InSufficientQuantityException;
import com.netent.demo.Exceptions.NoMediaPostMatchWithIsbnException;
import com.netent.demo.Exceptions.NoSuchBookFoundException;
import com.netent.demo.Exceptions.UnableToProcessOrderException;
import com.netent.demo.Exceptions.UnableToProcessQueryException;
import com.netent.demo.model.requestsEntity.BookReq;
import com.netent.demo.model.requestsEntity.OrderReq;
import com.netent.demo.model.requestsEntity.SearchReq;
import com.netent.demo.model.responseEntity.BookResponse;
import com.netent.demo.model.responseEntity.OrderResponse;
import com.netent.demo.model.responseEntity.SearchResponse;
import com.netent.demo.service.impl.BookService;
import com.netent.demo.service.impl.MediaService;
import com.netent.demo.service.impl.OrderService;
import com.netent.demo.utils.Constants;
import com.netent.demo.utils.ErrorCodes;
import com.netent.demo.utils.ServiceResponse;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.BOOKSTORE)
public class BookController {

  @Autowired
  BookService bookService;
  @Autowired
  OrderService orderService;

  @Autowired
  MediaService mediaService;

  @RequestMapping(value = Constants.SEARCH, method = RequestMethod.GET)
  public ServiceResponse<JsonNode> searchBooks(@RequestBody SearchReq request) {
    try {
      List<SearchResponse> searchResponse = bookService.searchBook(request);
      return ServiceResponse.getSearchResponse(searchResponse);

    } catch (NoSuchBookFoundException e) {
      return ServiceResponse.getFailureResponse(ErrorCodes.ERR_1002);
    }
  }


  @RequestMapping(value = Constants.ADD, method = RequestMethod.PUT)
  public ServiceResponse<JsonNode> addBook(@Valid @RequestBody BookReq req) {
    try {
      BookResponse response = bookService.processBookRequest(req);
      return ServiceResponse.getAddBookResponse(response);
    } catch (DuplicateKeyException e1){
      return ServiceResponse.getFailureResponse(ErrorCodes.ERR_1008);
    }
    catch (Exception e2) {
      return ServiceResponse.getFailureResponse(ErrorCodes.ERR_1001);
    }
  }

  @RequestMapping(value = Constants.BUY, method = RequestMethod.POST)
  public ServiceResponse<JsonNode> orderBook(@Valid @RequestBody OrderReq req) {
    try {
      OrderResponse response = orderService.orderBook(req);
      return ServiceResponse.getOrderResponse(response);
    } catch (NoSuchBookFoundException e) {
      return ServiceResponse.getFailureResponse(ErrorCodes.ERR_1002);
    } catch (InSufficientQuantityException e) {
      return ServiceResponse.getFailureResponse(ErrorCodes.ERR_1003);
    } catch (UnableToProcessOrderException e) {
      return ServiceResponse.getFailureResponse(ErrorCodes.ERR_1004);
    }
  }

  @RequestMapping(value = Constants.MEDIA_POSTS, method = RequestMethod.GET)
  public ServiceResponse<JsonNode> getMediaPosts(@RequestParam("isbn") String isbn) {

    try {
      List<String> response = mediaService.getMediaPostsByIsbn(isbn);
      return ServiceResponse.getMediaResponse(response);
    } catch (NoSuchBookFoundException e) {
      return ServiceResponse.getFailureResponse(ErrorCodes.ERR_1007);
    } catch (NoMediaPostMatchWithIsbnException e) {
      return ServiceResponse.getFailureResponse(ErrorCodes.ERR_1005);
    } catch (UnableToProcessQueryException e) {
      return ServiceResponse.getFailureResponse(ErrorCodes.ERR_1006);
    }
  }


}