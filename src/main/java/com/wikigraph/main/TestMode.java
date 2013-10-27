package com.wikigraph.main;

import com.google.common.base.Throwables;
import com.mysql.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class TestMode implements RunMode {
  @Override
  public void run(String[] args) {
    Connection connection = null;
    try {
      DriverManager.registerDriver(new Driver());
      System.out.println("Connecting...");
      connection = DriverManager.getConnection("jdbc:mysql://localhost/wikipedia", "root", "");
      Statement statement = connection.createStatement();
      statement.executeUpdate("CREATE TABLE wikipedia");
      System.out.println("Closing...");
    } catch (SQLException e) {
      throw Throwables.propagate(e);
    }
  }

  /* LOAD DATA INFILE '/Users/mattdee/Development/articles.csv' into table articles FIELDS TERMINATED BY ',';
     CREATE TABLE links( start INTEGER NOT NULL, end INTEGER NOT NULL, INDEX (start), INDEX (end));
      CREATE TABLE articles (id INTEGER NOT NULL, hash BIGINT NOT NULL, name VARCHAR(512) NOT NULL,
      redirect BOOL NOT NULL,
      PRIMARY KEY (id),
      UNIQUE INDEX(hash)) CHARACTER SET ISO-8859-1;
      INSERT INTO links (start,end) VALUES (1,1);
   */
}
