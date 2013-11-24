package com.wikigraph.index;

import com.google.common.base.Throwables;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RedirectIndex implements Index<Boolean> {

  public static final String REDIRECT = "redirects";
  private RandomAccessFile file;

  public RedirectIndex(File data) {
    try {
      file = new RandomAccessFile(data, "r");
    } catch (FileNotFoundException e) {
      throw Throwables.propagate(e);
    }
  }

  @Override
  public Boolean forIndex(int index) {
    try {
      file.seek(index);
      return file.readBoolean();
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }

  }

}
