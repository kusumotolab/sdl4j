package com.github.kusumotolab.sdl4j.util;

import java.util.function.Supplier;

public class Measure {

  public static <T> MeasuredResult<T> time(final Supplier<T> supplier) {
    final long startTime = System.currentTimeMillis();
    final T value = supplier.get();
    final long endTime = System.currentTimeMillis();
    return new MeasuredResult<>(value, startTime - endTime);
  }

  public static class MeasuredResult<T> {
    private final T value;
    private final long time;

    public MeasuredResult(final T value, final long time) {
      this.value = value;
      this.time = time;
    }

    public T getValue() {
      return value;
    }

    public long getMsTime() {
      return time;
    }
  }
}
