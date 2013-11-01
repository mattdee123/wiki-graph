package com.wikigraph.wikidump;

import com.google.common.base.Joiner;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import static com.google.common.base.Charsets.UTF_8;
import static com.wikigraph.wikidump.WikidumpHandler.Page;

/*
  Writes articles.csv which is a csv of

  id|hash|title|redirect
  (note that the '|' can not appear in wikipedia titles:
   http://en.wikipedia.org/wiki/Wikipedia:Naming_conventions_(technical_restrictions)#Forbidden_characters )

  sample:

  0|-3415644236936846715|AccessibleComputing|1
  1|151019466254441834|Anarchism|0
  2|-5577640864942179824|AfghanistanHistory|1
  3|-5554129218480647231|AfghanistanGeography|1
  4|2537779304334789134|AfghanistanPeople|1
  5|-221015016094207866|AfghanistanCommunications|1
  6|-7011153448663452709|AfghanistanTransportations|1
  7|7857168278907173820|AfghanistanMilitary|1
  8|8263408217281889416|AfghanistanTransnationalIssues|1
  9|5294478145563524887|AssistiveTechnology|1
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
      articleFile.mkdirs();
      articleFile.createNewFile();
      articleWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(articleFile)));
    } catch (IOException e) {
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
