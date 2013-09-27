package com.wikigraph.wikidump;

import static com.wikigraph.wikidump.WikidumpHandler.Page;

public interface PageProcessor {

  public void processPage(Page currentPage);

  public void finish();

}
