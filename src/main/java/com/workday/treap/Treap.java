package com.workday.treap;

import java.util.PriorityQueue;

public class Treap {
    private Node root;

    static class InOrderNeighbors {
        Node successor;
        Node predecessor;
    }

    static class Range {
        private long fromValue;
        private long toValue;
        private boolean fromInclusive;
        private boolean toInclusive;
        private PriorityQueue<Node> queue;

        public Range(long fromValue, long toValue, boolean fromInclusive, boolean toInclusive) {
            this.fromValue = fromValue;
            this.toValue = toValue;
            this.fromInclusive = fromInclusive;
            this.toInclusive = toInclusive;
            queue = new PriorityQueue<>(Comparators.nodeComparator());
        }

        public boolean offer(Node node) {
            if (node != null && inRange(node) && !queue.contains(node)) {
                queue.add(node);
                return true;
            }
            return false;
        }

        public Node poll() {
            return queue.poll();
        }

        public boolean isEmpty() {
            return queue.isEmpty();
        }

        public boolean inRange(Node node) {
            return ((node.getData() > fromValue) || (fromInclusive && node.getData() == fromValue)) &&
                    ((node.getData() < toValue) || (toInclusive && node.getData() == toValue));
        }
    }

    public void insert(Node node) {
        if (root == null) {
            root = node;
        } else {
            insert(root, node);
        }
    }

    void insert(Node node, Node newNode) {
        if (newNode.getData() < node.getData()) {
            if (node.getLeft() == null) {
                // insert here
                node.setLeft(newNode);
            } else {
                // insert in the left sub-tree
                insert(node.getLeft(), newNode);
            }
        } else {
            if (node.getRight() == null) {
                // insert here
                node.setRight(newNode);
            } else {
                // insert in the right sub-tree
                insert(node.getRight(), newNode);
            }
        }
    }

    void notEmptyCheck() {
        if (root == null) throw new IllegalStateException("Treap is empty.");
    }

    public Node find(long data) {
        notEmptyCheck();
        return find(root, data, new InOrderNeighbors());
    }

    Node find(Node node, long data, InOrderNeighbors neighbors) {
        // return the first occurrence of data in the treap
        if (node.getData() == data) {
            Node walker = node;
            if (walker.getLeft() != null) {
                // the right most element in the left sub-tree will be the predecessor
                walker = walker.getLeft();
                while (walker.getRight() != null) walker = walker.getRight();
                neighbors.predecessor = walker;
            }
            if (node.getRight() != null) {
                // the left most element in the right sub-tree the successor
                walker = node.getRight();
                while (walker.getLeft() != null) walker = walker.getLeft();
                // skip over duplicates
                while (walker.getRight() != null && walker.getData() == node.getData()) walker = walker.getRight();
                // only if it is not a duplicate of node
                if (walker.getData() != node.getData()) neighbors.successor = walker;
            }
            return node;
        }

        if (data < node.getData()) {
            // if node.getLeft() has no right sub-tree then it's parent is the successor
            neighbors.successor = node;
            // if data is not in the treap return the first node larger than data
            return node.getLeft() == null ? node : find(node.getLeft(), data, neighbors);
        } else {
            // if node.getRight() has no left sub-tree then it's parent is the predecessor
            neighbors.predecessor = node;
            // if data is not in the treap return the first node smaller that data
            return node.getRight() == null ? node : find(node.getRight(), data, neighbors);
        }
    }

    public TreapIds find(long fromValue, long toValue, boolean fromInclusive, boolean toInclusive) {
        notEmptyCheck();

        // find the node with smallest id in the query range (fromValue, toValue)
        Node ancestor = firstCommonAncestor(root, fromValue, toValue);

        // find fromValue or the closest value to it in the treap
        InOrderNeighbors fromNeighbors = new InOrderNeighbors();
        Node fromNode = find(root, fromValue, fromNeighbors);

        // find toValue or the closest value to it in the treap
        InOrderNeighbors toNeighbors = new InOrderNeighbors();
        Node toNode = find(root, toValue, toNeighbors);

        Range range = new Range(fromValue, toValue, fromInclusive, toInclusive);

        // if either fromNode or toNode is the first common ancestor no need to include it
        if (ancestor != fromNode && ancestor != toNode) range.offer(ancestor);

        // adjust fromNode and toNode if query range is not inclusive
        if (!fromInclusive && fromNode.getData() == fromValue) fromNode = fromNeighbors.successor;
        if (!toInclusive && toNode.getData() == toValue) toNode = toNeighbors.predecessor;

        // add fromNode
        range.offer(fromNode);
        // add toNode if it's not the same as fromNode
        if (toNode != fromNode) range.offer(toNode);

        return new TreapIds(range);
    }

    Node firstCommonAncestor(Node node, long fromValue, long toValue) {
        if (fromValue < node.getData() && toValue < node.getData()) {
            return node.getLeft() != null ? firstCommonAncestor(node.getLeft(), fromValue, toValue) : node;
        } else if (fromValue > node.getData() && toValue > node.getData()) {
            return node.getRight() != null ? firstCommonAncestor(node.getRight(), fromValue, toValue) : node;
        } else {
            return node;
        }
    }

}
