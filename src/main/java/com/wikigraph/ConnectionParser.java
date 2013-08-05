package com.wikigraph;

import java.util.List;

/** Interface for generating "connections" in the graph based on the markup in the article */
public interface ConnectionParser {

  public List<String> getConnections(String markup);
}
