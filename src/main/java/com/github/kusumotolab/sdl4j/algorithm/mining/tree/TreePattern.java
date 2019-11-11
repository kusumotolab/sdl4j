package com.github.kusumotolab.sdl4j.algorithm.mining.tree;

import com.github.kusumotolab.sdl4j.algorithm.mining.CountablePattern;

public class TreePattern<T> implements CountablePattern {

  private final Node<T> rootNode;
  private int count;

  public TreePattern(final Node<T> rootNode) {
    this.rootNode = rootNode;
    this.count = 0;
  }

  public TreePattern(final Node<T> rootNode, final int count) {
    this.rootNode = rootNode;
    this.count = count;
  }

  public TreePattern<T> deepCopy() {
    return new TreePattern<>(rootNode.deepCopy(), count);
  }

  public Node<T> getRootNode() {
    return rootNode;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final TreePattern<?> that = (TreePattern<?>) o;
    return rootNode.equals(that.rootNode);
  }

  @Override
  public int hashCode() {
    return rootNode.hashCode();
  }

  @Override
  public int countPatten() {
    return count;
  }
}
