package com.github.kusumotolab.sdl4j.algorithm.mining.tree;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

public class Freqt<T> implements SubtreeMining<T> {

  private final Observer<T> observer;

  public Freqt() {
    this(new Observer<>());
  }

  public Freqt(final Observer<T> observer) {
    this.observer = observer;
  }

  @Override
  public Set<TreePattern<T>> mining(final Set<Node<T>> trees, final double minimumSupport) {
    final Set<TreePattern<T>> results = Sets.newHashSet();

    observer.start(trees, minimumSupport);
    final int borderline = extractBorderline(trees, minimumSupport);

    int k = 1;
    final Set<TreePattern<T>> f1 = extractF1(trees, borderline);
    observer.willAddNewFk(f1, k, results);
    results.addAll(f1);
    k += 1;

    final Set<TreePattern<T>> f2 = extractF2(trees, f1, borderline);
    observer.willAddNewFk(f2, k, results);
    results.addAll(f2);
    k += 1;

    final Multimap<T, Node<T>> f2Map = createF2Map(f1, f2);

    Set<TreePattern<T>> fk = f2;
    while (!fk.isEmpty()) {
      final Set<TreePattern<T>> fkPlus1 = extractFkPlus1(trees, fk, f2Map, borderline);
      observer.willAddNewFk(fkPlus1, k, results);
      results.addAll(fkPlus1);
      fk = fkPlus1;
      k += 1;
    }

    observer.finish(results);
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
    final Set<Node<T>> element = trees.stream()
        .map(Node::getDescents)
        .flatMap(Collection::stream)
        .collect(Collectors.toSet());

    return element.stream()
        .map(e -> Node.createRootNode(e.getTreeId(), e.getLabel()))
        .map(candidate -> {
          final CountResult countResult = countPattern(trees, candidate);
          return new TreePattern<>(candidate, countResult.ids, countResult.count);
        })
        .filter(e -> e.countPatten() >= borderline)
        .collect(Collectors.toSet());
  }

  private Set<TreePattern<T>> extractF2(final Set<Node<T>> trees, final Set<TreePattern<T>> f1, final int borderline) {
    final Set<Node<T>> candidates = Sets.newHashSet();
    for (final TreePattern<T> element1 : f1) {
      final T label = element1.getRootNode().getLabel();

      for (final TreePattern<T> element2 : f1) {
        final Node<T> root = Node.createRootNode(element1.getRootNode().getTreeId(), label);
        root.createChildNode(element2.getRootNode().getLabel());
        candidates.add(root);
      }
    }

    return candidates.stream()
        .map(node -> {
          final CountResult countResult = countPattern(trees, node);
          return new TreePattern<>(node, countResult.ids, countResult.count);
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
          final CountResult countResult = countPattern(trees, candidate);
          return new TreePattern<>(candidate, countResult.ids, countResult.count);
        })
        .filter(candidate -> candidate.countPatten() >= borderline)
        .collect(Collectors.toSet());
  }

  private CountResult countPattern(final Set<Node<T>> rootTrees, final Node<T> subtree) {
    return rootTrees.stream()
        .map(tree -> {
          final int countPatterns = tree.countPatterns(subtree);
          return new CountResult(countPatterns, tree.getTreeId());
        })
        .reduce((e1, e2) -> {
          final Set<String> ids = Sets.newHashSet(e1.ids);
          ids.addAll(e2.ids);
          return new CountResult(e1.count + e2.count, ids);
        })
        .orElse(new CountResult(0, Collections.emptySet()));
  }

  public static class Observer<T> {

    public void start(final Set<Node<T>> rootNodes, final double minimumSupport) {
    }

    public void finish(final Set<TreePattern<T>> output) {
    }

    public void willAddNewFk(final Set<TreePattern<T>> fk, int k, final Set<TreePattern<T>> results) {
    }
  }

  private static class CountResult {
    private final int count;
    private final Set<String> ids;

    public CountResult(final int count, final Set<String> ids) {
      this.count = count;
      this.ids = ids;
    }

    public CountResult(final int count, final String id) {
      this.count = count;
      ids = Sets.newHashSet(id);
    }
  }
}
