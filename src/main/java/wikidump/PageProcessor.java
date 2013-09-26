package wikidump;

import static wikidump.WikidumpHandler.Page;

public interface PageProcessor {

  public void processPage(Page currentPage);

  public void finish();

}
