package com.wikigraph.main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wikigraph.algorithms.Algos;
import com.wikigraph.algorithms.GraphVertex;
import com.wikigraph.graph.Article;
import com.wikigraph.graph.ArticleStore;
import com.wikigraph.index.IndexArticleStore;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.template.freemarker.FreeMarkerRoute;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.externalStaticFileLocation;
import static spark.Spark.get;

public class RunWebSiteMode implements RunMode {
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
        int maxDepth = Integer.parseInt(request.queryParams("maxDepth"));
        int maxDegree = Integer.parseInt(request.queryParams("maxDegree"));
        int maxArticles = Integer.parseInt(request.queryParams("maxArticles"));
        Collection<Article> articles;
        Article start = store.forTitle(pageName);
        if (start == null) {
          halt(404);
          return null;
        }

        GraphVertex result = Algos.getSubGraph(start, maxDepth, maxDegree, maxArticles);

        ObjectMapper mapper = new ObjectMapper();

        try {
          return mapper.writer().withDefaultPrettyPrinter().writeValueAsString(result);
        } catch (JsonProcessingException e) {
          e.printStackTrace();
          return "{}";
        }
      }
    });
  }
}
