package com.wikigraph.wikidump;

import com.google.common.base.Joiner;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import static com.google.common.base.Charsets.UTF_8;
import static com.wikigraph.wikidump.WikidumpHandler.Page;

/*
 This class writes two files - links.wikigraph and articles.wikigraph and writes pages to each of the files in the
 following formats:

  articles.wikigraph : Contains groups of two lines - the first is the name of the article and the second is either
  "A" or "R", depending on if the page is an article or redirect.
  */
public class ArticleWriter implements PageProcessor {
  private final Writer articleWriter;
  Map<String, Integer> ids = Maps.newHashMapWithExpectedSize(14000000);
  int count = 0;
  Joiner joiner = Joiner.on('|');
  HashFunction sha256 = Hashing.sha256();


  public ArticleWriter(String outDir) {
    File articleFile = new File(outDir, "articles.csv");
    try {
      articleWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(articleFile)));
    } catch (FileNotFoundException e) {
      throw Throwables.propagate(e);
    }
  }

  @Override
  public void processPage(Page currentPage) {
    try {
      String title = TitleFixer.toTitle(currentPage.title);
      long hash = sha256.hashString(title, UTF_8).asLong();
      if (ids.containsKey(title)) {
        System.out.println("Duplicate:" + title);
      } else {
        articleWriter.write(joiner.join(count, hash, title.replace("\\", "\\\\"),
                currentPage.redirect != null ? 1 : 0) + "\n");
        ids.put(title, count);
        count++;
      }
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }

  public Map<String, Integer> getIds() {
    return ids;
  }

  @Override
  public void finish() {
    System.out.printf("Wrote %d articles, saved %d ids%n", count, ids.size());
    try {
      articleWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
