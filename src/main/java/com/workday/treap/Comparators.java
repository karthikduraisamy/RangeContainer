package com.workday.treap;

import java.util.Comparator;

public abstract class Comparators {
    private static final Comparator<Node> NODE_COMPARATOR = new NodeComparator();

    static class NodeComparator implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return Long.compare(o1.getId(), o2.getId());
        }
    }

    public static Comparator<Node> nodeComparator() {
        return NODE_COMPARATOR;
    }
}
