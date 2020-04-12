package com.netent.demo.utils;

public enum ErrorCodes {

  ERR_1001("Unable to add Book"),
  ERR_1002("No Book found with the given queries"),
  ERR_1003("Available Books are less than Requested Quantity"),
  ERR_1004("Unable to process the Order. Please try again"),
  ERR_1005("No Posts present with this ISBN"),
  ERR_1006("Unable to process the query");

  String text;

  ErrorCodes(String text) {
    this.text = text;
  }

  public String getText() {
    return this.text;
  }
}
