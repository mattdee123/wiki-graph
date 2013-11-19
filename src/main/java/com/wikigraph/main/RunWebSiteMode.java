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
import org.json.JSONException;
import org.json.JSONObject;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.template.freemarker.FreeMarkerRoute;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
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

        Stopwatch stopwatch = new Stopwatch().start();
        Path result = Algos.bidirectionalSearch(start, end);
        stopwatch.stop();
        System.out.println("Found path: " + result + " in " + stopwatch.elapsed(MILLISECONDS) +"ms");

        if (result == null) {
          halt(404, "There is no path between those two articles.");
        }

        JSONObject json = new JSONObject();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> retVal = Maps.newHashMap();
        retVal.put("path", result.toList());
        retVal.put("time", stopwatch.elapsed(MILLISECONDS));
        try {
          return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(retVal);
        } catch (JsonProcessingException e) {
          e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
          halt(500);
        }
        return "{}";
      }
    });
  }
}
