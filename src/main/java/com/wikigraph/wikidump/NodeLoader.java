package com.wikigraph.wikidump;

import com.google.common.base.Stopwatch;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import org.neo4j.unsafe.batchinsert.BatchInserters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.wikigraph.wikidump.WikiRelations.LINKS_TO;
import static com.wikigraph.wikidump.WikiRelations.REDIRECTS_TO;

public class NodeLoader {

  // TODO: clean up this hacky garbage
  public static void loadNodes(File nodeFile, File linkFile, String dbDir) {
    try {
      Map<String, Long> map = Maps.newHashMapWithExpectedSize(14000000);
      int pages = 0;
      BufferedReader reader = new BufferedReader(new FileReader(nodeFile));
      System.out.println("Creating db...");
      BatchInserter inserter = BatchInserters.inserter(dbDir);
      Stopwatch stopwatch = new Stopwatch();
      stopwatch.start();
      long lastElapsed = 0;
      System.out.println("Writing...");
      while (true) {
        if ((pages & 16383) == 0) {
          long thisElapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS);
          System.out.printf("Page %d started, elapsed time = %ds (+%dms)%n", pages, thisElapsed / 1000, thisElapsed - lastElapsed);
          lastElapsed = thisElapsed;
        }
        String line = reader.readLine();
        pages++;
        if (line == null) break;
        line = line.trim();
        if (line.length() == 0) {
          System.err.println("Length 0 title at page " + pages);
          reader.readLine();
          continue;
        }
        line = toTitle(line);
        String nextLine = reader.readLine().trim();
        Map<String, Object> properties = ImmutableMap.<String, Object>of("title", line, "redirect", nextLine.equals("R"));
        map.put(line, inserter.createNode(properties, WikiLabels.NODE));

      }
      System.out.println("Adding Edges...\n###############");
      pages = 0;
      int links = 0;
      int badLinks = 0;
      reader.close();
      try {
        boolean isRedirect = false;
        Long lastPage = null;
        reader = new BufferedReader(new FileReader(linkFile));
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
          line = line.trim();
          if (line.length() == 0) continue;
          if (line.equals("|A|")) {
            isRedirect = false;
            lastPage = null;
            continue;
          }
          if (line.equals("|R|")) {
            isRedirect = true;
            lastPage = null;
            continue;
          }

          line = toTitle(line);
          Long lineId = map.get(line);
          if (lineId == null) {
            if (lastPage == null) System.out.println("Unrecognized first article!  SHHIIITTT!!!!");
            badLinks++;
//            System.out.println("Line not found! line=" + line);
          } else {
            if (lastPage == null) {
              pages++;
              lastPage = lineId;
              if ((pages & 1023) == 0) {
                long thisElapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS);
                System.out.printf("Page %d started (%d:%d good:bad links), elapsed time = %ds (+%dms)%n", pages,links, badLinks, thisElapsed / 1000, thisElapsed - lastElapsed);
                lastElapsed = thisElapsed;
              }
            } else {
              links++;
              inserter.createRelationship(lastPage, lineId,
                      isRedirect ? REDIRECTS_TO : LINKS_TO, ImmutableMap.<String, Object>of());
            }
          }
        }
      } catch (IOException e) {
        throw Throwables.propagate(e);
      }
      System.out.println("Shutting down...");
      inserter.shutdown();
      System.out.println("elapsed:" + stopwatch.elapsed(TimeUnit.SECONDS) + "s");
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }

  }

  private static String toTitle(String line) {
     /*
     These two characters are dumb edge cases where Java's capitalization is actually inconsistent with Python's...
     This is so stupid... are there no standards for capitalizing....
      */
    if (line.charAt(0) != 'ß' && line.charAt(0) != 'ẗ') {
      line = line.substring(0, 1).toUpperCase() + line.substring(1);
    }
    return line;
  }

}
