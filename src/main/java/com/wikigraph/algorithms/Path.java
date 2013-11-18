package com.wikigraph.algorithms;

import com.google.common.collect.Lists;
import com.wikigraph.graph.Article;

import java.util.List;

public class Path {
  public int depth;
  public Article end;
  public Path previous;

  public static Path of(int depth, Article end, Path previous) {
    Path path = new Path();
    path.depth = depth;
    path.previous = previous;
    path.end = end;
    return path;
  }

  public List<Article> toList() {
    List<Article> result;
    if (previous == null) {
      result = Lists.newArrayList();
    } else {
      result = previous.toList();
    }

    result.add(end);
    return result;
  }

  public String toString() {
    if (previous == null) {
      return end.getTitle() + " (" + depth + ")";
    }
    return previous + " -> " + end.getTitle() + " (" + depth + ")";
  }

  public Path withEnd(Path end) {
    if (end == null) {
      return this;
    }
    Path next = Path.of(depth + 1, end.end, this);
    return next.withEnd(end.previous);
  }
}
