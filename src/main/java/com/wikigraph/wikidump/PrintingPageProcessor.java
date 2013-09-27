package com.wikigraph.wikidump;

public class PrintingPageProcessor implements PageProcessor {
  @Override
  public void processPage(WikidumpHandler.Page page) {
    System.out.println("==========Page Found!==========");
    System.out.println("page.title = " + page.title);
    System.out.println("page.redirect = " + page.redirect);
    System.out.println("page.text = " + page.text);
  }

  @Override
  public void finish() { }
}
