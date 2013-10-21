package com.wikigraph.main;

import com.wikigraph.graph.Article;
import com.wikigraph.graph.ArticleStore;
import com.wikigraph.neo4j.Neo4jArticleStore;
import org.neo4j.graphdb.Transaction;

import java.util.Collection;

public class TestMode implements RunMode {
  @Override
  public void run(String[] args) {
    System.out.println("Creating db");
    ArticleStore store = Neo4jArticleStore.forDatabaseAt(args[0]);
    System.out.println("Created, getting node");
    try (Transaction tx = store.beginTxn()) {
      Article article = store.forTitle("United States");
      System.out.println("Got Node!");
      Collection<Article> links = article.getOutgoingLinks();
      System.out.printf("Got %d links!%n", links.size());
    }

  }
}
