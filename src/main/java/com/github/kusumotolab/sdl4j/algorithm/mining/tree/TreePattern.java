package com.github.kusumotolab.sdl4j.algorithm.mining.tree;

import java.util.Set;
import com.github.kusumotolab.sdl4j.algorithm.mining.CountablePattern;

public class TreePattern<T> implements CountablePattern {

  private final Node<T> rootNode;
  private final Set<String> treeIds;
  private int count;

  public TreePattern(final Node<T> rootNode, final Set<String> treeIds, final int count) {
    this.rootNode = rootNode;
    this.treeIds = treeIds;
    this.count = count;
  }

  public TreePattern<T> deepCopy() {
    return new TreePattern<>(rootNode.deepCopy(), treeIds, count);
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
