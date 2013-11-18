package com.wikigraph.index.export;

import com.google.common.base.Throwables;

import java.io.DataOutputStream;
import java.io.IOException;

public class ArticleHashSource implements DataSource{
  @Override
  public int getIndexAndWriteData(DataOutputStream outputStream, String source) {
    String[] words = source.split("\\|");
    int hashIndex = Integer.parseInt(words[0]);
    for (int i = 1; i < words.length; i++) {
      try {
        String[] parts = words[i].split("#");
        if(parts.length != 2) {
          System.out.println("Error String:"+source);
        }
        outputStream.writeChars(parts[0]);
        outputStream.writeChar('\0');
        outputStream.writeInt(Integer.parseInt(parts[1]));
      } catch (IOException e) {
        throw Throwables.propagate(e);
      }
    }
    return hashIndex;
  }
}
