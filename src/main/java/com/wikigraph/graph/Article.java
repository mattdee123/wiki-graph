package com.wikigraph.graph;

import com.google.common.base.Objects;

import java.util.Collection;
/*
Immutable class which represents a Wikipedia article, and allows graph operations.
 */
public abstract class Article {

  public abstract String getTitle();

  public abstract int getId();

  public abstract boolean isRedirect();

  public abstract Collection<Article> getIncomingLinks(int limit);

  public abstract Collection<Article> getOutgoingLinks(int limit);

  //Title and Redirect should uniquely identify an Article
  public int hashCode() {
    return Objects.hashCode(getTitle(), isRedirect());
  }

  public boolean equals(Object o) {
    if (o instanceof Article) {
      Article other = (Article) o;
      return Objects.equal(other.getTitle(), getTitle()) && other.isRedirect() == isRedirect();
    }
    return false;
  }
}
