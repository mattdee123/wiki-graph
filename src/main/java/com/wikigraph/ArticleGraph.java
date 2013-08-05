package com.wikigraph;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

/** Represents a graph of articles */
public class ArticleGraph {

  private final Map<String, Article> articleMap = newHashMap();
  private final ArticleReader articleReader;

  public ArticleGraph(ArticleReader articleReader) {
    this.articleReader = articleReader;
  }

  public Article loadArticleFromName(String articleTitle, int depth) {
    Article article = getArticleOrCreateNew(articleTitle);
    loadConnections(article, depth);
    return article;
  }

  public Article loadConnections(Article article, int depth) {
    if (depth == 0) return article;

    List<Article> connections = article.getConnections();
    if (connections == null) {
      ImmutableList.Builder<Article> listBuilder = ImmutableList.builder();
      for (String title : articleReader.connectionsOnArticle(article)) {
        listBuilder.add(getArticleOrCreateNew(title));
      }
      connections = listBuilder.build();
      article.setConnections(connections);
    }

    for (Article connectedArticle : connections) {
      loadConnections(connectedArticle, depth - 1);
    }
    return article;
  }

  private Article getArticleOrCreateNew(String title) {
    Article article = articleMap.get(title);
    if (article == null) {
      article = new Article(title);
      articleMap.put(title, article);
    }
    return article;
  }
}
