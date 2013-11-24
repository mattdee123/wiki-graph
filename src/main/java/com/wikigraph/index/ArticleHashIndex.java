package com.wikigraph.index;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

import static com.wikigraph.wikidump.ArticleWriter.NUM_BUCKETS;

public class ArticleHashIndex extends DataFileIndex<Map<String, Integer>> {

  private char[] buf = new char[512];

  public ArticleHashIndex(File dir) {
    super(dir);
  }

  @Override
  protected Map<String, Integer> getData(RandomAccessFile dataFile, int length) throws IOException {
    int charIndex = 0;
    Map<String, Integer> result = new HashMap<>();
    long end = dataFile.getFilePointer() + length;
    while (dataFile.getFilePointer() < end) {
      buf[charIndex] = dataFile.readChar();
      if (buf[charIndex] == '\0') {
        int index = dataFile.readInt();
        result.put(new String(buf, 0, charIndex), index);
        charIndex = 0;
      } else {
        charIndex++;
      }
    }
    return result;
  }

  public Integer getId(String input) {
    int hash = input.hashCode() % NUM_BUCKETS;
    if (hash < 0) {
      hash += NUM_BUCKETS;
    }
    return forIndex(hash).get(input);
  }

}
