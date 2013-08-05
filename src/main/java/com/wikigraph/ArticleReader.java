package com.wikigraph;

import java.util.List;

/**
 * Reads links from articles
 */
public interface ArticleReader {

  public List<String> connectionsOnArticle(Article article);

}
