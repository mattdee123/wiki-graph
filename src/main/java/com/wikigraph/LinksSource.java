package com.wikigraph;

import com.google.common.base.Throwables;
import com.wikigraph.graph.Direction;

import java.io.DataOutputStream;
import java.io.IOException;

import static com.wikigraph.graph.Direction.OUTGOING;


public class LinksSource implements DataSource {

  int indexPos;
  int targetPos;

  public LinksSource(Direction direction) {
    indexPos = (direction == OUTGOING) ? 0 : 1;
    targetPos = (direction == OUTGOING) ? 1 : 0;
  }

  @Override
  public int getIndexAndWriteData(DataOutputStream outputStream, String line) {
    try {
      String[] words = line.split(",");
      for (int i = 1; i < words.length; i++) {
        outputStream.writeInt(Integer.parseInt(words[i]));
      }
      return Integer.parseInt(words[0]);
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }
}
