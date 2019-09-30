package com.github.kusumotolab.algorithm.mining.itemset;

import java.util.Set;

public interface ItemSetMining<Item> {

  Set<ItemSet<Item>> execute(final Set<Set<Item>> transactions, final int theta);

  default int countContainedTransactions(final ItemSet<Item> itemSet,
      final Set<Set<Item>> transactions) {
    return (int) transactions.parallelStream()
        .filter(e -> e.containsAll(itemSet))
        .count();
  }

  class Observer<Item> {

    public void start(final Set<Set<Item>> transactions, final int theta) {
    }

    public void end(final Set<ItemSet<Item>> output) {
    }
  }
}
