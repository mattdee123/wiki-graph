package com.wikigraph.main;

import com.wikigraph.wikidump.NodeLoader;

import java.io.File;

public class AddToGraphMode implements RunMode {
  @Override
  public void run(String[] args) {
    if (args.length != 2) {
      System.out.println("Takes two args: [temp dir] [dbDir]");
      System.exit(1);
    }
    NodeLoader.loadNodes(new File(args[0],"articles"), new File(args[0], "links"), args[1]);
  }
}
