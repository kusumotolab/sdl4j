package com.github.kusumotolab.algorithm.mining.sequential;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.Set;
import org.junit.Test;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class PrefixPatternTest {

  @SuppressWarnings("unchecked")
  private final Set<List<Integer>> transactions = Sets.newHashSet(
      Lists.newArrayList(1, 2, 3, 4, 1, 6, 8),
      Lists.newArrayList(2, 4, 5, 6),
      Lists.newArrayList(3, 1)
  );

  @Test
  public void testExecute() {
    final SequentialPatternMining<Integer> patternMining = new PrefixPattern<>();
    final Set<SequentialPattern<Integer>> patterns = patternMining.execute(transactions, 2);
    assertThat(patterns)
        .contains(new SequentialPattern<>(Lists.newArrayList(1)))
        .contains(new SequentialPattern<>(Lists.newArrayList(2)))
        .contains(new SequentialPattern<>(Lists.newArrayList(3)))
        .contains(new SequentialPattern<>(Lists.newArrayList(4)))
        .contains(new SequentialPattern<>(Lists.newArrayList(6)))
        .contains(new SequentialPattern<>(Lists.newArrayList(2, 4)))
        .contains(new SequentialPattern<>(Lists.newArrayList(2, 6)))
        .contains(new SequentialPattern<>(Lists.newArrayList(4, 6)))
        .contains(new SequentialPattern<>(Lists.newArrayList(3, 1)))
        .contains(new SequentialPattern<>(Lists.newArrayList(2, 4, 6)))
        .hasSize(10);
  }
}