package com.wikigraph.main;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.wikigraph.algorithms.Algos;
import com.wikigraph.algorithms.GraphVertex;
import com.wikigraph.graph.Article;
import com.wikigraph.graph.ArticleStore;
import com.wikigraph.index.IndexArticleStore;
import com.wikigraph.index.LinkIndex;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class TestMode implements RunMode {
  @Override
  public void run(String[] args) {
    ArticleStore articleStore = new IndexArticleStore(new File(args[0]));
    Article a = articleStore.forTitle("United States");
    Stopwatch stopwatch = new Stopwatch().start();
    GraphVertex v = Algos.getSubGraph(a, 3, 500, 1000);
    stopwatch.stop();
    System.out.println("DONE!  " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + "\n" + v);
  }
}
