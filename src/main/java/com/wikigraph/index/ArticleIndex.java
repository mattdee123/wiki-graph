package com.wikigraph.index;

import com.wikigraph.wikidump.TitleFixer;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ArticleIndex extends DataFileIndex<String> {
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
}
