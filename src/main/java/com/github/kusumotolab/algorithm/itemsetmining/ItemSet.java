package com.github.kusumotolab.algorithm.itemsetmining;

import java.util.Collection;
import java.util.HashSet;

public class ItemSet<T> extends HashSet<T> {
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

  public int count() {
    return counter;
  }
}
