package com.wikigraph.neo4j;

import com.wikigraph.graph.Article;
import com.wikigraph.graph.ArticleStore;
import com.wikigraph.wikidump.WikiLabels;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import javax.validation.constraints.NotNull;

public class Neo4jArticleStore implements ArticleStore {

  public static Neo4jArticleStore forDatabaseAt(String storeDir) {
    final GraphDatabaseService graph = Neo4j.getGraph(storeDir);
    return new Neo4jArticleStore(graph);
  }

  private final GraphDatabaseService graph;

  private Neo4jArticleStore(GraphDatabaseService graph) {
    this.graph = graph;
  }

  @Override
  public Article forTitle(@NotNull String title) {
    System.out.println("Finding node for " + title);
    try (ResourceIterator<Node> iterator = graph.findNodesByLabelAndProperty(WikiLabels.NODE, "title",
            title).iterator()) {
      if (!iterator.hasNext()) {
        // This does not exist
        return null;
      }
      Node n = iterator.next();
      if (iterator.hasNext()) {
        System.err.printf("Error: Multiple entries for same title=%s%n", title);
      }
      return Neo4jArticle.fromNode(n, title);
    }
  }


  public Transaction beginTxn() {
    return graph.beginTx();
  }
}
