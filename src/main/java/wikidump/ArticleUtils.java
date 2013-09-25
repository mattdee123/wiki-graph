package wikidump;

import java.io.File;
import java.util.Map;

public class ArticleUtils {

  private final File baseDir;
  private final Map<String, String> redirects;

  public ArticleUtils(File baseDir, Map<String, String> redirects) {
    this.baseDir = baseDir;
    this.redirects = redirects;
  }

  public File getFileForPage(String title, File baseDir) {
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

  public static String resolveRedirect(String title, File baseDir) {
    File file = getFileForPage(title, baseDir);
    if (file.exists()) {
      return title;
    }
    if

    return null;
  }
}
