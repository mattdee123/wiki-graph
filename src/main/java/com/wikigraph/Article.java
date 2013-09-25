package com.wikigraph;

import java.util.List;


/** Class which represents a wikipedia article. */
public class Article {

  private final String title;
  private List<Article> connections;

  public Article(String title) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }

  public List<Article> getConnections() {
    return connections;
  }

  public void setConnections(List<Article> connections) {
    this.connections = connections;
  }

  public String toString() {
    return String.format("[Article: title=%s]", title);
  }
}
