package main;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Throwables;
import com.google.common.io.Files;
import com.wikigraph.LinkParser;

import java.io.File;
import java.io.IOException;

public class ParseMode implements RunMode {
  @Override
  public void run(String[] args) {
    if (args.length < 1) {
      System.out.println("No file to parse specified");
    }
    String markup;
    try {
      markup = Files.toString(new File(args[0]), Charsets.UTF_8);
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
    Joiner joiner = Joiner.on('\n');
    System.out.println(joiner.join(new LinkParser().getConnections(markup)));
  }
}
