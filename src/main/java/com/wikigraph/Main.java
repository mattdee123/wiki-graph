package com.wikigraph;

/** This is the main class. */
public class Main {

  public static void main(String[] args) {
    if (args.length != 1) {
      System.out.println("Specify a single article");
      return;
    }
    Article article = new Article(args[0]);
    WikipediaReader wikipediaReader = new WikipediaReader(new LinkParser());
    for (String link : wikipediaReader.connectionsOnArticle(article)) {
      System.out.println(link);
    }

  }
}
