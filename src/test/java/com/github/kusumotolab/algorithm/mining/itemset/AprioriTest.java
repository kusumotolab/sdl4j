package com.github.kusumotolab.algorithm.mining.itemset;

import org.junit.Test;

public class AprioriTest extends ItemSetMiningTest {

  @Test
  public void testApriori() {
    final Apriori<Integer> apriori = new Apriori<>();
    testItemSetMining(apriori);
  }
}