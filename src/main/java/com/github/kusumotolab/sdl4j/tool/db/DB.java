package com.github.kusumotolab.sdl4j.tool.db;

import java.util.Collection;
import java.util.List;

public interface DB {

  void connect();

  void close();

  <T extends DBObject> void insert(final List<T> objects);

  <T extends DBObject> void update(final T object);

  <T> Collection<T> fetch(final Query<T> query);

  <T> void delete(final Query<T> query);

  void truncateTable(final Class<? extends DBObject> aClass);
}
