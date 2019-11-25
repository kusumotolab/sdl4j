package com.github.kusumotolab.sdl4j.tool.db.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import com.github.kusumotolab.sdl4j.tool.db.DB;
import com.github.kusumotolab.sdl4j.tool.db.DBObject;
import com.github.kusumotolab.sdl4j.tool.db.Query;
import com.google.common.collect.Lists;

public class SQLite implements DB {

  private static final String DEFAULT_SQLITE_PATH = "./sqlite3.db";
  private static boolean isInitialized = false;

  private final String sqliteURL;
  private Connection connection;

  public SQLite() {
    this(DEFAULT_SQLITE_PATH);
  }

  public SQLite(final String sqlitePath) {
    this.sqliteURL = "jdbc:sqlite:" + sqlitePath;

    if (!isInitialized) {
      initialize();
      isInitialized = true;
    }
  }

  private void initialize() {
    try {
      Class.forName("org.sqlite.JDBC");
    } catch (final ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void connect() {
    if (this.connection != null) {
      return;
    }
    try {
      this.connection = DriverManager.getConnection(sqliteURL, getProperties());
    } catch (final SQLException e) {
      e.printStackTrace();
    }
  }

  private static Properties getProperties() {
    Properties prop = new Properties();
    prop.put("journal_mode", "MEMORY");
    prop.put("sync_mode", "OFF");
    return prop;
  }

  @Override
  public void close() {
    if (this.connection == null) {
      return;
    }
    try {
      this.connection.close();
      this.connection = null;
    } catch (final SQLException e) {
      e.printStackTrace();
    }
  }

  public void createTable(final Class<? extends SQLiteObject> aClass) throws SQLException {
    final List<String> columns = SQLiteObject.getColumns(aClass)
        .stream()
        .map(Column::toString)
        .collect(Collectors.toList());

    final String command = "CREATE TABLE IF NOT EXISTS "
        + SQLiteObject.getDBName(aClass)
        + " ("
        + String.join(", ", columns)
        + ")";
    final Statement statement = connection.createStatement();
    statement.executeUpdate(command);
  }

  @Override
  public <T extends DBObject> void insert(final List<T> objects) {
    if (objects.isEmpty()) {
      return;
    }
    if (!(objects.get(0) instanceof SQLiteObject)) {
      return;
    }

    final List<SQLiteObject> list = objects.stream()
        .map(e -> ((SQLiteObject) e))
        .collect(Collectors.toList());

    try {
      connection.setAutoCommit(false);
      final String prepareStatementCommand = list.get(0)
          .prepareStatementCommand();
      final PreparedStatement prepareStatement = connection.prepareStatement(
          prepareStatementCommand);

      for (final SQLiteObject object : list) {
        object.addBatchCommand(prepareStatement);
      }
      prepareStatement.executeBatch();
      connection.commit();
    } catch (final IllegalAccessException | SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public <T extends DBObject> void update(final T object) {
    throw new UnsupportedOperationException();
  }

  @Override
  public <T> Collection<T> fetch(final Query<T> query) {
    return fetch(query, 10000);
  }

  public <T> Collection<T> fetch(final Query<T> query, final int fetchSize) {
    final List<T> result = Lists.newArrayList();
    try {
      final Statement statement = connection.createStatement();
      statement.setFetchSize(fetchSize);
      final String command = query.toCommand();

      final ResultSet resultSet = statement.executeQuery(command);
      while (resultSet.next()) {
        final T object = query.resolve(resultSet);
        result.add(object);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return result;
  }


  @Override
  public <T> void delete(final Query<T> query) {
    try {
      final Statement statement = connection.createStatement();
      final String command = query.toCommand();
      statement.execute(command);
    } catch (final SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void truncateTable(final Class<? extends DBObject> aClass) {
    @SuppressWarnings("unchecked")
    final Class<SQLiteObject> castedClass = (Class<SQLiteObject>) aClass;
    try {
      final Statement statement = connection.createStatement();
      final String command = "DROP TABLE " + SQLiteObject.getDBName(castedClass) + ";";
      statement.execute(command);
      createTable(castedClass);
    } catch (final SQLException e) {
      e.printStackTrace();
    }
  }
}
