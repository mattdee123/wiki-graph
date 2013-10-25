package com.wikigraph.main;

import com.wikigraph.wikidump.WikiLabels;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class TestMode implements RunMode {
  @Override
  public void run(String[] args) {
    System.out.println("Creating db");
    final GraphDatabaseService graph = new GraphDatabaseFactory().newEmbeddedDatabase(args[0]);
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        graph.shutdown();
      }
    });
    System.out.println("Created, getting nodes");
    test("United States", graph);
    test("Canada", graph);
    test("Singapore American School", graph);
    test("Wikipedia", graph);
    test("University", graph);
    test("Facebook", graph);

  }

  public static void test(String title, GraphDatabaseService graph) {
    System.out.println("Getting for " + title);
    try (Transaction tx = graph.beginTx()) {
      System.out.println("Getting Node...");
      try (ResourceIterator<Node> iterator = graph.findNodesByLabelAndProperty(WikiLabels.NODE, "title",
              title).iterator()) {
        System.out.println("Got Node!");
        Node node = iterator.next();
        System.out.println("Getting Links");
        Iterable<Relationship> links = node.getRelationships(Direction.OUTGOING);
        System.out.println("Got iterable!");
        int d = 0;
        for (Relationship r : links) {
          System.out.print("."); d++;
        }
        System.out.printf("Got %d links!%n", d);
      }
    }
  }
}
