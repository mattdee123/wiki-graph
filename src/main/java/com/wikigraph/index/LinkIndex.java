package com.wikigraph.index;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class LinkIndex extends DataFileIndex<List<Integer>> {

  public LinkIndex(File dir) {
    super(dir);
  }

  @Override
  protected List<Integer> getData(RandomAccessFile dataFile, int length) throws IOException {
    int numberOfLinks = length / 4;
    List<Integer> list = new ArrayList<>(numberOfLinks);
    for (int i = 0; i < numberOfLinks; i++) {
      list.add(dataFile.readInt());
    }
    return list;
  }
}
