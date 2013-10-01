package com.wikigraph.main;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.wikigraph.Article;
import com.wikigraph.ArticleGraph;
import com.wikigraph.WikidumpArticleReader;
import com.wikigraph.wikidump.ArticleNameResolver;
import org.json.JSONArray;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.template.freemarker.FreeMarkerRoute;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.externalStaticFileLocation;
import static spark.Spark.get;

public class RunWebSiteMode implements RunMode {
  @Override
  public void run(String[] args) {
    if (args.length != 1) {
      System.out.println("Requires 1 argument: [baseDir]");
      System.exit(1);
    }
    ArticleNameResolver redirects = new ArticleNameResolver(new File(args[0]));
    final ArticleGraph articleGraph = new ArticleGraph(new WikidumpArticleReader(args[0]), redirects);

    externalStaticFileLocation("src/main/resources/static");

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
        List<Article> articles = articleGraph.loadArticleFromName(pageName, 1).getConnections();
        if (articles == null) {
          halt(404);
        }

        List<String> links = Lists.transform(articles, new Function<Article, String>() {
          @Override
          public String apply(Article article) {
            return article.getTitle();
          }
        });
        JSONArray linksJson = new JSONArray(links);
        return linksJson.toString();
      }
    });
  }
}
