package com.wikigraph;

import com.google.common.collect.ImmutableList;
import wikidump.ArticleNameResolver;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

/**
 * Represents a graph of articles
 */
public class ArticleGraph {

  private final Map<String, Article> articleMap = newHashMap();
  private final ArticleReader articleReader;
  private final ArticleNameResolver nameResolver;

  public ArticleGraph(ArticleReader articleReader, ArticleNameResolver nameResolver) {
    this.articleReader = articleReader;
    this.nameResolver = nameResolver;
  }

  public Article loadArticleFromName(String linkName, int depth) {
    String articleTitle = nameResolver.resolveName(linkName);
    Article article = getArticleOrCreateNew(articleTitle);
    loadConnections(article, depth);
    return article;
  }

  public void loadConnections(Article article, int depth) {
    if (depth == 0) return;

    List<Article> connections = article.getConnections();
    if (connections == null) {
      ImmutableList.Builder<Article> listBuilder = ImmutableList.builder();
      List<String> connectionTitles = articleReader.connectionsOnArticle(article);
      if (connectionTitles == null) {
        System.out.println("Setting connections to null: title = " + article.getTitle());
        article.setConnections(null);
      } else {
        for (String title : connectionTitles) {
          listBuilder.add(getArticleOrCreateNew(title));
        }
        connections = listBuilder.build();
        article.setConnections(connections);
      }
    }
    if (connections != null) {
      for (Article connectedArticle : connections) {
        loadConnections(connectedArticle, depth - 1);
      }
    }
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
