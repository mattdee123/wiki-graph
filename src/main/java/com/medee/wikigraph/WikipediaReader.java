package com.medee.wikigraph;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.io.CharStreams;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

/** Class responsible for reading data from Wikipedia. */
public class WikipediaReader implements ArticleReader {

  private static final String URL_FORMAT =
      "http://en.wikipedia.org/w/index.php?title=%s&action=raw";
  private static final String LINK_FRONT = "[[";
  private static final String LINK_END = "]]";

  @Override public List<String> linksOnArticle(Article article) {
    URL url = urlOf(article);
    System.out.printf("Fetching article %s from %s%n", article.getTitle(), url);
    String input;
    try {
      Readable reader = new InputStreamReader(url.openStream());
      input = CharStreams.toString(reader);
    } catch (IOException e) {
      System.err.printf("Error fetching article from URL=%s%n", url);
      throw Throwables.propagate(e);
    }
    return linksFromMarkup(input);
  }

  private List<String> linksFromMarkup(String markup) {
    int currentIndex = 0;
    int length = markup.length();
    ImmutableList.Builder<String> listBuilder = ImmutableList.builder();
    while (currentIndex < length) {
      int frontIndex = markup.indexOf(LINK_FRONT, currentIndex);
      if (frontIndex == -1) break;
      int endIndex = markup.indexOf(LINK_END, frontIndex);
      String linkWithPossibleName = markup.substring(frontIndex + LINK_FRONT.length(), endIndex);
      int pipeIndex = linkWithPossibleName.indexOf('|');
      if (pipeIndex != -1) {
        listBuilder.add(linkWithPossibleName.substring(0, pipeIndex).trim());
      } else {
        listBuilder.add(linkWithPossibleName.trim());
      }
      currentIndex = endIndex + LINK_END.length();
    }
    return listBuilder.build();
  }

  private URL urlOf(Article article) {
    URL url = null;
    try {
      String encodedTitle = URLEncoder.encode(article.getTitle(), "UTF-8");
      String urlString = String.format(URL_FORMAT, encodedTitle);
      url = new URL(urlString);
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return url;
  }
}
