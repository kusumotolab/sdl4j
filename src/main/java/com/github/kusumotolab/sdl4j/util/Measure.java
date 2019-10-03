package com.github.kusumotolab.sdl4j.util;

import java.time.Duration;
import java.util.function.Supplier;
import com.google.common.base.Stopwatch;

public class Measure {

  public static <T> MeasuredResult<T> time(final Supplier<T> supplier) {
    final Stopwatch stopwatch = Stopwatch.createStarted();
    final T value = supplier.get();
    stopwatch.stop();
    return new MeasuredResult<>(value, stopwatch.elapsed());
  }

  public static class MeasuredResult<T> {
    private final T value;
    private final Duration duration;

    public MeasuredResult(final T value, final Duration duration) {
      this.value = value;
      this.duration = duration;
    }

    public T getValue() {
      return value;
    }

    public Duration getDuration() {
      return duration;
    }
  }
}
