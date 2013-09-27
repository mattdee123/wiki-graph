package com.wikigraph.wikidump;

import com.google.common.base.Throwables;
import com.wikigraph.main.RunMode;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class TestParseMode implements RunMode{
  @Override
  public void run(String[] args) {
    if (args.length != 1) {
      System.out.println("Requires 1 argument: [text to parse]");
      System.out.println("Got: " + Arrays.toString(args));
      return;
    }
    try {
      SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
      PageProcessor processor = new PrintingPageProcessor();
      InputStream stream = new ByteArrayInputStream(args[0].getBytes("UTF-8"));
      parser.parse(stream, new WikidumpHandler(processor));

    } catch (ParserConfigurationException e) {
      throw Throwables.propagate(e);
    } catch (SAXException e) {
      throw Throwables.propagate(e);
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }
}
