package com.github.kusumotolab.algorithm.mining.sequential;

import java.util.List;
import java.util.Set;

public interface SequentialPatternMining<Item> {

  Set<SequentialPattern<Item>> execute(final Set<List<Item>> transactions, final int theta);
}
