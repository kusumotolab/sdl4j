package com.github.kusumotolab.sdl4j.tool.db;

public interface Query<T> {

  String toCommand();

  T resolve(final Object object);
}
