package com.wikigraph.main;

import com.google.common.collect.ImmutableMap;
import com.wikigraph.neo4j.Neo4j;
import com.wikigraph.wikidump.WikiLabels;
import com.wikigraph.wikidump.WikiRelations;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class TestMode implements RunMode {
  @Override
  public void run(String[] args) {
    System.out.println("Creating db");
    final GraphDatabaseService graph = Neo4j.getGraph(args[0]);

    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        graph.shutdown();
      }
    });
    System.out.println("Created, getting nodes");
    try {
      test("United States", graph);
      test("Canada", graph);
      test("Singapore American School", graph);
      test("Wikipedia", graph);
      test("University", graph);
      test("Facebook", graph);
    } catch (IOException e) {
      System.out.println(e.getStackTrace());
    }

  }

  public static void test(String title, GraphDatabaseService graph) throws IOException {
    System.out.println("Getting for " + title);
    try (Transaction tx = graph.beginTx()) {
      System.out.println("Getting Node...");
      try (ResourceIterator<Node> iterator = graph.findNodesByLabelAndProperty(WikiLabels.NODE, "title",
              title).iterator()) {
        Node node = iterator.next();
        System.out.println("Got Node!");
        System.out.println("Node Class:" + node.getClass());
        System.out.println("Getting Links");
        Iterable<Relationship> links = node.getRelationships(Direction.OUTGOING, WikiRelations.LINKS_TO);
        int d = 0;
        System.out.println("Getting iterator");
        Iterator<Relationship> it = links.iterator();
        System.out.println("Got iterator");
//        while (it.hasNext()) {
          Relationship r = it.next();
          System.out.println("Got next!");
          System.out.println("Got next:" + r.getEndNode().getProperty("title"));
          System.in.read();
//        }
//        for (Relationship r : links) {
//          System.out.print("."); d++;
//        }
        System.out.printf("Got %d links!%n", d);
      }
    }
  }
}
