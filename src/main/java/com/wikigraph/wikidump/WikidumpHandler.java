package com.wikigraph.wikidump;

import com.google.common.base.Stopwatch;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.concurrent.TimeUnit;

public class WikidumpHandler extends DefaultHandler {



  private int pageNum;
  private long lastElapsed;
  private Page currentPage;
  private StringBuilder currentString;
  private final Stopwatch stopwatch = new Stopwatch();
  private final PageProcessor pageProcessor;

  public WikidumpHandler(PageProcessor pageProcessor) {
    this.pageProcessor = pageProcessor;
  }

  @Override
  public void startDocument() {
    stopwatch.start();
    System.out.println("Started!");
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    currentString = new StringBuilder();
    if (qName.equals("page")) {
      currentPage = new Page();
      if ((pageNum & 16383) == 0) {
        long thisElapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS);
        System.out.printf("Page %d started, elapsed time = %ds (+%dms)%n", pageNum, thisElapsed / 1000, thisElapsed - lastElapsed);
        lastElapsed = thisElapsed;
      }
      pageNum++;
    } else if (qName.equals("redirect")) {
      currentPage.redirect = attributes.getValue("title");
    }
  }

  @Override
  public void characters(char[] chars, int start, int length) throws SAXException {
    currentString.append(chars, start, length);
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {
    if (qName.equals("title")) {
      currentPage.title = currentString.toString();
    } else if (qName.equals("text")) {
      currentPage.text = currentString.toString();
    } else if (qName.equals("page")) {
      pageProcessor.processPage(currentPage);
    }
  }

  @Override
  public void endDocument() throws SAXException {
    super.endDocument();
    pageProcessor.finish();
    System.out.println("Finished!");
  }

  @Override
  public void warning(SAXParseException e) throws SAXException {
    super.warning(e);
  }

  @Override
  public void error(SAXParseException e) throws SAXException {
    super.error(e);
  }

  @Override
  public void fatalError(SAXParseException e) throws SAXException {
    super.fatalError(e);
  }

  public static class Page {
    public String title;
    public String redirect;
    public String text;
  }
}


