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

  public Article loadArticlesFromName(String articleTitle, int depth) {
    Article article = getArticleOrCreateNew(articleTitle);
    loadChildren(article, depth);
    return article;
  }

  public Article loadChildren(Article article, int depth) {
    if (depth == 0) return article;

    List<Article> links = article.getLinks();
    if (links == null) {
      ImmutableList.Builder<Article> listBuilder = ImmutableList.builder();
      for (String title : articleReader.linksOnArticle(article)) {
        listBuilder.add(getArticleOrCreateNew(title));
      }
      links = listBuilder.build();
      article.setLinks(links);
    }

    for (Article linkedArticle : links) {
      loadChildren(linkedArticle, depth - 1);
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
