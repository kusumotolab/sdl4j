package com.github.kusumotolab.sdl4j.algorithm.mining.tree;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class Freqt<T> implements SubtreeMining<T> {

  @Override
  public Set<TreePattern<T>> mining(final Set<Node<T>> trees, final double minimumSupport) {
    final Set<TreePattern<T>> results = Sets.newHashSet();
    final int borderline = extractBorderline(trees, minimumSupport);

    final Set<TreePattern<T>> f1 = extractF1(trees, borderline);
    results.addAll(f1);

    final Set<TreePattern<T>> f2 = extractF2(trees, f1, borderline);
    results.addAll(f2);

    int k = 2;
    Set<TreePattern<T>> fk = f2;
    while (!fk.isEmpty()) {
      final Set<TreePattern<T>> fkPlus1 = extractFkPlus1(trees, k, fk, f1, f2, borderline);
      k += 1;
      fk = fkPlus1;
    }
    return results;
  }

  private int extractBorderline(final Set<Node<T>> trees, final double minimumSupport) {
    final int sum = (int) trees.stream()
        .map(Node::getDescents)
        .mapToLong(Collection::size)
        .sum();
    final int border = (int) (((double) sum) * minimumSupport);
    return border;
  }

  private Set<TreePattern<T>> extractF1(final Set<Node<T>> trees, final int borderline) {
    final Map<T, Integer> map = Maps.newHashMap();

    trees.stream()
        .map(Node::getDescents)
        .flatMap(Collection::stream)
        .forEach(node -> {
          final Integer count = map.computeIfAbsent(node.getLabel(), e -> 0);
          map.put(node.getLabel(), count + 1);
        });

    return map.entrySet()
        .stream()
        .filter(e -> {
          final Integer count = e.getValue();
          return count > borderline;
        })
        .map(e -> new TreePattern<>(Node.createRootNode(e.getKey()), e.getValue()))
        .collect(Collectors.toSet());
  }

  private Set<TreePattern<T>> extractF2(final Set<Node<T>> trees, final Set<TreePattern<T>> f1,
      final int borderline) {
    final Map<String, Node<T>> c2 = Maps.newHashMap();
    for (final TreePattern<T> element1 : f1) {
      final T label1 = element1.getRootNode()
          .getLabel();
      for (final TreePattern<T> element2 : f1) {
        final Node<T> root = Node.createRootNode(label1);
        root.createChildNode(element2.getRootNode()
            .getLabel());
        final List<Label<T>> labels = root.getLabels();
        final String treeText = labels.stream()
            .map(Label::toString)
            .collect(Collectors.joining("・"));
        c2.put(treeText, root);
      }
    }
    return c2.values()
        .stream()
        .map(node -> {
          final int count = countPattern(trees, node);
          return new TreePattern<>(node, count);
        })
        .filter(e -> e.countPatten() > borderline)
        .collect(Collectors.toSet());
  }

  private Set<TreePattern<T>> extractFkPlus1(final Set<Node<T>> trees, final int k,
      final Set<TreePattern<T>> _fk, final Set<TreePattern<T>> f1, final Set<TreePattern<T>> f2,
      final int borderline) {
    final Set<Node<T>> candidates = Sets.newHashSet();
    for (final TreePattern<T> treePattern : _fk) {
      final Node<T> rootNode = treePattern.getRootNode();
      final List<Node<T>> rightMostBranch = rootNode.getRightMostBranch();

      for (int index = 0; index < rightMostBranch.size(); index++) {
        // Nodeのタイプとf2から挿入するラベルを選ぶ
        final Node<T> node = rightMostBranch.get(index);
        final Set<Node<T>> equalRootNodes = f2.stream()
            .map(TreePattern::getRootNode)
            .filter(t -> t.getLabel()
                .equals(node.getLabel()))
            .collect(Collectors.toSet());
        for (final Node<T> equalRootNode : equalRootNodes) {
          final List<Node<T>> children = equalRootNode.getChildren();
          final Node<T> child = children.get(0);
          final Node<T> copiedRootNode = rootNode.deepCopy();
          copiedRootNode.getRightMostBranch()
              .get(index)
              .createChildNode(child.getLabel());
          candidates.add(copiedRootNode);
        }
      }
    }

    return candidates.stream()
        .map(candidate -> {
          final int count = countPattern(trees, candidate);
          return new TreePattern<>(candidate, count);
        })
        .filter(candidate -> candidate.countPatten() > borderline)
        .collect(Collectors.toSet());
  }

  private int countPattern(final Set<Node<T>> rootTrees, final Node<T> subtree) {
    return ((int) rootTrees.stream()
        .filter(tree -> tree.contains(subtree))
        .count());
  }
}
