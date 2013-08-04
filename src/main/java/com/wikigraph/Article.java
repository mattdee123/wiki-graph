package com.wikigraph;

import java.util.List;


/** Class which represents a wikipedia article. */
public class Article {

  private final String title;
  private final String type;
  private List<Article> links;

  public Article(String title) {
    this.title = title;
    int colonIndex = title.indexOf(':');
    if (colonIndex != -1) {
      this.type = title.substring(0, colonIndex);
    } else {
      this.type = null;
    }
  }

  public String getTitle() {
    return title;
  }

  public String getType() {
    return type;
  }

  public List<Article> getLinks() {
    return links;
  }

  public void setLinks(List<Article> links) {
    this.links = links;
  }

  public boolean hasSetLinks() {
    return links != null;
  }

  public String toString() {
    return String.format("[Article: title=%s, type=%s]", title, type);
  }
}
