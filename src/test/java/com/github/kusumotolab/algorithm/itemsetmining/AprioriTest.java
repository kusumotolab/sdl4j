package com.github.kusumotolab.algorithm.itemsetmining;

import org.junit.Test;

public class AprioriTest extends ItemSetMiningTest {

  @Test
  public void testApriori() {
    final Apriori<Integer> apriori = new Apriori<>();
    testItemSetMining(apriori);
  }
}