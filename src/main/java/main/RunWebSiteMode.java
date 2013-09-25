package main;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.wikigraph.Article;
import com.wikigraph.ArticleGraph;
import com.wikigraph.LinkParser;
import com.wikigraph.OnlineArticleReader;
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

public class RunWebSiteMode implements RunMode {
  @Override
  public void run(String[] args) {
    final ArticleGraph articleGraph = new ArticleGraph(new OnlineArticleReader(new LinkParser()));

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
        String pageName = request.queryParams("page");
        List<Article> articles = articleGraph.loadArticleFromName(pageName, 1).getConnections();
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
