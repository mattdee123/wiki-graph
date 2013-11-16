package com.wikigraph.main;

import com.wikigraph.index.export.ArticleSource;
import com.wikigraph.index.export.Indexer;
import com.wikigraph.index.export.LinksSource;

import java.io.File;

import static com.wikigraph.graph.Direction.INCOMING;
import static com.wikigraph.graph.Direction.OUTGOING;

public class IndexMode implements RunMode {
  @Override
  public void run(String[] args) {
    if (args.length != 4) {
      System.out.println("Usage: [outgoing .csv] [incoming .csv] [articles.csv] [outDir]");
      System.exit(1);
    }
    File dir = new File(args[3]);
    File outgoing = new File(args[0]);
    new Indexer(new LinksSource(OUTGOING)).write(outgoing, new File(dir, "outgoing"));
    File incoming = new File(args[1]);
    new Indexer(new LinksSource(INCOMING)).write(incoming, new File(dir, "incoming"));
    File articles = new File(args[2]);
    new Indexer(new ArticleSource()).write(articles, new File(dir, "articles"));
  }
}
