package com.wikigraph.wikidump;

import org.neo4j.graphdb.Label;

public enum WikiLabels implements Label{

  NODE // Every node should have this label.  It allows us to create an index over all nodes
}
