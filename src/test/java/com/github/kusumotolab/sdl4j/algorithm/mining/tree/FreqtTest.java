package com.github.kusumotolab.sdl4j.algorithm.mining.tree;

import java.util.List;
import org.junit.Test;
import com.google.common.collect.Lists;

public class FreqtTest {
  private final List<Label<Integer>> labels = Lists.newArrayList(
      new Label<>(0, 0), // 1
      new Label<>(1, 1), // 2
      new Label<>(2, 2), // 3
      new Label<>(2, 3), // 4
      new Label<>(3, 4), // 5
      new Label<>(1, 0), // 6
      new Label<>(2, 1), // 7
      new Label<>(3, 2), // 8
      new Label<>(3, 3), // 9
      new Label<>(4, 4), // 10
      new Label<>(4, 5), // 11
      new Label<>(2, 1), // 12
      new Label<>(3, 2), // 13
      new Label<>(3, 3), // 14
      new Label<>(4, 4) // 15
  );

  @Test
  public void testMining() {
    final Node<Integer> rootNode = Node.createTree(labels);
    final Freqt<Integer> freqt = new Freqt<>();
    freqt.mining()
  }
}