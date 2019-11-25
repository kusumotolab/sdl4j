package com.github.kusumotolab.sdl4j.algorithm.mining.tree;

import java.util.Objects;

public class Label<T> {

  private final int depth;
  private final T label;

  public Label(final int depth, final T label) {
    this.depth = depth;
    this.label = label;
  }

  public int getDepth() {
    return depth;
  }

  public T getLabel() {
    return label;
  }

  @Override
  public String toString() {
    return "(" + depth + ", " + label.toString() + ")";
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final Label<?> label1 = (Label<?>) o;
    return depth == label1.depth &&
        Objects.equals(label, label1.label);
  }

  @Override
  public int hashCode() {
    return Objects.hash(depth, label);
  }
}
