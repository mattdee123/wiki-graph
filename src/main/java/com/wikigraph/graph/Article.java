package com.wikigraph.graph;

import com.google.common.base.Objects;

import java.util.Collection;
import java.util.List;

/*
Immutable class which represents a Wikipedia article, and allows graph operations.
 */
public abstract class Article {

  public abstract String getTitle();

  public abstract int getId();

  public abstract boolean isRedirect();

  /* Subzero limit => no limit */
  public abstract List<Article> getIncomingLinks(int limit);

  /* Subzero limit => no limit */
  public abstract List<Article> getOutgoingLinks(int limit);

  public int hashCode() {
    return getId();
  }

  public boolean equals(Object o) {
    if (o instanceof Article) {
      Article other = (Article) o;
      return other.getId() == getId();
    }
    return false;
  }
}
