package com.wikigraph.wikidump;

import com.google.common.base.Joiner;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import static com.wikigraph.wikidump.WikidumpHandler.Page;
/*
  Creates links.csv of format

  inID, outID

  and badlinks.csv (just for debugging really), of format

  badlink |in| article

  sample links.csv:

  0,262139
  1,15170
  1,2048507
  1,9006
  1,8979586
  1,34159
  1,2082674
  1,34159
  1,18614
  1,162159

  sample badlinks.csv:

  Wikt:anarchism|in| Anarchism
  Wikt:brigand|in| Anarchism
  A. K. Press|in| Anarchism
  Anarchism_in_Italy|in| Anarchism
  Anarchism_in_France|in| Anarchism
  Simon_Critchley|in| Anarchism
  Anarchist Voices: An Oral History of Anarchism in America|in| Anarchism
 */
public class LinkWriter implements PageProcessor {
  private final Writer linkWriter;
  private final Writer badLinkWriter;
  private final LinkParser parser;
  private int goodCount = 0;
  private int badCount = 0;
  private final Map<String, Integer> idMap;
  private Joiner joiner = Joiner.on(',');

  public LinkWriter(String outDir, LinkParser parser, Map<String, Integer> idMap) {
    this.idMap = idMap;
    File linkFile = new File(outDir, "links.csv");
    File badLinkFile = new File(outDir, "badLinks.csv");
    try {
      linkFile.createNewFile();
      badLinkFile.createNewFile();
      this.linkWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(linkFile)));
      this.badLinkWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(badLinkFile)));
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
    this.parser = parser;
  }

  @Override
  public void processPage(Page currentPage) {
    List<String> links = currentPage.redirect == null ? parser.getConnections(currentPage.text) : ImmutableList.of
            (currentPage.redirect);
    String from = TitleFixer.toTitle(currentPage.title);
    int fromId = idMap.get(from);
    try {
      for (String to : links) {
        to = TitleFixer.toTitle(to);
        Integer toId = idMap.get(to);
        if (toId == null) {
          badLinkWriter.write(to + " |in| " + from + "\n");
          badCount++;
        } else {
          linkWriter.write(joiner.join(fromId, toId) + "\n");
          goodCount++;
        }
      }
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }

  @Override
  public void finish() {
    System.out.printf("Wrote %d good links, ignored %d bad links%n", goodCount, badCount);
    try {
      linkWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
