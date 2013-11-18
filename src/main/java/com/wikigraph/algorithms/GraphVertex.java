package com.wikigraph.algorithms;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class GraphVertex {
  @JsonProperty("name")
  public String title;

  @JsonProperty("id")
  public int id;

  @JsonProperty("children")
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
