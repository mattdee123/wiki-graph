package com.wikigraph;

import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.io.Files;
import wikidump.ArticleUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class WikidumpArticleReader implements ArticleReader {

  private final File basedir;
  private final Map<String, String> redirectMap;

  public WikidumpArticleReader(File basedir, Map<String, String> redirectMap) {
    this.basedir = basedir;
    this.redirectMap = redirectMap;
  }


  @Override
  public List<String> connectionsOnArticle(Article article) {
    System.out.printf("Fetching connections for %s%n", article.getTitle());
    File articleFile = ArticleUtils.getFileForPage(article.getTitle(), basedir);
    if (!articleFile.exists()) {
      String redirect = redirectMap.get(article.getTitle());
      if (redirect != null) {
        articleFile = ArticleUtils.
      }
      System.err.println("File not found:"+articleFile);
    }
    try {
      String fileText = Files.toString(articleFile, Charsets.UTF_8);
      return Arrays.asList(fileText.split("\n"));
    } catch (IOException e) {
      e.printStackTrace();
      throw Throwables.propagate(e);
    }


  }
}
