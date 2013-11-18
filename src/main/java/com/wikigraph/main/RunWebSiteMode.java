package com.wikigraph.main;

import com.wikigraph.graph.Article;
import com.wikigraph.graph.ArticleStore;
import com.wikigraph.index.IndexArticleStore;
import com.wikigraph.main.RunMode;
import org.json.JSONArray;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.template.freemarker.FreeMarkerRoute;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static spark.Spark.externalStaticFileLocation;
import static spark.Spark.get;

public class RunWebSiteMode implements RunMode {
  private static final int MAX_DEPTH = 5;
  private static final int MAX_DEGREE = 20;
  private static final int MAX_ARTICLES = 1000;
  private static final int MAX_ROWS = 10000;

  @Override
  public void run(String[] args) {
    if (args.length != 1) {
      System.out.println("Requires 1 argument: [index dir]");
      System.exit(1);
    }
    externalStaticFileLocation("src/main/resources/static");

    final ArticleStore store = new IndexArticleStore(new File(args[0]));

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
        Collection<Article> articles;
        Article start = store.forTitle(pageName);
        if (start == null) {
          halt(404);
          return null;
        }
        articles = start.getOutgoingLinks(MAX_ROWS);

        Set<String> links = new HashSet<>();
        for (Article a : articles) {
          links.add(a.getTitle());
        }
        System.out.printf("Returning %d links%n", links.size());
        JSONArray linksJson = new JSONArray(links);
        return linksJson.toString();
      }
    });
  }
}
