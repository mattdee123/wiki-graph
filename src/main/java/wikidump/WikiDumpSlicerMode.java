package wikidump;

import com.google.common.base.Throwables;
import com.wikigraph.LinkParser;
import main.RunMode;
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
      System.out.println("Requires 2 arguments: [location of Wikipedia XML] [output dir]");
      System.out.println("Got: " + Arrays.toString(args));
      return;
    }
    try {
      SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
      parser.parse(new File(args[0]), new WikidumpHandler(args[1], new LinkParser()));

    } catch (ParserConfigurationException e) {
      throw Throwables.propagate(e);
    } catch (SAXException e) {
      throw Throwables.propagate(e);
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }
}
