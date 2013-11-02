package com.wikigraph.main;

import com.wikigraph.Indexer;
import com.wikigraph.LinksSource;

import java.io.File;

import static com.wikigraph.graph.Direction.INCOMING;
import static com.wikigraph.graph.Direction.OUTGOING;

public class IndexMode implements RunMode {
  @Override
  public void run(String[] args) {
    if (args.length != 3) {
      System.out.println("Usage: [articles.csv] [outgoing .csv] [incoming .csv] [outDir]");
      System.exit(1);
    }
    File outgoing = new File(args[0]);
    File dir = new File(args[2]);
    new Indexer(new LinksSource(OUTGOING)).write(outgoing, dir);
    File incoming = new File(args[1]);
    new Indexer(new LinksSource(INCOMING)).write(incoming, dir);
  }
}
