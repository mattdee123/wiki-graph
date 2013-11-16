package com.wikigraph.index;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wikigraph.graph.Article;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class IndexArticle extends Article {
  @JsonProperty("name")
  private final String title;

  @JsonProperty("id")
  private final int id;

  private final LinkIndex outgoingIndex;
  private final LinkIndex incomingIndex;
  private final ArticleIndex articleIndex;

  public IndexArticle(String title, int id, LinkIndex outgoingIndex, LinkIndex incomingIndex, ArticleIndex articleIndex) {
    this.title = title;
    this.id = id;
    this.outgoingIndex = outgoingIndex;
    this.incomingIndex = incomingIndex;
    this.articleIndex = articleIndex;
  }

  @Override
  public String getTitle() {
    return title;
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  @JsonIgnore
  public boolean isRedirect() {
    return false;  //TODO : get this working!
  }

  @Override
  @JsonIgnore
  public Collection<Article> getIncomingLinks(int limit) {
    return getLinks(limit, incomingIndex);
  }

  @Override
  @JsonIgnore
  public Collection<Article> getOutgoingLinks(int limit) {
    return getLinks(limit, outgoingIndex);
  }

  @JsonProperty("links")
  private Collection<Article> getLinks(int limit, LinkIndex index) {
    List<Integer> links = index.forIndex(id);
    int max = Math.min(limit, links.size());
    Collection<Article> result = new ArrayList<>(max);
    for (int i = 0; i < max; i++) {
      int newId = links.get(i);
      String title = articleIndex.forIndex(newId);
      result.add(new IndexArticle(title, newId, outgoingIndex, incomingIndex, articleIndex));
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
