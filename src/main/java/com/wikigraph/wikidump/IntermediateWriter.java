package com.wikigraph.wikidump;

import com.google.common.base.Throwables;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import static com.wikigraph.wikidump.WikidumpHandler.Page;

/*
 This class writes two files - links.wikigraph and articles.wikigraph and writes pages to each of the files in the
 following formats:

  articles.wikigraph : Contains groups of two lines - the first is the name of the article and the second is either
  "A" or "R", depending on if the page is an article or redirect.
  */
public class IntermediateWriter implements PageProcessor {
  private Writer linkWriter;
  private Writer articleWriter;
  private final LinkParser parser;

  public IntermediateWriter(String outDir, LinkParser parser) {
    this.parser = parser;
    File linkFile = new File(outDir, "links.wikigraph");
    File articleFile = new File(outDir, "articles.wikigraph");
    try {
      linkWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(linkFile)));
      articleWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(articleFile)));
    } catch (FileNotFoundException e) {
      throw Throwables.propagate(e);
    }

  }

  @Override
  public void processPage(Page currentPage) {
    try {
      articleWriter.write(currentPage.title + "\n" + (currentPage.redirect == null ? "A" : "R") + "\n");
      linkWriter.write(currentPage.redirect == null ? "|A|\n" : "|R|\n");
      linkWriter.write(currentPage.title + "\n");
      if (currentPage.redirect != null) {
        if (currentPage.redirect.length() == 0) {
          System.err.println("Length 0 redirect: " + currentPage.title);
        } else {
          linkWriter.write(currentPage.redirect + "\n");
        }
      } else {
        List<String> connections = parser.getConnections(currentPage.text);
        for (String link : connections) {
          if (link.indexOf('\n') != -1) {
            System.out.printf("Connection With Newline:  Page=%s, text=%s, connections=%s%n",
                    currentPage.title, currentPage.text, connections.toString());
          }
          linkWriter.write(link + "\n");
        }
      }
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }

  @Override
  public void finish() {
    try {
      linkWriter.close();
      articleWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
