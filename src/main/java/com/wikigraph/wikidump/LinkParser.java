package com.wikigraph.wikidump;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Parses out the links from the markup.  Tries to only get the links in the body of the article.
 * Will stop parsing once it hits references, if the section exists.
 */
public class LinkParser {
  private static final String LINK_FRONT = "[[";
  private static final String LINK_END = "]]";
  private static final String ARTICLE_END = "==References==";
  //most articles have a reference section signaling the end. If not, tough shit.
  private static final String COMMENT_FRONT = "<!--";
  private static final String COMMENT_END = "-->";

  public List<String> getConnections(String markup) {
    HashSet<String> set = new HashSet<>();

    //deal with articles without ref section
    int stopIndex = indexOfOrEnd(markup, ARTICLE_END, 0);
    int nextCommentStart = indexOfOrEnd(markup, COMMENT_FRONT, 0);

    int nextLink = nextLink(markup, 0, nextCommentStart);

    while (nextLink < stopIndex) {
      int linkEnd = markup.indexOf(LINK_END, nextLink);
      if (linkEnd == -1) {
        // Malformed link!  We can safely break without missing one because there are no more link closers
        break;
      }
      String link = linkFrom(markup.substring(nextLink + LINK_FRONT.length(), linkEnd));
      if (link != null) {
        set.add(link);
      }
      // Get next link start
      nextLink = nextLink(markup, nextLink+1, nextCommentStart);
      if(nextLink > nextCommentStart) {
        nextCommentStart = indexOfOrEnd(markup, COMMENT_FRONT, nextLink);
      }
    }
    return new ArrayList<>(set);
  }

  // currentIndex < nextComment
  private int nextLink(String markup, int currentIndex, int nextComment) {
    int nextLink = markup.indexOf(LINK_FRONT, currentIndex);
    if (nextLink == -1) {
      return markup.length();
    }
    if (nextLink > nextComment) {
      // We might have a comment collision.
      int commentEnd;
      while ((commentEnd = commentEndIfInComment(markup, nextLink)) != -1 ) {
        nextLink = indexOfOrEnd(markup, LINK_FRONT, commentEnd);
        if (nextLink == markup.length()) break;
      }
    }
    return nextLink;
  }

  private String linkFrom(String linkInternals) {
    int pipeLoc = indexOfOrEnd(linkInternals, "|", 0);
    int hashLoc = indexOfOrEnd(linkInternals, "#", 0);
    int end = Math.min(pipeLoc, hashLoc);
    String link = linkInternals.substring(0, end).trim();
    if (link.length() == 0 || link.charAt(0)=='{' || link.contains("\n")) {
      return null;
    }
    return link;
  }

  // Returns the end of the comment if its in the comment, -1 if not in a comment
  private int commentEndIfInComment(String markup, int index) {
    int previousCommentStart = markup.lastIndexOf(COMMENT_FRONT, index);
    if (previousCommentStart == -1) return -1;
    int commentEnd = markup.indexOf(COMMENT_END, previousCommentStart);
    if (commentEnd == -1) {
      // Assume comment stretches forever
      return markup.length();
    }
    if (commentEnd > index) {
      return commentEnd;
    } else {
      return -1;
    }
  }

  // Like String.indexOf, but returns the length of the string if not found.
  private int indexOfOrEnd(String toSearch, String search, int offset) {
    int index = toSearch.indexOf(search, offset);
    return index == -1 ? toSearch.length() : index;
  }

}
