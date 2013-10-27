package com.wikigraph.wikidump;

/*
Static class with function to fix titles.  Not the best style to have it static, but I see no need to change this now
 */
public class TitleFixer {
  public static String toTitle(String line) {
    line = line.trim();
    if (line.length() == 0) {
      return line;
    }
     /*
     These two characters are dumb edge cases where Java's capitalization is actually inconsistent with Python's...
     This is so stupid... are there no standards for capitalizing....
      */
    String title;
    if (line.charAt(0) != 'ß' && line.charAt(0) != 'ẗ') {
      title = line.substring(0, 1).toUpperCase() + line.substring(1);
      return title;
    }
    return line;
  }
}
