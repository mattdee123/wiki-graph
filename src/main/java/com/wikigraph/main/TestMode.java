package com.wikigraph.main;

import com.wikigraph.algorithms.Algos;
import com.wikigraph.graph.Article;
import com.wikigraph.graph.ArticleStore;
import com.wikigraph.index.IndexArticleStore;

import java.io.File;

import static com.wikigraph.algorithms.Algos.Path;


public class TestMode implements RunMode {
  @Override
  public void run(String[] args) {
    ArticleStore articleStore = new IndexArticleStore(new File(args[0]));
    Article a = articleStore.forTitle("United States");
    Article b = articleStore.forTitle("Adolf Hitler");
    Path p = Algos.shortestPath(a,b);
    System.out.println(p);
    System.out.println("Finished!");
    while(p != null) {
      System.out.println(p.end.getTitle());
      p = p.previous;
    }
  }
}
