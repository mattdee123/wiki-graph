package wikidump;

import com.google.common.base.Joiner;
import com.google.common.base.Throwables;
import com.wikigraph.LinkParser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PageWriter implements PageProcessor {
  private static final Joiner joiner = Joiner.on('\n');

  private final LinkParser linkParser;
  private final ArticleFileUtils fileUtils;
  private final BufferedWriter redirectWriter;

  public PageWriter(LinkParser linkParser, ArticleFileUtils fileUtils) {
    this.linkParser = linkParser;
    this.fileUtils = fileUtils;
    try {
      this.redirectWriter = new BufferedWriter(new FileWriter(fileUtils.getRedirectFile()));
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }


  @Override
  public void processPage(WikidumpHandler.Page currentPage) {
    if (currentPage.redirect == null) {
      writeText(currentPage.title, joiner.join(linkParser.getConnections(currentPage.text)) + "\n");
    } else {
      try {
        if (currentPage.redirect.length() == 0) {
          System.err.println("Length zero redirect : %s" + currentPage.title);
        }
        redirectWriter.write(currentPage.title + "|" + currentPage.redirect + "\n");
      } catch (IOException e) {
        throw Throwables.propagate(e);
      }
    }
  }

  @Override
  public void finish() {
    try {
      redirectWriter.close();
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }

  private void writeText(String title, String output) {
    File file = fileUtils.getFileForPage(title);
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
      System.err.println("title = " + title);
      System.err.println("text = " + output);
      throw Throwables.propagate(e);
    }
  }
}
