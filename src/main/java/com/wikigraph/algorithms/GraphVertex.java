package com.wikigraph.algorithms;

import java.util.ArrayList;
import java.util.List;

public class GraphVertex {
  public String title;
  public int id;
  public List<GraphVertex> children;

  public GraphVertex(String title, int id) {
    this.title = title;
    this.id = id;
    children = new ArrayList<>();
  }

  public String toString() {
    return "{"+title + "#" +id + "->"+children + "}";
  }
}
