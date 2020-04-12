package com.netent.demo.model;

import lombok.Data;


@Data
public class MediaPostEntity {

  private String userId;
  private String id;
  private String title;
  private String body;

  public MediaPostEntity(String userId, String id, String title, String body) {
    this.userId = userId;
    this.id = id;
    this.title = title;
    this.body = body;
  }

  @Override
  public String toString() {
    return "MediaPostEntity{" +
        "userId='" + userId + '\'' +
        ", id='" + id + '\'' +
        ", title='" + title + '\'' +
        ", body='" + body + '\'' +
        '}';
  }
}
