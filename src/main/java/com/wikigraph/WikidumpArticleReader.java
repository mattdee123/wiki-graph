package com.wikigraph;

import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import wikidump.ArticleHasher;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WikidumpArticleReader implements ArticleReader {

  private final File outdir;

  public WikidumpArticleReader(File outdir) {
    this.outdir = outdir;
  }


  @Override
  public List<String> connectionsOnArticle(Article article) {

    File articleFile = ArticleHasher.getFileForPage(article.getTitle(), outdir);
    if (!articleFile.exists()) {
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
