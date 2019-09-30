package com.github.kusumotolab.sdl4j.algorithm.mining.itemset;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import com.google.common.collect.Lists;

public class Backtrack<Item> implements ItemSetMining<Item> {

  private final BacktrackObserver<Item> observer;

  public Backtrack() {
    this(new BacktrackObserver<>());
  }

  public Backtrack(final BacktrackObserver<Item> observer) {
    this.observer = observer;
  }

  @Override
  public Set<ItemSet<Item>> execute(final Set<Set<Item>> transactions, final int theta) {
    observer.start(transactions, theta);

    final Set<ItemSet<Item>> outputs = new HashSet<>();
    final List<Item> elements = extractElements(transactions, theta);
    backtrack(Lists.newArrayList(), elements, 0, theta, transactions, outputs);

    observer.end(outputs);
    return outputs;
  }

  private void backtrack(final List<Item> x, final List<Item> elements, final int index,
      final int theta, final Set<Set<Item>> transactions, final Set<ItemSet<Item>> output) {
    final ItemSet<Item> itemSet = new ItemSet<>(x);

    for (int i = index; i < elements.size(); i++) {
      final List<Item> newItemList = Lists.newArrayList(x);
      newItemList.add(elements.get(i));

      final ItemSet<Item> newItemSet = new ItemSet<>(newItemList);
      final int count = countContainedTransactions(newItemSet, transactions);
      if (count >= theta) {
        output.add(newItemSet);
        observer.foundNewItemSet(newItemSet, itemSet, output);
        backtrack(newItemList, elements, i + 1, theta, transactions, output);
      }
    }
  }

  private List<Item> extractElements(final Set<Set<Item>> transactions, final int theta) {
    return transactions.stream()
        .flatMap(Collection::stream)
        .sorted()
        .distinct()
        .filter(e -> countContainedTransactions(new ItemSet<>(e), transactions) >= theta)
        .collect(Collectors.toList());
  }

  public static class BacktrackObserver<Item> extends Observer<Item> {

    public void foundNewItemSet(final ItemSet<Item> newItemSet, final ItemSet<Item> fromItemSet,
        final Set<ItemSet<Item>> output) {
    }
  }
}
