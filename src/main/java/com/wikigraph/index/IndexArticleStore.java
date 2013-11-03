package com.wikigraph.index;

import com.wikigraph.graph.Article;
import com.wikigraph.graph.ArticleStore;

import java.io.File;
import java.util.Map;

public class IndexArticleStore implements ArticleStore {


  private final ArticleIndex articleIndex;
  private final LinkIndex outgoingIndex;
  private final LinkIndex incomingIndex;
  private final Map<String, Integer> idMap;

  public IndexArticleStore(File dir) {
    articleIndex = new ArticleIndex(new File(dir, "articles"));
    outgoingIndex = new LinkIndex(new File(dir, "outgoing"));
    incomingIndex = new LinkIndex(new File(dir, "incoming"));
    idMap = articleIndex.getMap();
  }

  @Override
  public Article forTitle(String title) {
    System.out.println("Fetching for:" +title);
    Integer id = idMap.get(title);
    System.out.println("Found: "+id);
    if (id == null) {
      return null;
    } else {
      return new IndexArticle(title, id, outgoingIndex, incomingIndex, articleIndex);
    }
  }
}
