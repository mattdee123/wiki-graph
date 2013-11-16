package com.wikigraph.main;

import com.google.common.base.Throwables;
import com.wikigraph.wikidump.LinkParser;
import com.wikigraph.wikidump.*;
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
      System.out.println("Requires 2 arguments: [location of Wikipedia XML] [database dir]");
      System.out.println("Got: " + Arrays.toString(args));
      return;
    }
    try {
      SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
//      PageProcessor writer = new Neo4jPageWriter(args[1], new LinkParser());
      PageProcessor writer = new IntermediateWriter(args[1], new LinkParser());
      parser.parse(new File(args[0]), new WikidumpHandler(writer));

    } catch (ParserConfigurationException | SAXException | IOException e) {
      throw Throwables.propagate(e);
    }
  }
}
