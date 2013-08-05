package com.wikigraph;

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

  private final ConnectionParser connectionParser;

  public WikipediaReader(ConnectionParser connectionParser) {
    this.connectionParser = connectionParser;
  }

  @Override public List<String> connectionsOnArticle(Article article) {
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
    return connectionParser.getConnections(input);
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
