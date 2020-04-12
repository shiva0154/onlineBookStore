package com.netent.demo.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.netent.demo.model.responseEntity.BookResponse;
import com.netent.demo.model.responseEntity.OrderResponse;
import com.netent.demo.model.responseEntity.SearchResponse;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ServiceResponse<T> extends ResponseEntity<T> {

  static Gson gson = new Gson();
  private static ObjectMapper objectMapper = new ObjectMapper();

  public ServiceResponse(T data) {
    super(data, HttpStatus.OK);
  }

  public ServiceResponse(T data, HttpStatus status) {
    super(data, status);
  }

  public static ServiceResponse<JsonNode> getDefaultServiceOkResponse() {
    ObjectNode objectNode = objectMapper.createObjectNode();
    return new ServiceResponse<>(objectNode, HttpStatus.OK);
  }

  public static ServiceResponse<JsonNode> getAddBookResponse( BookResponse bookResponse)
  {
    ObjectNode objectNode = objectMapper.createObjectNode();
    objectNode.put("status","Success");
    objectNode.put("book_id", bookResponse.getBookId());
    return new ServiceResponse<>(objectNode, HttpStatus.OK);
  }

  public static ServiceResponse<JsonNode> getSearchResponse(List<SearchResponse> searchResponse)
  {
    ObjectNode objectNode = objectMapper.createObjectNode();
    objectNode.put("status","Success");
    objectNode.put("books", gson.toJson(searchResponse));
    return new ServiceResponse<>(objectNode, HttpStatus.OK);
  }

  public static ServiceResponse<JsonNode> getOrderResponse(OrderResponse orderResponse)
  {
    ObjectNode objectNode = objectMapper.createObjectNode();
    objectNode.put("status","Success");
    objectNode.put("order_details", gson.toJson(orderResponse));
    return new ServiceResponse<>(objectNode, HttpStatus.OK);
  }

  public static ServiceResponse<JsonNode> getMediaResponse(List<String> strings)
  {
    ObjectNode objectNode = objectMapper.createObjectNode();
    objectNode.put("status","Success");
    objectNode.put("book_ids", gson.toJson(strings));
    return new ServiceResponse<>(objectNode, HttpStatus.OK);
  }

  public static ServiceResponse<JsonNode> getFailureResponse(ErrorCodes errorCode)
  {
    ObjectNode objectNode = objectMapper.createObjectNode();
    objectNode.put("status","Failure");
    objectNode.put("error_code", errorCode.name());
    objectNode.put("message", errorCode.getText());
    return new ServiceResponse<>(objectNode, HttpStatus.BAD_REQUEST);
  }


}
