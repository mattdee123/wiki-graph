package com.wikigraph;

import java.util.List;

/**
 * Reads links from articles
 */
public interface ArticleReader {

  // Returns the list of connections on the article, or null if the article does not exist
  public List<String> connectionsOnArticle(Article article);

}
