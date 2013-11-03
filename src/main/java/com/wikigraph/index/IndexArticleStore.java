package com.wikigraph.index;

import com.wikigraph.graph.Article;
import com.wikigraph.graph.ArticleStore;

import java.io.File;

public class IndexArticleStore implements ArticleStore {

  public IndexArticleStore(File dir) {
    File articleIndex = new File(dir, "articles.idx");
    File articleData = new File(dir, "articles.idx");
  }

  @Override
  public Article forTitle(String title) {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }
}
