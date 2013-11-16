package com.wikigraph.index;

import com.wikigraph.graph.Article;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class IndexArticle extends Article {

  private final String title;
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
  public boolean isRedirect() {
    return false;  //TODO : get this working!
  }

  @Override
  public Collection<Article> getIncomingLinks(int limit) {
    return getLinks(limit, incomingIndex);
  }

  @Override
  public Collection<Article> getOutgoingLinks(int limit) {
    return getLinks(limit, outgoingIndex);
  }

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
}
