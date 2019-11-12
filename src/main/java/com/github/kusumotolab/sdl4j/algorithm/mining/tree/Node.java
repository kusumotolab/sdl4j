package com.github.kusumotolab.sdl4j.algorithm.mining.tree;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class Node<T> {

  private final T label;
  private final Node<T> parent;
  private final int position;
  private final List<Node<T>> children = Lists.newArrayList();

  private Node(final T label, final Node<T> parent, final int position) {
    this.label = label;
    this.parent = parent;
    this.position = position;
  }

  public static <T> Node<T> createTree(final List<Label<T>> labels) {
    final Label<T> rootLabel = labels.get(0);
    final Node<T> rootNode = createRootNode(rootLabel.getLabel());

    if (labels.size() == 1) {
      return rootNode;
    }

    // depth -> node
    final Map<Integer, Node<T>> nodes = Maps.newHashMap();
    nodes.put(0, rootNode);

    for (int i = 1; i < labels.size(); i++) {
      final Label<T> label = labels.get(i);
      final int depth = label.getDepth();
      final Node<T> parentNode = nodes.get(depth - 1);
      final Node<T> node = parentNode.createChildNode(label.getLabel());
      nodes.put(depth, node);
    }

    return rootNode;
  }

  public Node<T> deepCopy() {
    return Node.createTree(getLabels());
  }

  public static <T> Node<T> createRootNode(final T label) {
    return new Node<>(label, null, 0);
  }

  public Node<T> createChildNode(final T label) {
    final Node<T> node = new Node<>(label, this, this.children.size());
    children.add(node);
    return node;
  }

  public T getLabel() {
    return label;
  }

  public Node<T> getParent() {
    return parent;
  }

  public int getPosition() {
    return position;
  }

  public List<Node<T>> getChildren() {
    return children;
  }

  public List<Node<T>> getDescents() {
    final List<Node<T>> list = Lists.newArrayList();
    list.add(this);
    for (final Node<T> child : children) {
      final List<Node<T>> descents = child.getDescents();
      list.addAll(descents);
    }
    return list;
  }

  public List<Label<T>> getLabels() {
    return getLabels(0);
  }

  private List<Label<T>> getLabels(final int depth) {
    final List<Label<T>> labels = Lists.newArrayList();
    labels.add(new Label<>(depth, this.getLabel()));
    for (final Node<T> child : getChildren()) {
      labels.addAll(child.getLabels(depth + 1));
    }
    return labels;
  }

  public Node<T> getRightChild() {
    if (children.isEmpty()) {
      return null;
    }
    return children.get(children.size() - 1);
  }

  public List<Node<T>> getRightMostBranch() {
    final List<Node<T>> branch = Lists.newArrayList();
    branch.add(this);
    if (!isLeaf()) {
      branch.addAll(getRightChild().getRightMostBranch());
    }
    return branch;
  }

  public boolean isLeaf() {
    return children.isEmpty();
  }

  public int countPatterns(final Node<T> subtree) {
    int counter = 0;
    final T subtreeLabel = subtree.getLabel();
    for (final Node<T> node : getDescents()) {
      final T label = node.getLabel();
      if (!label.equals(subtreeLabel)) {
        continue;
      }
      if (node.containsInOrder(subtree)) {
        counter+= 1;
      }
    }
    return counter;
  }

  // root同士の比較
  private boolean containsInOrder(final Node<T> subtree) {
    final List<Label<T>> thisLabels = getLabels();
    final List<Label<T>> subtreeLabels = subtree.getLabels();
    int index = 0;
    for (final Label<T> element : thisLabels) {
      final Label<T> subtreeLabel = subtreeLabels.get(index);
      if (!subtreeLabel.equals(element)) {
        continue;
      }
      index++;
      if (index == subtreeLabels.size()) {
        return true;
      }
    }
    return false;

  }
  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final Node<?> node = (Node<?>) o;
    return Objects.equals(label, node.label) &&
        Objects.equals(children, node.children); // childrenが末端まで行って遅い可能性あり
  }

  @Override
  public int hashCode() {
    // childrenが末端まで行って遅い可能性あり
    return Objects.hash(label, children);
  }

  public String toLongString() {
    final List<Label<T>> labels = getLabels();
    final StringBuilder stringBuilder = new StringBuilder();
    for (final Label<T> label : labels) {
      if (stringBuilder.length() != 0) {
        stringBuilder.append("\n");
      }
      for (int i = 0; i < label.getDepth(); i++) {
        stringBuilder.append("  ");
      }
      stringBuilder.append(label.toString());
    }
    return stringBuilder.toString();
  }
}
