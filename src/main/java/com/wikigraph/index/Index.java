package com.wikigraph.index;

import com.google.common.base.Throwables;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public abstract class Index<T> {

  public static final String OUT_DIR = "outgoing";
  public static final String IN_DIR = "incoming";
  public static final String ARTICLE_DIR = "article-id";
  public static final String ARTICLE_HASH_DIR = "article-hash";

  private RandomAccessFile indexFile;
  private RandomAccessFile dataFile;

  public Index(File dir) {
    try {
      this.indexFile = new RandomAccessFile(new File(dir, "index"), "r");
      this.dataFile = new RandomAccessFile(new File(dir, "data"), "r");
    } catch (FileNotFoundException e) {
      throw Throwables.propagate(e);
    }
  }

  public T forIndex(int index) {
    try {
      if ((index + 1) * 4 > indexFile.length()) {
        return null;
      }
      indexFile.seek(index * 4);
      int pos = indexFile.readInt();
      int endPos = indexFile.readInt();
      dataFile.seek(pos);
      return getData(dataFile, endPos - pos);
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }

  public int size() {
    try {
      return ((int) indexFile.length() / 4) - 1;
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }

  protected abstract T getData(RandomAccessFile dataFile, int length) throws IOException;

}
