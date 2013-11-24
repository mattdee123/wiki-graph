package com.wikigraph.main;


import com.google.common.base.Stopwatch;
import com.wikigraph.algorithms.Algos;
import com.wikigraph.algorithms.Path;
import com.wikigraph.graph.Article;
import com.wikigraph.graph.ArticleStore;
import com.wikigraph.index.IndexArticleStore;

import java.io.File;
import java.util.concurrent.TimeUnit;


public class TestMode implements RunMode {
  @Override
  public void run(String[] args) {
    ArticleStore articleStore = new IndexArticleStore(new File(args[0]));
    Article a = articleStore.forTitle(args[1]);
    Article b = articleStore.forTitle(args[2]);
    Stopwatch s = new Stopwatch().start();
    while (true) {
      Path p = Algos.bidirectionalSearch(a, b);
      s.stop();
      System.out.println("Finished Bidirectional in " + s.elapsed(TimeUnit.MILLISECONDS) + "ms! The path is: " + p);
      s.reset().start();
    }

  }
}
