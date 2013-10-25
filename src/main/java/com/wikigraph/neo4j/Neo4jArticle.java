package com.wikigraph.neo4j;

import com.wikigraph.graph.Article;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Neo4jArticle extends Article {

  private final String title;
  private final boolean isRedirect;
  private final Node node;

  // Package-private so that it can only be created with Neo4j code
  static Article fromNode(@NotNull Node node, String title) {
    boolean isRedirect;
    if (!node.hasProperty("redirect")) {
      System.err.printf("Error: No redirect property for article %s%n", title);
      // Default to false so we don't crash
      isRedirect = false;
    } else {
      isRedirect = (boolean) node.getProperty("redirect");
    }
    return new Neo4jArticle(title, isRedirect, node);
  }

  private Neo4jArticle(@NotNull String title, boolean redirect, @NotNull Node node) {
    this.title = title;
    isRedirect = redirect;
    this.node = node;
  }

  @Override
  public String getTitle() {
    return title;
  }

  @Override
  public boolean isRedirect() {
    return isRedirect;
  }

  @Override
  public Collection<Article> getIncomingLinks() {
    List<Article> incoming = new LinkedList<>();
    for (Relationship rel : node.getRelationships(Direction.INCOMING)) {
      Node parent = rel.getStartNode();
      incoming.add(fromNode(parent, (String) parent.getProperty("title")));
    }
    return incoming;

  }

  @Override
  public Collection<Article> getOutgoingLinks() {
    System.out.println("Collecting Outgoing links for " + title);
    List<Article> outgoing = new LinkedList<>();
    for (Relationship rel : node.getRelationships(Direction.OUTGOING)) {
      Node child = rel.getEndNode();
      outgoing.add(fromNode(child, (String) child.getProperty("title")));
    }
    System.out.printf("Got %d links for %s%n", outgoing.size(), title);
    return outgoing;
  }
}
