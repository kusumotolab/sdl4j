package com.github.kusumotolab.sdl4j.algorithm.mining.tree;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Test;
import com.google.common.collect.Lists;

public class NodeTest {

  private final List<Label<Integer>> labels = Lists.newArrayList(
      new Label<>(0, 0),
      new Label<>(1, 1),
      new Label<>(2, 2),
      new Label<>(2, 2),
      new Label<>(3, 3),
      new Label<>(3, 4),
      new Label<>(1, 1),
      new Label<>(2, 2),
      new Label<>(2, 5),
      new Label<>(3, 4)
  );

  @Test
  public void testTreeConstruct() {
    final Node<Integer> node = Node.createTree("tree1", labels);
    final List<Label<Integer>> labels = node.getLabels();
    assertThat(labels).hasSize(10);
  }

  @Test
  public void testRightMostBranch() {
    final Node<Integer> root = Node.createTree("tree1", labels);
    final List<Node<Integer>> rightMostBranch = root.getRightMostBranch();
    final List<Integer> rightMostBranchLabel = rightMostBranch.stream()
        .map(Node::getLabel)
        .collect(Collectors.toList());
    final List<Integer> expected = Lists.newArrayList(0, 1, 5, 4);

    assertThat(rightMostBranchLabel).isEqualTo(expected);
  }
}