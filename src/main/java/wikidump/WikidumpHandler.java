package wikidump;

import com.google.common.base.Joiner;
import com.google.common.base.Stopwatch;
import com.google.common.base.Throwables;
import com.wikigraph.LinkParser;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class WikidumpHandler extends DefaultHandler {

  private static final Joiner joiner = Joiner.on('\n');

  private final File outdir;
  private int pageNum;
  private long lastElapsed;
  private final LinkParser linkParser;
  private Page currentPage;
  private StringBuilder currentString;
  private final BufferedWriter redirectWriter;
  Stopwatch stopwatch = new Stopwatch();

  public WikidumpHandler(File outdir, LinkParser linkParser) {
    this.outdir = outdir;
    this.linkParser = linkParser;
    try {
      File redirectFile = new File(outdir, "redirects");
      redirectFile.getParentFile().mkdirs();
      redirectFile.createNewFile();
      this.redirectWriter = new BufferedWriter(new FileWriter(redirectFile));
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
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
      processPage(currentPage);
    }
  }

  private void processPage(Page currentPage) {
    if (currentPage.redirect == null) {
      writeText(currentPage.title, outdir, joiner.join(linkParser.getConnections(currentPage.text)) + "\n");
    } else {
      try {
        redirectWriter.write(currentPage.title + "|" + currentPage.redirect + "\n");
      } catch (IOException e) {
        throw Throwables.propagate(e);
      }
    }
  }

  private void writeText(String title, File outdir, String output) {
    File file = ArticleHasher.getFileForPage(title, outdir);
    try {
      if (!file.exists()) {
        file.getParentFile().mkdirs();
        file.createNewFile();
      } else {
        System.err.printf("Warning: File %s already exists%n", file.getAbsolutePath());
      }
      FileWriter writer = new FileWriter(file);
      writer.write(output);
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
      System.err.println("file = " + file.getAbsolutePath());
      System.err.println("currentPage.text = " + currentPage.text);
      System.err.println("currentPage.title = " + currentPage.title);
      throw Throwables.propagate(e);
    }
  }



  private int positiveMod(int dividend, int divisor) {
    int result = dividend % divisor;
    if (result < 0) {
      result += divisor;
    }
    return result;
  }

  @Override
  public void endDocument() throws SAXException {
    super.endDocument();
    try {
      redirectWriter.close();
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
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

  private class Page {
    public String title;
    public String redirect;
    public String text;
  }
}


