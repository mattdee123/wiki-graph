package wikidump;

import com.google.common.base.Throwables;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ArticleNameResolver {

  private Map<String, String> redirects;

  public ArticleNameResolver(File baseDir) {
    File redirectsFile = new File(baseDir, "redirects");
    redirects = new HashMap<String, String>();
    try {
      BufferedReader redirectReader = new BufferedReader(new FileReader(redirectsFile));
      String line;
      System.out.println("Loading Redirects....");
      while ((line = redirectReader.readLine()) != null) {
        String[] redirect = line.split("\\|");
        if (redirect.length != 2) {
          System.err.println("ERROR: Invalid Redirect Line:" + line);
        } else {
        redirects.put(redirect[0], redirect[1]);
        }
      }
      System.out.printf("Loaded %d redirects!%n", redirects.size());
    } catch (IOException e) {
      e.printStackTrace();
      throw Throwables.propagate(e);
    }
  }

  // Takes the name of the link, returns the name of the associated article
  public String resolveName(String title) {
    title = capitalizeFirstLetter(title);
    String redirect = redirects.get(title);
    if (redirect != null) {
      System.out.printf("Redirecting %s to %s%n", title, redirect);
      return redirect;
    }
    // It doesn't redirect, return the orignial version
    return title;
  }

  private String capitalizeFirstLetter(String original){
    if(original.length() == 0)
      return original;
    return original.substring(0, 1).toUpperCase() + original.substring(1);
  }

}
