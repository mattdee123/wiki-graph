package com.wikigraph.main;

import com.google.common.base.Throwables;
import com.wikigraph.wikidump.ArticleWriter;
import com.wikigraph.wikidump.LinkParser;
import com.wikigraph.wikidump.LinkWriter;
import com.wikigraph.wikidump.WikidumpHandler;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class WikiDumpSlicerMode implements RunMode {
  @Override
  public void run(String[] args) {
    if (args.length != 2) {
      System.out.println("Requires 2 arguments: [location of Wikipedia XML] [outDir]");
      System.out.println("Got: " + Arrays.toString(args));
      return;
    }
    try {
      SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
      ArticleWriter articleWriter = new ArticleWriter(args[1]);
      parser.parse(new File(args[0]), new WikidumpHandler(articleWriter));
      System.out.println("DONE WRITING ARTICLES");
      LinkWriter linkWriter = new LinkWriter(args[1], new LinkParser(), articleWriter.getIds());
      parser.parse(new File(args[0]), new WikidumpHandler(linkWriter));
    } catch (ParserConfigurationException | SAXException | IOException e) {
      throw Throwables.propagate(e);
    }
  }
}
