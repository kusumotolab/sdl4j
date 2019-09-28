package com.github.kusumotolab.algorithm.itemsetmining;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Set;
import org.junit.Test;
import com.google.common.collect.Sets;

public class AprioriTest {

  final Set<Set<Integer>> transactions = Sets.newHashSet(
      Sets.newHashSet(1, 2, 5, 6, 7, 9),
      Sets.newHashSet(2, 3, 4, 5),
      Sets.newHashSet(1, 2, 7, 8, 9),
      Sets.newHashSet(1, 7, 9),
      Sets.newHashSet(2, 3, 7, 9)
  );

  @Test
  public void testApriori() {
    final ItemSetMining<Integer> itemSetMining = new Apriori<>();
    final Set<ItemSet<Integer>> itemSets = itemSetMining.execute(transactions, 3);

    assertThat(itemSets).hasSize(11)
        .contains(new ItemSet<>(Sets.newHashSet(1)))
        .contains(new ItemSet<>(Sets.newHashSet(2)))
        .contains(new ItemSet<>(Sets.newHashSet(7)))
        .contains(new ItemSet<>(Sets.newHashSet(9)))
        .contains(new ItemSet<>(Sets.newHashSet(1, 7)))
        .contains(new ItemSet<>(Sets.newHashSet(1, 9)))
        .contains(new ItemSet<>(Sets.newHashSet(2, 7)))
        .contains(new ItemSet<>(Sets.newHashSet(2, 9)))
        .contains(new ItemSet<>(Sets.newHashSet(7, 9)))
        .contains(new ItemSet<>(Sets.newHashSet(1, 7, 9)))
        .contains(new ItemSet<>(Sets.newHashSet(2, 7, 9)));
  }
}