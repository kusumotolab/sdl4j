package com.github.kusumotolab.sdl4j.util;

import java.util.function.Function;

public class Try {

  @FunctionalInterface
  public interface ThrowableFunction<T, R> {

    R apply(final T t) throws Exception;
  }

  public static <T, R>  Function<T, R> function(final ThrowableFunction<T, R> function) {
    return t -> {
      try {
        return function.apply(t);
      } catch (final Exception e) {
        e.printStackTrace();
      }
      return null;
    };
  }
}
