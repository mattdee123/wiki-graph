package com.wikigraph;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ArticleIndex extends Index<String> {

  public ArticleIndex(File dir) {
    super(dir);
  }

  @Override
  protected String getData(RandomAccessFile dataFile, int length) throws IOException {
    char[] chars = new char[length];
    for (int i = 0; i < length; i++) {
      chars[i] = dataFile.readChar();
    }
    return new String(chars);
  }
}
