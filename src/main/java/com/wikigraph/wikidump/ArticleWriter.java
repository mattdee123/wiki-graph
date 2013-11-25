package com.wikigraph.wikidump;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Throwables;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

  public static final int NUM_BUCKETS = 15000000;
  public static final String ARTICLE_HASH = "article-hash.csv";
  public static final String ARTICLE_ID = "article-id.csv";
  public static final String REDIRECTS = "redirects.csv";

  private final Writer articleIdWriter;
  private final Writer duplicateWriter;
  private final Writer articleHashWriter;
  private final Writer redirectWriter;

  Map<String, Integer> ids = Maps.newHashMapWithExpectedSize(14000000);
  List<List<String>> buckets = Lists.newArrayListWithCapacity(NUM_BUCKETS);
  int count = 0;
  Joiner joiner = Joiner.on('|');
  List<Character> duplicates = new ArrayList<>();
  TitleFixer titleFixer = TitleFixer.getFixer();

  public ArticleWriter(String outDir) {
    while (buckets.size() < NUM_BUCKETS) {
      buckets.add(new ArrayList<String>());
    }
    File articleIdFile = new File(outDir, ARTICLE_ID);
    File articleHashFile = new File(outDir, ARTICLE_HASH);
    File duplicateFile = new File(outDir, "duplicates.csv");
    File redirectFile = new File(outDir, REDIRECTS);
    try {
      articleIdFile.getParentFile().mkdirs();
      articleIdWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(articleIdFile)));
      duplicateWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(duplicateFile)));
      articleHashWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(articleHashFile)));
      redirectWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(redirectFile)));

    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }

  @Override
  public void processPage(Page currentPage) {
    if (!shouldIgnore(currentPage.title)) return;
    try {
      String title = titleFixer.toTitle(currentPage.title);
      if (ids.containsKey(title)) {
        System.out.println("Ignoring Duplicate:" + title + ", Current: " + currentPage.title);
        duplicateWriter.write("|capitalized|" + title + " |lowercaseFirstChar| " +
                currentPage.title.substring(0, 1).toLowerCase() + '\n');
        duplicates.add(currentPage.title.toLowerCase().charAt(0));
      } else {
        if (currentPage.redirect != null) {
          redirectWriter.write(count + "\n");
        }
        articleIdWriter.write(joiner.join(count, title) + "\n");
        int bucket = title.hashCode() % NUM_BUCKETS;
        if (bucket < 0) {
          bucket += NUM_BUCKETS;
        }

        buckets.get(bucket).add(title);
        ids.put(title, count);
        count++;
      }
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }

  private boolean shouldIgnore(String title) {
    return (title.startsWith("File:"));
  }

  public Map<String, Integer> getIds() {
    return ids;
  }

  @Override
  public void finish() {
    System.out.printf("Wrote %d articles, saved %d ids%n", count, ids.size());
    try {
      for (char c : duplicates) {
        duplicateWriter.write(c);
      }
      articleIdWriter.close();
      duplicateWriter.close();
      redirectWriter.close();
      System.out.println("Now Writing hash values");
      for (int i = 0; i < NUM_BUCKETS; i++) {
        if (buckets.get(i).size() > 0) {
          Iterable<String> nameAndIdString = Iterables.transform(buckets.get(i), new Function<String, String>() {
            @Override
            public String apply(String title) {
              return String.format("%s#%d", title, ids.get(title));
            }
          });

          articleHashWriter.write(i + "|" + joiner.join(nameAndIdString) + "\n");
        }
      }
      articleHashWriter.close();
      buckets = null;
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
