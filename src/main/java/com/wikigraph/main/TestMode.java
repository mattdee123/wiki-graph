package com.wikigraph.main;

import com.wikigraph.index.LinkIndex;

import java.io.File;


public class TestMode implements RunMode {
  @Override
  public void run(String[] args) {
    File in = new File(args[0]);
    LinkIndex index = new LinkIndex(in);
    for (int i = 0; i < 11; i++) {
      System.out.println(index.forIndex(i));
    }
  }

  /* LOAD DATA INFILE '/Users/mattdee/Development/articles.csv' into table articles FIELDS TERMINATED BY '|';

     CREATE TABLE links( start INTEGER NOT NULL, end INTEGER NOT NULL, INDEX (start), INDEX (end));

      CREATE TABLE articles (id INTEGER NOT NULL, hash BIGINT NOT NULL, name VARCHAR(512) NOT NULL,
      redirect BOOL NOT NULL,
      PRIMARY KEY (id),
      UNIQUE INDEX(hash)) CHARACTER SET ISO-8859-1;
      INSERT INTO links (start,end) VALUES (1,1);
      for i in `ls`; do echo $i; mysql -uroot -pmatt -e "LOAD DATA INFILE '/data/articles/$i' into table wikipedia.articles FIELDS TERMINATED BY '|'"; done;
   */
}
