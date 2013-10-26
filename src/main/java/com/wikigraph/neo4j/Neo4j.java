package com.wikigraph.neo4j;

import com.google.common.collect.ImmutableMap;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class Neo4j {

  public static GraphDatabaseService getGraph(String storeDir) {

    /*

    Tuned using:
    [06:10:19] mattdee: ~/Development/db du -h neostore.nodestore.db neostore.propertystore.db neostore.relationshipstore.db neostore.propertystore.db.strings
    184M	neostore.nodestore.db
    638M	neostore.propertystore.db
    6.0G	neostore.relationshipstore.db
    376M	neostore.propertystore.db.strings
     */
    final GraphDatabaseService graph = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(storeDir).setConfig
            (ImmutableMap.of(
                    "neostore.nodestore.db.mapped_memory", "185M",
                    "neostore.neostore.propertystore.db.mapped_memory", "300M",
                    "neostore.neostore.relationshipstore.db.mapped_memory", "1G",
                    "neostore.neostore.propertystore.db.strings.mapped_memory", "300M"
            )).newGraphDatabase();

    // Add shutdown hook
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        graph.shutdown();
      }
    });

    return graph;
  }
}
