package com.github.kusumotolab.sdl4j.algorithm.mining.sequential;

import java.util.ArrayList;
import java.util.Collection;
import com.github.kusumotolab.sdl4j.algorithm.mining.CountablePattern;

public class SequentialPattern<Item> extends ArrayList<Item> implements CountablePattern {
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

  @Override
  public int countPatten() {
    return counter;
  }
}
