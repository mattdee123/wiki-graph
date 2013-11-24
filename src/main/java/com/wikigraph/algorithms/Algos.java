package com.wikigraph.algorithms;

import com.wikigraph.graph.Article;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/*
Collection of static helper functions regarding the graph
 */
public class Algos {
  /*
  returns : map from (article) -> (depth, children)
   */
  public static GraphVertex getSubGraph(Article start, int maxDepth, int maxDegree, int maxArticles) {
    GraphVertex root = new GraphVertex(start.getTitle(), start.getId());
    Queue<NodeDepth> frontier = new ArrayDeque<>();
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

  public static Path shortestPathBfs(Article start, Article end) {
    Queue<Path> frontier = new ArrayDeque<>();
    Set<Article> seen = new HashSet<>();
    frontier.add(Path.of(0, start, null));
    int depth = 0;
//    int searched = 0;
//    Stopwatch s = new Stopwatch().start();
    while (!frontier.isEmpty()) {
      Path path = frontier.remove();

//      searched++;
//      if (searched % 1000 == 0) System.out.printf("\r%d : Searched %,d/%,d in %,dms", depth, searched,
//              seen.size(), s.elapsed(MILLISECONDS));

      if (path.depth != depth) {
        depth = path.depth;
      }
      for (Article child : path.end.getOutgoingLinks(-1)) {
        Path newPath = Path.of(path.depth + 1, child, path);
        if (child.equals(end)) {
          System.out.println();
          return newPath;
        }
        if (!seen.contains(child)) {
          frontier.add(newPath);
          seen.add(child);
        }
      }
    }
    return null;
  }

  public static Path bidirectionalSearch(Article start, Article end) {

    if (start.equals(end)) {
      return Path.of(0, start, null);
    }

    HashMap<Article, Path> fromStart = new HashMap<>();
    HashMap<Article, Path> fromEnd = new HashMap<>();
    Queue<Path> startFrontier = new ArrayDeque<>();
    Queue<Path> endFrontier = new ArrayDeque<>();

    Path startPath = Path.of(0, start, null);
    Path endPath = Path.of(0, end, null);

    fromStart.put(start, startPath);
    fromEnd.put(end, endPath);
    startFrontier.add(startPath);
    endFrontier.add(endPath);
    int expanded = 1;
    while (!(startFrontier.isEmpty() || endFrontier.isEmpty())) {
      // Search Forwards
      if (startFrontier.size() <= endFrontier.size()) {
        expanded += startFrontier.size();
        Queue<Path> nextStartFrontier = new ArrayDeque<>();
        while (!startFrontier.isEmpty()) {
          Path p = startFrontier.remove();
          for (Article child : p.end.getOutgoingLinks(-1)) {
            if (fromStart.containsKey(child)) {
              // We've already seen it from the start - carry on
              continue;
            }
            if (fromEnd.containsKey(child)) {
              // We've seen it from the end - DONE!
              System.out.printf("Expanded %d%n", expanded);
              return p.withEnd(fromEnd.get(child));
            } else {
              Path newPath = Path.of(p.depth + 1, child, p);
              fromStart.put(child, newPath);
              nextStartFrontier.add(newPath);
            }
          }
        }
        startFrontier = nextStartFrontier;
      } else {
        // Search Backwards
        expanded += endFrontier.size();
        Queue<Path> nextEndFrontier = new ArrayDeque<>();
        while (!endFrontier.isEmpty()) {
          Path p = endFrontier.remove();
          for (Article child : p.end.getIncomingLinks(-1)) {
            if (fromEnd.containsKey(child)) {
              // We've already seen it from the start - carry on
              continue;
            }
            if (fromStart.containsKey(child)) {
              // We've seen it from the start - DONE!
              System.out.printf("Expanded %d%n", expanded);
              return fromStart.get(child).withEnd(p);
            } else {
              Path newPath = Path.of(p.depth + 1, child, p);
              fromEnd.put(child, newPath);
              nextEndFrontier.add(newPath);
            }
          }
        }
        endFrontier = nextEndFrontier;
      }
    }
    return null;

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
