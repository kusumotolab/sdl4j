package com.github.kusumotolab.sdl4j.algorithm.mining.sequential;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class PrefixPattern<Item> implements SequentialPatternMining<Item> {

  private final Observer<Item> observer;

  public PrefixPattern() {
    this(new Observer<>());
  }

  public PrefixPattern(final Observer<Item> observer) {
    this.observer = observer;
  }

  @Override
  public Set<SequentialPattern<Item>> execute(final Set<List<Item>> transactions, final int theta) {
    observer.start(transactions, theta);
    final Set<SequentialPattern<Item>> output = Sets.newHashSet();
    prefixPattern(transactions, theta, Lists.newArrayList(), output);
    observer.finish(output);

    return output;
  }

  private void prefixPattern(final Set<List<Item>> transactions, final int theta,
      final List<Item> prefix, final Set<SequentialPattern<Item>> output) {
    observer.beginCalculatingForPrefix(prefix, output);
    final Set<Item> occurredElements = extractOccurredElements(transactions, theta);
    for (final Item item : occurredElements) {
      final Set<List<Item>> newTransactions = transactions.stream()
          .filter(e -> e.contains(item))
          .map(
              transaction -> transaction.subList(transaction.indexOf(item) + 1, transaction.size()))
          .filter(e -> !e.isEmpty())
          .collect(Collectors.toSet());

      final List<Item> newPrefix = Lists.newArrayList(prefix);
      newPrefix.add(item);

      final SequentialPattern<Item> sequentialPattern = new SequentialPattern<>(newPrefix);
      output.add(sequentialPattern);
      observer.foundNewSequentialPattern(prefix, sequentialPattern, output);

      prefixPattern(newTransactions, theta, newPrefix, output);
    }
    observer.enCalculatingForPrefix(prefix, output);
  }

  private Set<Item> extractOccurredElements(final Set<List<Item>> transactions, final int theta) {
    final Map<Item, Integer> map = new HashMap<>();

    transactions.stream()
        .map(HashSet::new)
        .flatMap(Collection::stream)
        .forEach(element -> {
          Integer count = map.computeIfAbsent(element, e -> 0);
          count += 1;
          map.put(element, count);
        });

    return map.entrySet()
        .stream()
        .filter(e -> e.getValue() >= theta)
        .map(Entry::getKey)
        .collect(Collectors.toSet());
  }

  public static class Observer<Item> {

    public void start(final Set<List<Item>> transactions, final int theta) {
    }

    public void finish(final Set<SequentialPattern<Item>> output) {
    }

    public void beginCalculatingForPrefix(final List<Item> prefixList,
        final Set<SequentialPattern<Item>> output) {
    }

    public void enCalculatingForPrefix(final List<Item> prefixList,
        final Set<SequentialPattern<Item>> output) {
    }

    public void foundNewSequentialPattern(final List<Item> prefixList,
        final SequentialPattern<Item> newPattern, final Set<SequentialPattern<Item>> output) {
    }
  }
}
