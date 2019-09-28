package com.github.kusumotolab.algorithm.itemsetmining;

import java.util.Set;

public interface ItemSetMining<Item> {

  Set<ItemSet<Item>> execute(final Set<Set<Item>> transactions, final int theta);
}
