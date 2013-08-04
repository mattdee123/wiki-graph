package com.wikigraph;

import org.json.JSONArray;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.template.freemarker.FreeMarkerRoute;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.staticFileLocation;

/**
 * This is the main class.
 */
public class Main {

  public static void main(String[] args) {
    staticFileLocation("static");

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
        String pageName = (String) request.queryParams("page");
        WikipediaReader wikipediaReader = new WikipediaReader();
        List<String> links = wikipediaReader.linksOnArticle(new Article(pageName));

        JSONArray linksJson = new JSONArray(links);

        return linksJson.toString();
      }
    });
  }
}
