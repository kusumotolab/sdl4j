package com.github.kusumotolab.sdl4j.algorithm.mining.tree;

import java.util.Set;

public interface SubtreeMining<T> {

  Set<TreePattern<T>> mining(final Set<Node<T>> rootNodes, final double minimumSupport);
}
