package com.github.kusumotolab.algorithm.itemsetmining;

import java.util.Collection;
import java.util.HashSet;

public class ItemSet<T> extends HashSet<T> {
  private int counter = 0;

  public ItemSet() {
  }

  public ItemSet(final Collection<? extends T> c) {
    super(c);
  }

  synchronized void setCounter(int counter) {
    this.counter = counter;
  }

  public int count() {
    return counter;
  }
}
