package com.wikigraph.main;

import com.google.common.collect.Lists;
import com.wikigraph.index.LinkIndex;

import java.io.File;
import java.util.List;


public class TestMode implements RunMode {
  @Override
  public void run(String[] args) {
    List<Integer> l = Lists.newArrayListWithCapacity(10);
    l.set(9,1);
    System.out.println(l);
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
