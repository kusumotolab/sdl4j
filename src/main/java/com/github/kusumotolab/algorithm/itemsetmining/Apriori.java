package com.github.kusumotolab.algorithm.itemsetmining;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Apriori<Item> implements ItemSetMining<Item> {

  private final Observer<Item> observer;

  public Apriori() {
    this(new Observer<>());
  }

  public Apriori(final Observer<Item> observer) {
    this.observer = observer;
  }

  @Override
  public Set<ItemSet<Item>> execute(final Set<Set<Item>> transactions, final int theta) {
    observer.start(transactions, theta);
    final Set<ItemSet<Item>> itemSets = new HashSet<>();

    int k = 0;
    observer.beginCalculatingNextDk(k, itemSets);
    Set<ItemSet<Item>> dk = extractD1(transactions, theta);
    observer.endCalculatingNextDk(k, itemSets, dk);
    itemSets.addAll(dk);

    while (!dk.isEmpty()) {
      k += 1;
      observer.beginCalculatingNextDk(k, dk);
      final Set<ItemSet<Item>> nextDk = extractNextDk(transactions, dk, k, theta);
      observer.endCalculatingNextDk(k, dk, nextDk);
      dk = nextDk;
      itemSets.addAll(nextDk);
    }
    observer.end(itemSets);
    return itemSets;
  }

  private Set<ItemSet<Item>> extractD1(final Set<Set<Item>> transactions, final int theta) {
    final Map<Item, ItemSet<Item>> itemMap = new HashMap<>();
    final List<Item> items = transactions.stream()
        .map(Collection::stream)
        .flatMap(Stream::distinct)
        .collect(Collectors.toList());

    for (final Item item : items) {
      final ItemSet<Item> itemSet = itemMap.computeIfAbsent(item, this::createItemSet);
      itemMap.put(item, itemSet);
    }
    return convertAndFilter(transactions, itemMap.values(), theta);
  }

  private Set<ItemSet<Item>> extractNextDk(final Set<Set<Item>> transactions,
      final Set<ItemSet<Item>> dk, final int k, final int theta) {
    final List<ItemSet<Item>> itemSets = new ArrayList<>(dk);
    final Map<ItemSet<Item>, ItemSet<Item>> results = new HashMap<>();

    for (int i = 0; i < itemSets.size() - 1; i++) {
      for (int j = i + 1; j < itemSets.size(); j++) {
        final ItemSet<Item> newItemSet = new ItemSet<>();
        newItemSet.addAll(itemSets.get(i));
        newItemSet.addAll(itemSets.get(j));
        if (newItemSet.size() != k + 1) {
          break;
        }
        final ItemSet<Item> itemSet = results.computeIfAbsent(newItemSet, e -> e);
        results.put(itemSet, itemSet);
      }
    }
    return convertAndFilter(transactions, results.values(), theta);
  }

  @SafeVarargs
  private final ItemSet<Item> createItemSet(final Item... items) {
    final ItemSet<Item> itemSet = new ItemSet<>();
    itemSet.addAll(Arrays.asList(items));
    return itemSet;
  }

  private Set<ItemSet<Item>> convertAndFilter(final Set<Set<Item>> transactions, final Collection<ItemSet<Item>> collections, final int theta) {
    final Set<ItemSet<Item>> items = new HashSet<>();
    for (final ItemSet<Item> itemSet : collections) {
      final long count = transactions.stream()
          .filter(e -> e.containsAll(itemSet))
          .count();
      itemSet.setCounter(((int) count));
      if (itemSet.count() >= theta) {
        items.add(itemSet);
      }
    }
    return items;
  }

  public static class Observer<Item> {

    public void start(final Set<Set<Item>> transactions, final int theta) {
    }

    public void end(final Set<ItemSet<Item>> output) {
    }

    public void beginCalculatingNextDk(final int k, final Set<ItemSet<Item>> dk) {
    }

    public void endCalculatingNextDk(final int k, final Set<ItemSet<Item>> dk, final Set<ItemSet<Item>> nextDk) {
    }
  }
}
