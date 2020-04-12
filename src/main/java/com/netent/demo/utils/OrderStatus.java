package com.netent.demo.utils;

public enum OrderStatus {

  CREATED("created"),
  PROCESSING("processing"),
  COMPLETED("completed"),
  CANCELLED("cancelled");

  String text;

  OrderStatus(String text) {
    this.text = text;
  }

  public String getText() {
    return this.text;
  }
}
