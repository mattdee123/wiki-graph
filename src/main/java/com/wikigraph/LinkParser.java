package com.wikigraph;

import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * Parses out the links from the markup.  Tries to only get the links in the body of the article
 * (ie., ignores
 */
public class LinkParser implements ConnectionParser {
  private static final String LINK_FRONT = "[[";
  private static final String LINK_END = "]]";

  @Override
  public List<String> getConnections(String markup) {
    int currentIndex = 0;
    int length = markup.length();
    ImmutableList.Builder<String> listBuilder = ImmutableList.builder();
    while (currentIndex < length) {
      int frontIndex = markup.indexOf(LINK_FRONT, currentIndex);
      if (frontIndex == -1)
        break;
      int endIndex = markup.indexOf(LINK_END, frontIndex);
      if (endIndex == -1)
        break;
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
      listBuilder.add(title);
    }
    return listBuilder.build();
  }
}
