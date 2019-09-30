package com.github.kusumotolab.sdl4j.algorithm.mining.itemset;

import org.junit.Test;

public class BacktrackTest extends ItemSetMiningTest {

  @Test
  public void testBacktrack() {
    final Backtrack<Integer> backtrack = new Backtrack<>();
    testItemSetMining(backtrack);
  }
}