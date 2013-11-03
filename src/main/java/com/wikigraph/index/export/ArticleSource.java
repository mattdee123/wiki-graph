package com.wikigraph.index.export;

import com.google.common.base.Throwables;

import java.io.DataOutputStream;
import java.io.IOException;

public class ArticleSource implements DataSource {
  @Override
  public int getIndexAndWriteData(DataOutputStream outputStream, String source) {
    try {

      String[] words = source.split("\\|");
      outputStream.writeChars(words[1]);
      return Integer.parseInt(words[0]);

    } catch (IOException e) {
      throw Throwables.propagate(e);
    } catch (NumberFormatException e) {
//      System.out.printf("Bad String: %s%n", source);
      throw Throwables.propagate(e);
    }
  }
}
