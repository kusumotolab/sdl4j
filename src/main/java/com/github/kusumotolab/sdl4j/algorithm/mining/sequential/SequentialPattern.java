package com.github.kusumotolab.sdl4j.algorithm.mining.sequential;

import java.util.ArrayList;
import java.util.Collection;

public class SequentialPattern<Item> extends ArrayList<Item> {
  private int counter = 0;

  public SequentialPattern() {
  }

  public SequentialPattern(final Item item) {
    this.add(item);
  }

  public SequentialPattern(final Collection<? extends Item> c) {
    super(c);
  }

  void setCounter(int counter) {
    this.counter = counter;
  }

  public int count() {
    return counter;
  }
}
