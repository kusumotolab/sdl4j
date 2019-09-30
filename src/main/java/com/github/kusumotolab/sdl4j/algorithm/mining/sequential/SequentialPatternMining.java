package com.github.kusumotolab.sdl4j.algorithm.mining.sequential;

import java.util.List;
import java.util.Set;

public interface SequentialPatternMining<Item> {

  Set<SequentialPattern<Item>> execute(final Set<List<Item>> transactions, final int theta);
}
