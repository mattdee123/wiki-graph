package wikidump;

import java.io.BufferedWriter;
import java.io.File;

public class ArticleFileUtils {

  private final File baseDir;

  public ArticleFileUtils(File baseDir) {
    this.baseDir = baseDir;
  }

  public File getFileForPage(String title) {
    title = title.replace('/', '|');
    if (title.length() > 255) {
      System.err.printf("Warning: title truncated.  original=%s, after=%s%n", title, title.substring(0, 255));
      title = title.substring(0, 255);
    }
    // Guarantees this will be exactly 15 characters long
    String hash = String.format("%015d", title.hashCode());
    String dir1 = ("" + hash.charAt(14)) + hash.charAt(12) + hash.charAt(10);
    String dir2 = ("" + hash.charAt(13)) + hash.charAt(11) + hash.charAt(9);
    File file = new File(baseDir, dir1);
    file = new File(file, "" + dir2);
    file = new File(file, title);
    return file;
  }

  public File getRedirectFile() {
    return new File(baseDir, "redirects");
  }

}
