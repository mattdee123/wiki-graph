package com.wikigraph.main;

import com.wikigraph.index.DataFileIndex;
import com.wikigraph.index.RedirectIndex;
import com.wikigraph.index.export.ArticleHashSource;
import com.wikigraph.index.export.ArticleSource;
import com.wikigraph.index.export.Indexer;
import com.wikigraph.index.export.LinksSource;
import com.wikigraph.index.export.WriteRedirects;
import com.wikigraph.wikidump.ArticleWriter;
import com.wikigraph.wikidump.LinkWriter;

import java.io.File;

import static com.wikigraph.graph.Direction.INCOMING;
import static com.wikigraph.graph.Direction.OUTGOING;
import static com.wikigraph.index.DataFileIndex.ARTICLE_HASH_DIR;
import static com.wikigraph.index.DataFileIndex.IN_DIR;
import static com.wikigraph.index.DataFileIndex.OUT_DIR;

public class IndexMode implements RunMode {
  @Override
  public void run(String[] args) {
    if (args.length != 2) {
      System.out.println("Usage: [dataDir] [indexDir]");
      System.exit(1);
    }
    File dataDir = new File(args[0]);
    File indexDir = new File(args[1]);
    File outgoing = new File(dataDir, LinkWriter.OUT);
    File incoming = new File(dataDir, LinkWriter.IN);
    File articles = new File(dataDir, ArticleWriter.ARTICLE_ID);
    File articlesHash = new File(dataDir, ArticleWriter.ARTICLE_HASH);
    File redirects = new File(dataDir, ArticleWriter.REDIRECTS);

    new Indexer(new ArticleHashSource()).write(articlesHash, new File(indexDir, ARTICLE_HASH_DIR));

    new Indexer(new LinksSource(OUTGOING)).write(outgoing, new File(indexDir, OUT_DIR));

    new Indexer(new LinksSource(INCOMING)).write(incoming, new File(indexDir, IN_DIR));

    new Indexer(new ArticleSource()).write(articles, new File(indexDir, DataFileIndex.ARTICLE_DIR));

    new WriteRedirects().write(redirects, new File(indexDir, RedirectIndex.REDIRECT));


  }
}
