package com.wikigraph.graph;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import static com.google.common.collect.Maps.newHashMap;

/*
Collection of static helper functions regarding the graph
 */
public class Articles {
  /*
  returns : map from (article) -> (depth, children)
   */
  public static Collection<ArticleInfo> articlesToDepth(Article start, int maxDepth,
                                                          int maxDegree, int maxArticles) {
    Map<Article, ArticleInfo> result = newHashMap();
    Queue<NodeDepth> frontier = new LinkedList<>();
    frontier.add(NodeDepth.of(start, 0));
    int articlesLeft = maxArticles - 1; // Includes articles in the frontier
    while (!frontier.isEmpty()) {
      NodeDepth nd = frontier.remove();
      int depth = nd.depth;
      Article article = nd.article;

      Collection<Article> children;
      if (depth >= maxDepth) {
        children = ImmutableList.of(); //We will add no children
      } else {
        children = article.getOutgoingLinks(Math.min(articlesLeft, maxDegree));
        for (Article child : children) {
          if (!result.containsKey(child)) {
            frontier.add(NodeDepth.of(child, depth + 1));
            articlesLeft--;
          }
        }
      }
      result.put(article, ArticleInfo.of(article.getTitle(), article.getId(), children, depth));
    }
    return result.values();
  }

  /*
  Convenience value classes for algorithms.
   */

  /* Class which represents the information necessary to display an article */
  public static class ArticleInfo {
    @JsonProperty("name")
    public String title;

    @JsonProperty("id")
    public String id;

    @JsonIgnore
    public int depth;

    @JsonProperty("children")
    public Collection<Article> links;

    public static ArticleInfo of(String title, int id, Collection<Article> links, int depth) {
      ArticleInfo info = new ArticleInfo();
      info.title = title;
      info.id = id + "";
      info.depth = depth;
      info.links = links;
      return info;
    }
  }

  public static class NodeDepth {
    public int depth;
    public Article article;

    public static NodeDepth of(Article article, int depth) {
      NodeDepth entry = new NodeDepth();
      entry.article = article;
      entry.depth = depth;
      return entry;
    }
  }

}
