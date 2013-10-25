package com.wikigraph.wikidump;

import org.neo4j.graphdb.RelationshipType;

public enum WikiRelations implements RelationshipType {
  LINKS_TO,
  REDIRECTS_TO
}
