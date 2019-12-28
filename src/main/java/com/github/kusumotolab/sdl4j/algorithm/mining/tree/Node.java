package com.github.kusumotolab.sdl4j.algorithm.mining.tree;

import java.lang.ref.SoftReference;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

public class Node<T> {

  private final String treeId;
  private final T label;
  private final Node<T> parent;
  private final int position;
  private final List<Node<T>> children = Lists.newArrayList();

  private SoftReference<Multimap<T, Node<T>>> cacheNodeMap = new SoftReference<>(null);
  private SoftReference<List<Node<T>>> cacheDescents = new SoftReference<>(null);
  private SoftReference<List<Label<T>>> cacheLabels = new SoftReference<>(null);

  private Node(final String treeId, final T label, final Node<T> parent, final int position) {
    this.treeId = treeId;
    this.label = label;
    this.parent = parent;
    this.position = position;
  }

  public static <T> Node<T> createTree(final String treeId, final List<Label<T>> labels) {
    final Label<T> rootLabel = labels.get(0);
    final Node<T> rootNode = createRootNode(treeId, rootLabel.getLabel());

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

  public static <T> Node<T> createRootNode(final String treeId, final T label) {
    return new Node<>(treeId, label, null, 0);
  }

  public Node<T> deepCopy() {
    return Node.createTree(treeId, getLabels());
  }

  public Node<T> createChildNode(final T label) {
    final Node<T> node = new Node<>(treeId, label, this, this.children.size());
    children.add(node);
    clearCache();
    return node;
  }

  private void clearCache() {
    cacheDescents.clear();
    cacheNodeMap.clear();
    if (parent != null) {
      parent.clearCache();
    }
  }

  private Multimap<T, Node<T>> getCacheMap() {
    Multimap<T, Node<T>> multimap = cacheNodeMap.get();
    if (multimap != null) {
      return multimap;
    }
    multimap = ArrayListMultimap.create();
    for (final Node<T> node : getDescents()) {
      multimap.put(node.getLabel(), node);
    }
    this.cacheNodeMap = new SoftReference<>(multimap);
    return multimap;
  }

  public String getTreeId() {
    return treeId;
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
    final List<Node<T>> cache = cacheDescents.get();
    if (cache != null) {
      return cache;
    }
    final List<Node<T>> list = Lists.newArrayList();
    list.add(this);
    for (final Node<T> child : children) {
      final List<Node<T>> descents = child.getDescents();
      list.addAll(descents);
    }
    this.cacheDescents = new SoftReference<>(list);
    return list;
  }

  public List<Label<T>> getLabels() {
    final List<Label<T>> cache = cacheLabels.get();
    if (cache != null) {
      return cache;
    }
    final List<Label<T>> labels = getLabels(0);
    cacheLabels = new SoftReference<>(labels);
    return labels;
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
    return ((int) getCacheMap().get(subtree.getLabel())
        .stream()
        .filter(node -> node.contains(subtree))
        .count());

  }

  // root同士の比較
  private boolean contains(final Node<T> subtree) {
    final List<Node<T>> subtreeChildren = subtree.getChildren();
    final List<Node<T>> children = this.getChildren();

    int index = 0;
    for (final Node<T> subtreeChild : subtreeChildren) {
      final T subtreeLabel = subtreeChild.getLabel();

      boolean foundMatchedSubtreeChild = false;

      for (int i = index; i < children.size(); i++) {
        final Node<T> child = children.get(i);
        if (!child.getLabel().equals(subtreeLabel)) {
          continue;
        }

        if (child.contains(subtreeChild)) {
          index = i + 1;
          foundMatchedSubtreeChild = true;
          break;
        }
      }
      if (!foundMatchedSubtreeChild) {
        return false;
      }
    }
    return true;
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
