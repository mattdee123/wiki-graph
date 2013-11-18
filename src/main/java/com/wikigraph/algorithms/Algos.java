package com.wikigraph.algorithms;


import com.google.common.collect.ImmutableList;
import com.wikigraph.graph.Article;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import static com.google.common.collect.Maps.newHashMap;

/*
Collection of static helper functions regarding the graph
 */
public class Algos {
  /*
  returns : map from (article) -> (depth, children)
   */
  public static GraphVertex getSubGraph(Article start, int maxDepth, int maxDegree, int maxArticles) {
    GraphVertex root = new GraphVertex(start.getTitle(), start.getId());
    Queue<NodeDepth> frontier = new LinkedList<>();
    Map<Article, GraphVertex> seen = new HashMap<>();
    frontier.add(NodeDepth.of(start, 0, root));
    int articlesLeft = maxArticles - 1; // Includes articles in the frontier in amount subtracted

    while (!frontier.isEmpty()) {

      NodeDepth nd = frontier.remove();
      int depth = nd.depth;
      Article article = nd.article;
      GraphVertex vertex = nd.graphVertex;

      Collection<Article> children;
      if (depth < maxDepth) {
        children = article.getOutgoingLinks(Math.min(articlesLeft, maxDegree));
        for (Article child : children) {

          GraphVertex childVertex = new GraphVertex(child.getTitle(), child.getId());
          vertex.children.add(childVertex);

          if (!seen.containsKey(child)) {
            seen.put(child, childVertex);
            frontier.add(NodeDepth.of(child, depth + 1, childVertex));
            articlesLeft--;
          }

        }
      }
    }
    return root;
  }

  public static Path shortestPath(Article start, Article end) {
    Queue<Path> frontier = new LinkedList<>();
    Set<Article> seen = new HashSet<>();
    frontier.add(Path.of(0, start, null));
    while (!frontier.isEmpty()) {
      Path path = frontier.remove();
      System.out.println(path.end.getTitle());

      if(!seen.contains(path.end)) {
        seen.add(path.end);
        for(Article child : path.end.getOutgoingLinks(-1)) {
          Path newPath = Path.of(path.depth+1, child, path);
          if (child.equals(end)) {
            return newPath;
          }
          frontier.add(newPath);
        }
      }
    }
    return null;
  }

  public static class Path {
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
  }

  /*
  Convenience value classes for algorithms.
   */

  public static class NodeDepth {
    public int depth;
    public Article article;
    public GraphVertex graphVertex;

    public static NodeDepth of(Article article, int depth, GraphVertex graphVertex) {
      NodeDepth entry = new NodeDepth();
      entry.article = article;
      entry.depth = depth;
      entry.graphVertex = graphVertex;
      return entry;
    }
  }


}
