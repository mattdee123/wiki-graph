package com.wikigraph.sql;

import com.google.common.base.Throwables;
import com.google.common.hash.Hashing;
import com.mysql.jdbc.Driver;
import com.wikigraph.graph.Article;
import com.wikigraph.graph.ArticleStore;
import com.wikigraph.wikidump.TitleFixer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.google.common.base.Charsets.UTF_8;

public class SqlStore implements ArticleStore {
  private static final String QUERY_SQL = "SELECT name, id, redirect FROM articles WHERE hash = ?";
  private static final int NAME_COL = 1;
  private static final int ID_COL = 2;
  private static final int REDIRECT_COL = 3;
  private final Connection connection;
  private final PreparedStatement query;
  private TitleFixer titleFixer = TitleFixer.getFixer();


  public SqlStore(String dbName) {
    try {
      DriverManager.registerDriver(new Driver());
      System.out.println("Connecting...");

      connection = DriverManager.getConnection("jdbc:mysql://localhost/" + dbName, "root", "");
      query = connection.prepareStatement(QUERY_SQL);
      System.out.println("Connected!");
    } catch (SQLException e) {
      throw Throwables.propagate(e);
    }
    // Add shutdown hook
    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        try {
          connection.close();
          query.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    });
  }


  @Override
  public Article forTitle(String title) {
    System.out.println("Getting article for " + title);
    long hash = Hashing.sha256().hashString(titleFixer.toTitle(title), UTF_8).asLong();
    try {

      query.setLong(1, hash);
      ResultSet result = query.executeQuery();

      while (result.next()) {
        String resultTitle = result.getString(NAME_COL);
        if (resultTitle.equals(title)) {
          int id = result.getInt(ID_COL);
          boolean redirect = result.getBoolean(REDIRECT_COL);
          return new SqlArticle(connection, title, id, redirect);
        } else {
          System.err.printf("Bad match: title=%s, retreived=%s, hash=%d%n", title, resultTitle, hash);
        }
      }
      System.out.printf("Did not find article:%s%n", title);
      return null;

    } catch (SQLException e) {
      throw Throwables.propagate(e);
    }
  }
}
