package com.workday.treap;

public class Node {
    private short id;
    private long data;

    private Node left;
    private Node right;

    public Node(short id, long data) {
        this.id = id;
        this.data = data;
    }

    public short getId() {
        return id;
    }

    public long getData() {
        return data;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }
}
