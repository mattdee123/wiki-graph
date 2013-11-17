package com.wikigraph.algorithms;


import com.google.common.collect.ImmutableList;
import com.wikigraph.graph.Article;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
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
