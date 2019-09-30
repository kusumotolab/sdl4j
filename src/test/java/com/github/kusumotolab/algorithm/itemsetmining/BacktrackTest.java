package com.github.kusumotolab.algorithm.itemsetmining;

import org.junit.Test;

public class BacktrackTest extends ItemSetMiningTest {

  @Test
  public void testBacktrack() {
    final Backtrack<Integer> backtrack = new Backtrack<>();
    testItemSetMining(backtrack);
  }
}