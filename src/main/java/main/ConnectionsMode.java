package main;

import com.wikigraph.Article;
import com.wikigraph.ArticleReader;
import com.wikigraph.WikidumpArticleReader;

import java.io.File;
import java.util.List;

public class ConnectionsMode implements RunMode {
  @Override
  public void run(String[] args) {
    if (args.length != 2) {
      System.out.println("Invalid number of arguments.  Takes 2 args: [base dir] [article title]");
      System.exit(1);
    }
    File baseDir = new File(args[0]);
    ArticleReader articleReader = new WikidumpArticleReader(baseDir);
    List<String> connections = articleReader.connectionsOnArticle(new Article(args[1]));
    for (String s : connections) {
      System.out.println(s);
    }

  }
}
