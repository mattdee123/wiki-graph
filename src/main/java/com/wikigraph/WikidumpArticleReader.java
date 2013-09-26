package com.wikigraph;

import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.io.Files;
import wikidump.ArticleFileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class WikidumpArticleReader implements ArticleReader {

  private final ArticleFileUtils fileUtils;

  public WikidumpArticleReader(String basedir) {
    this(new ArticleFileUtils(new File(basedir)));
  }

  public WikidumpArticleReader(ArticleFileUtils fileUtils) {
    this.fileUtils = fileUtils;
  }


  @Override
  public List<String> connectionsOnArticle(Article article) {
    System.out.printf("Fetching connections for %s...%n", article.getTitle());
    File articleFile = fileUtils.getFileForPage(article.getTitle());
    if (!articleFile.exists()) {
      System.err.println("File not found:" + articleFile);
    }
    try {
      String fileText = Files.toString(articleFile, Charsets.UTF_8);
      return Arrays.asList(fileText.split("\n"));
    } catch (IOException e) {
      System.out.println("File not found:" + articleFile.getAbsolutePath());
      return null;
    }


  }
}
