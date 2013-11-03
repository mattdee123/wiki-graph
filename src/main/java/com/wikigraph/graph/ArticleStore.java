package com.wikigraph.graph;

/*
Class which represents the container of Article information and allows for retrieval of data.
 */
public interface ArticleStore {

  /*
  Returns the article associated with the title, or null if no article exists.  Note that title must be exactly the
  same as the stored title.
   */

  public Article forTitle(String title);

}
