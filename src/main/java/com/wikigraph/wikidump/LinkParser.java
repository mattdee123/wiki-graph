package com.wikigraph.wikidump;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;

/**
 * Parses out the links from the markup.  Tries to only get the links in the body of the article.
 * Will stop parsing once it hits references, if the section exists.
 * 
 */
public class LinkParser {
  private static final String LINK_FRONT = "[[";
  private static final String LINK_END = "]]";
  private static final String ARTICLE_END = "==References==";
  //most articles have a reference section signaling the end. If not, tough shit.
  private static final String COMMENT_FRONT = "<!--";
  private static final String COMMENT_END = "-->";

  public List<String> getConnections(String markup) {
    int currentIndex = 0;

    //deal with articles without ref section
    int stopIndex = markup.indexOf(ARTICLE_END);
    if (stopIndex < 0) {
      stopIndex = markup.length();
    }

    HashSet<String> set = new HashSet<String>();
    while (currentIndex < stopIndex) {
      int frontComment = markup.indexOf(COMMENT_FRONT, currentIndex);
      int endComment = markup.indexOf(COMMENT_END, currentIndex);
      int frontIndex = markup.indexOf(LINK_FRONT, currentIndex);

      if (frontIndex > frontComment && 
         (frontIndex < endComment || endComment == -1) && 
          frontComment != -1) {
        if (endComment != -1) {
          currentIndex = endComment;
          continue;
        }
        else break;
      }
      if (frontIndex == -1)
        break;
      int endIndex = markup.indexOf(LINK_END, frontIndex);
      if (endIndex == -1)
        break;
      //Deal with unclosed links
      int nextFront = markup.indexOf(LINK_FRONT, frontIndex + LINK_FRONT.length());
      if (nextFront != -1 && nextFront < endIndex) {
        currentIndex = nextFront;
        continue;
      }
      currentIndex = endIndex + LINK_END.length();
      String inBrackets = markup.substring(frontIndex + LINK_FRONT.length(), endIndex);
      // Both of these are delimiters in the markup which come after the link name
      int pipeIndex = inBrackets.indexOf('|');
      int poundIndex = inBrackets.indexOf('#');

      int linkEnd = inBrackets.length();
      linkEnd = pipeIndex == -1 ? linkEnd : Math.min(linkEnd, pipeIndex);
      linkEnd = poundIndex == -1 ? linkEnd : Math.min(linkEnd, poundIndex);
      if (linkEnd == 0) continue;
      String title = inBrackets.substring(0, linkEnd).trim();
      if (title.length() == 0) continue;
      if (title.charAt(0) == '{') {
//        System.err.printf("Ignoring %s because of invalid beginning%n", title);
        continue;
      }
      if (title.indexOf('\n') == -1) {
        set.add(title);
      }
    }
    List<String> list = new ArrayList<String>(set);
    return list;
  }
}
