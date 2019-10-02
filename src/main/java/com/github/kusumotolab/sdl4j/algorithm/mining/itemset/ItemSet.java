package com.github.kusumotolab.sdl4j.algorithm.mining.itemset;

import java.util.Collection;
import java.util.HashSet;
import com.github.kusumotolab.sdl4j.algorithm.mining.CountablePattern;

public class ItemSet<T> extends HashSet<T> implements CountablePattern {
  private int counter = 0;

  public ItemSet() {
  }

  public ItemSet(final T item) {
    this.add(item);
  }

  public ItemSet(final Collection<? extends T> c) {
    super(c);
  }

  void setCounter(int counter) {
    this.counter = counter;
  }

  @Override
  public int countPatten() {
    return counter;
  }
}
