package com.wikigraph.main;

import com.wikigraph.graph.Article;
import com.wikigraph.graph.ArticleStore;
import com.wikigraph.graph.Articles;
import com.wikigraph.neo4j.Neo4jArticleStore;
import org.json.JSONArray;
import org.neo4j.graphdb.Transaction;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.template.freemarker.FreeMarkerRoute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.externalStaticFileLocation;
import static spark.Spark.get;

public class RunWebSiteMode implements RunMode {
  private static final int MAX_DEPTH = 1;
  private static final int MAX_DEGREE = 11;
  private static final int MAX_ARTICLES = 50;

  @Override
  public void run(String[] args) {
    if (args.length != 1) {
      System.out.println("Requires 1 argument: [storeDir]");
      System.exit(1);
    }
    externalStaticFileLocation("src/main/resources/static");

    final ArticleStore store = Neo4jArticleStore.forDatabaseAt(args[0]);

    get(new FreeMarkerRoute("/") {
      @Override
      public Object handle(Request request, Response response) {
        Map<String, Object> templateValues = new HashMap<String, Object>();
        return modelAndView(templateValues, "index.ftl");
      }
    });

    get(new Route("/page") {
      @Override
      public Object handle(Request request, Response response) {
        String pageName = request.queryParams("page");
        Map<Article, Articles.ArticleInfo> articles;

        try (Transaction tx = store.beginTxn()) {
          Article start = store.forTitle(pageName);
          articles = (start == null) ? null :
                  Articles.articlesToDepth(start, MAX_DEPTH, MAX_DEGREE, MAX_ARTICLES);
          tx.success();
        }
        if (articles == null) {
          halt(404);
          return null;
        }

        List<String> links = new ArrayList<>();
        for (Article a : articles.keySet()) {
          links.add(a.getTitle());
        }
        JSONArray linksJson = new JSONArray(links);
        return linksJson.toString();
      }
    });
  }
}
