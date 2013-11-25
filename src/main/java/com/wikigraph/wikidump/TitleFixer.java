package com.wikigraph.wikidump;

import java.util.HashSet;
import java.util.Set;

/*
Static class with function to fix titles.  Not the best style to have it static, but I see no need to change this now
 */
public class TitleFixer {
  //"ẗßⅰⅱⅲⅳⅴⅵⅶⅷⅸⅹⅺⅻⅼⅽⅾⅿﬁﬂﬀﬃﬄﬅﬆǉǰǌǳⓩⓨⓧⓦⓥⓤⓣⓢⓡⓠⓟⓞⓝⓜⓛⓚⓙⓘⓗⓖⓕⓔⓓⓒⓑⓐǆẖͅ"
  private static final String DONT_CAPITALIZE = "ǋẗßⅰⅱⅲⅳⅴⅵⅶⅷⅸⅹⅺⅻⅼⅽⅾⅿﬁﬂﬀﬃﬄﬅﬆǉǰǌǳⓩⓨⓧⓦⓥⓤⓣⓢⓡⓠⓟⓞⓝⓜⓛⓚⓙⓘⓗⓖⓕⓔⓓⓒⓑⓐǆẖǈͅǲǅ";
  private static TitleFixer singleton;
  private Set<Character> uncapitalizables;

  public static TitleFixer getFixer() {
    if (singleton == null) {
      singleton = new TitleFixer();
    }
    return singleton;
  }

  private TitleFixer() {
    uncapitalizables = new HashSet<>();
    for (char c : DONT_CAPITALIZE.toCharArray()) {
      uncapitalizables.add(c);
    }
  }

  public String toTitle(String line) {
    line = line.trim();
    if (line.length() == 0) {
      return line;
    }
     /*
     These two characters are dumb edge cases where Java's capitalization is actually inconsistent with Python's...
     This is so stupid... are there no standards for capitalizing....
      */
    String title;
    if (!uncapitalizables.contains(line.charAt(0))) {
      title = line.substring(0, 1).toUpperCase() + line.substring(1);
      return title;
    }
    return line;
  }

  public boolean shouldIgnore(String title) {
    return (title.startsWith("File:"));
  }
}
