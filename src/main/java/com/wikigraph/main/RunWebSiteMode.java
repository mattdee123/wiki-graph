package com.wikigraph.main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Maps;
import com.wikigraph.algorithms.Algos;
import com.wikigraph.algorithms.GraphVertex;
import com.wikigraph.algorithms.Path;
import com.wikigraph.graph.Article;
import com.wikigraph.graph.ArticleStore;
import com.wikigraph.index.IndexArticleStore;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.template.freemarker.FreeMarkerRoute;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static spark.Spark.externalStaticFileLocation;
import static spark.Spark.get;

public class RunWebSiteMode implements RunMode {
  private static final Random random = new Random();

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

    get(new Route("/graph") {
      @Override
      public Object handle(Request request, Response response) {
        String pageName = request.queryParams("page");
        int maxDepth = Integer.parseInt(request.queryParams("maxDepth"));
        int maxDegree = Integer.parseInt(request.queryParams("maxDegree"));
        int maxArticles = Integer.parseInt(request.queryParams("maxArticles"));

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

    get(new Route("/path") {
      @Override
      public Object handle(Request request, Response response) {
        String startTitle = request.queryParams("start");
        String endTitle = request.queryParams("end");

        if (startTitle == null || startTitle.length() == 0) {
          halt(400, "No start title specified.");
        }

        if (endTitle == null || endTitle.length() == 0) {
          halt(400, "No end title specified.");
        }

        Article start = store.forTitle(startTitle);
        Article end = store.forTitle(endTitle);

        if (start == null) {
          halt(404, "Article " + startTitle + " does not exist.");
        }

        if (end == null) {
          halt(404, "Article " + endTitle + " does not exist.");
        }

        System.out.printf("Found %d for %s and %d for %s%n", start.getId(), startTitle, end.getId(), endTitle);

        Stopwatch stopwatch = new Stopwatch().start();
        Path path = Algos.bidirectionalSearch(start, end);
        stopwatch.stop();
        System.out.println("Found path: " + path + " in " + stopwatch.elapsed(MILLISECONDS) + "ms");

        if (path == null) {
          halt(404, "There is no path between those two articles.");
        }

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> result = Maps.newHashMap();
        result.put("path", path.toList());
        result.put("time", stopwatch.elapsed(MILLISECONDS));
        try {
          return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
        } catch (JsonProcessingException e) {
          e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
          halt(500);
        }
        return "{}";
      }
    });

    get(new Route("/links") {
      @Override
      public Object handle(Request request, Response response) {
        String pageTitle = request.queryParams("page");

        if (pageTitle == null || pageTitle.length() == 0) {
          halt(400, "No page specified.");
        }

        Article article = store.forTitle(pageTitle);

        if (article == null) {
          halt(404, "Article " + pageTitle + " does not exist.");
        }

        Map<String, List<Article>> result = Maps.newHashMap();
        result.put("in", article.getIncomingLinks(-1));
        result.put("out", article.getOutgoingLinks(-1));

        ObjectMapper mapper = new ObjectMapper();
        try {
          return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
        } catch (JsonProcessingException e) {
          e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
          halt(500);
        }
        return "{}";
      }
    });

    get(new Route("/api/randomArticle") {
      @Override
      public Object handle(Request request, Response response) {
        int id = random.nextInt(store.numberOfArticles());
        return store.forId(id).getTitle();
      }
    });
  }
}
