package com.wikigraph.sql;

import com.google.common.base.Stopwatch;
import com.google.common.base.Throwables;
import com.wikigraph.graph.Article;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class SqlArticle extends Article {



  private static final String OUTGOING_SQL =
          "SELECT id, name, redirect FROM links JOIN articles on id=end where start=? LIMIT ?;";
  private static final String INCOMING_SQL =
          "SELECT id, name, redirect FROM links JOIN articles on id=start where end=? LIMIT ?;";
  private static final int ID_COL = 1;
  private static final int NAME_COL = 2;
  private static final int REDIRECT_COL = 3;

  private final Connection connection;
  private final Stopwatch stopwatch;
  private final String title;
  private final int id;
  private final boolean isRedirect;

  public SqlArticle(Connection connection, String title, int id, boolean redirect) {
    this.connection = connection;
    this.title = title;
    stopwatch = new Stopwatch();
    this.id = id;
    this.isRedirect = redirect;
  }

  @Override
  public String getTitle() {
    return title;
  }

  @Override
  public boolean isRedirect() {
    return isRedirect;
  }

  @Override
  public Collection<Article> getIncomingLinks(int limit) {
    try (PreparedStatement query = connection.prepareStatement(INCOMING_SQL)) {
      query.setInt(1, id);
      query.setInt(2, limit);
      stopwatch.reset().start();
      Collection<Article> result = articlesForQuery(query);
      stopwatch.stop();
      System.out.printf("Found %d incoming links for %s in %dms%n",
              result.size(), title, stopwatch.elapsed(MILLISECONDS));
      return result;
    } catch (SQLException e) {
      throw Throwables.propagate(e);
    }
  }

  @Override
  public Collection<Article> getOutgoingLinks(int limit) {
    try (PreparedStatement query = connection.prepareStatement(OUTGOING_SQL)) {
      query.setInt(1, id);
      query.setInt(2, limit);
      stopwatch.reset().start();
      Collection<Article> result = articlesForQuery(query);
      stopwatch.stop();
      System.out.printf("Found %d outgoing links for %s in %dms%n",
              result.size(), title, stopwatch.elapsed(MILLISECONDS));
      return result;
    } catch (SQLException e) {
      throw Throwables.propagate(e);
    }
  }

  private Collection<Article> articlesForQuery(PreparedStatement query) throws SQLException {
    ResultSet result = query.executeQuery();
    List<Article> articles = new ArrayList<>();
    while (result.next()) {
      Article newArticle = new SqlArticle(
              connection, result.getString(NAME_COL), result.getInt(ID_COL), result.getBoolean(REDIRECT_COL));
      articles.add(newArticle);
    }
    return articles;
  }
}
