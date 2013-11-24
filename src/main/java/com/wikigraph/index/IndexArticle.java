package com.wikigraph.index;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wikigraph.graph.Article;

import java.util.ArrayList;
import java.util.List;

public class IndexArticle extends Article {

  private final int id;
  private final LinkIndex outgoingIndex;
  private final LinkIndex incomingIndex;
  private final ArticleIndex articleIndex;
  private final RedirectIndex redirectIndex;
  // Lazily Loaded
  private String title;
  private Boolean isRedirect;

  public IndexArticle(int id, LinkIndex outgoingIndex, LinkIndex incomingIndex, ArticleIndex articleIndex, RedirectIndex redirectIndex) {
    this.id = id;
    this.outgoingIndex = outgoingIndex;
    this.incomingIndex = incomingIndex;
    this.articleIndex = articleIndex;
    this.redirectIndex = redirectIndex;
  }

  /* Only to be used when we know the title in advance */
  public void setTitle(String title) {
    if (this.title == null) {
      this.title = title;
    } else {
      System.err.println("Tried to set title that was already set");
    }

  }

  @Override
  public String getTitle() {
    if (title == null) {
      title = articleIndex.forIndex(id);
    }
    return title;
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  @JsonIgnore
  public boolean isRedirect() {
    if (isRedirect == null) {
      isRedirect = redirectIndex.forIndex(id);
    }
    return isRedirect;
  }

  @Override
  @JsonIgnore
  public List<Article> getIncomingLinks(int limit) {
    return getLinks(limit, incomingIndex);
  }

  @Override
  @JsonIgnore
  public List<Article> getOutgoingLinks(int limit) {
    return getLinks(limit, outgoingIndex);
  }

  @JsonProperty("links")
  private List<Article> getLinks(int limit, LinkIndex index) {
    List<Integer> links = index.forIndex(id);
    int max;
    if (limit < 0) {
      max = links.size();
    } else {
      max = Math.min(limit, links.size());
    }
    List<Article> result = new ArrayList<>(max);
    for (int i = 0; i < max; i++) {
      int newId = links.get(i);
      result.add(new IndexArticle(newId, outgoingIndex, incomingIndex, articleIndex, redirectIndex));
    }
    return result;
  }

  public String toString() {
    ObjectMapper mapper = new ObjectMapper();
    try {
      return mapper.writeValueAsString(this);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return "{}";
    }
  }
}
