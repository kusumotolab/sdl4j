package com.github.kusumotolab.sdl4j.algorithm.mining.tree;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
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

    final Multimap<T, Node<T>> f2Map = createF2Map(f1, f2);

    Set<TreePattern<T>> fk = f2;
    while (!fk.isEmpty()) {
      final Set<TreePattern<T>> fkPlus1 = extractFkPlus1(trees, fk, f2Map, borderline);
      results.addAll(fkPlus1);
      fk = fkPlus1;
    }
    return results;
  }

  private int extractBorderline(final Set<Node<T>> trees, final double minimumSupport) {
    final int sum = (int) trees.stream()
        .map(Node::getDescents)
        .mapToLong(Collection::size)
        .sum();
    return (int) (((double) sum) * minimumSupport);
  }

  private Set<TreePattern<T>> extractF1(final Set<Node<T>> trees, final int borderline) {
    final Map<T, Integer> map = Maps.newHashMap();

    trees.stream()
        .map(Node::getDescents)
        .flatMap(Collection::stream)
        .forEach(node -> {
          final T label = node.getLabel();
          final Integer count = map.computeIfAbsent(label, e -> 0);
          map.put(label, count + 1);
        });

    return map.entrySet()
        .stream()
        .filter(e -> {
          final Integer count = e.getValue();
          return count >= borderline;
        })
        .map(e -> new TreePattern<>(Node.createRootNode(e.getKey()), e.getValue()))
        .collect(Collectors.toSet());
  }

  private Set<TreePattern<T>> extractF2(final Set<Node<T>> trees, final Set<TreePattern<T>> f1,
      final int borderline) {
    final Set<Node<T>> candidates = Sets.newHashSet();
    for (final TreePattern<T> element1 : f1) {
      final T label1 = element1.getRootNode()
          .getLabel();
      for (final TreePattern<T> element2 : f1) {
        final Node<T> root = Node.createRootNode(label1);
        root.createChildNode(element2.getRootNode()
            .getLabel());
        candidates.add(root);
      }
    }
    return candidates.stream()
        .map(node -> {
          final int count = countPattern(trees, node);
          return new TreePattern<>(node, count);
        })
        .filter(e -> e.countPatten() >= borderline)
        .collect(Collectors.toSet());
  }

  private Multimap<T, Node<T>> createF2Map(final Set<TreePattern<T>> f1,
      final Set<TreePattern<T>> f2) {
    final Multimap<T, Node<T>> f2Map = ArrayListMultimap.create(f1.size(), f2.size());
    for (final TreePattern<T> pattern : f2) {
      final Node<T> rootNode = pattern.getRootNode();
      f2Map.put(rootNode.getLabel(), rootNode.getRightChild());
    }
    return f2Map;
  }

  private Set<TreePattern<T>> extractFkPlus1(final Set<Node<T>> trees, final Set<TreePattern<T>> fk,
      final Multimap<T, Node<T>> f2Cache, final int borderline) {
    final Set<Node<T>> candidates = Sets.newHashSet();
    for (final TreePattern<T> treePattern : fk) {
      final Node<T> rootNode = treePattern.getRootNode();
      final List<Node<T>> rightMostBranch = rootNode.getRightMostBranch();

      for (int index = 0; index < rightMostBranch.size(); index++) {
        // Nodeのタイプとf2から挿入するラベルを選ぶ
        final Node<T> node = rightMostBranch.get(index);
        final Set<Node<T>> candidateNodes = Sets.newHashSet(f2Cache.get(node.getLabel()));
        for (final Node<T> candidateNode : candidateNodes) {
          final Node<T> copiedRootNode = rootNode.deepCopy();
          copiedRootNode.getRightMostBranch()
              .get(index)
              .createChildNode(candidateNode.getLabel());
          candidates.add(copiedRootNode);
        }
      }
    }

    return candidates.stream()
        .map(candidate -> {
          final int count = countPattern(trees, candidate);
          return new TreePattern<>(candidate, count);
        })
        .filter(candidate -> candidate.countPatten() >= borderline)
        .collect(Collectors.toSet());
  }

  private int countPattern(final Set<Node<T>> rootTrees, final Node<T> subtree) {
    return rootTrees.stream()
        .map(tree -> tree.countPatterns(subtree))
        .reduce(Integer::sum)
        .orElse(0);
  }
}
