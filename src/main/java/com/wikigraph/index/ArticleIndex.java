package com.wikigraph.index;

import com.wikigraph.wikidump.TitleFixer;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

public class ArticleIndex extends Index<String> {
  private TitleFixer fixer = TitleFixer.getFixer();
  public ArticleIndex(File dir) {
    super(dir);
  }

  @Override
  protected String getData(RandomAccessFile dataFile, int length) throws IOException {
    if (length == 0) return null;
    char[] chars = new char[length];
    // Each character is 2 bytes => we need length/2 characters
    for (int i = 0; i < length / 2; i++) {
      chars[i] = dataFile.readChar();
    }
    return fixer.toTitle(new String(chars));
  }

  public Map<String, Integer> getMap() {
    System.out.println("Getting Reverse Map...");
    Map<String, Integer> map = new HashMap<>(size());
    for (int i = 0; i < size(); i++) {
      if (i % 100000 == 0) {
        System.out.println(i);
      }
      String s = forIndex(i);
      if (s != null) {
        map.put(s, i);
      }
    }
    System.out.println("Got Reverse Map!");
    String s = map.keySet().iterator().next();
    System.out.println(s);

    return map;
  }

}
