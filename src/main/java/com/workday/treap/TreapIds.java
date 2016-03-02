package com.workday.treap;

import com.workday.Ids;

/*
    a lazy tree-based iterator that walks the treap only if nextId() is actually invoked
    lazily allocates O(k) memory for the queue holding treap nodes where k is the query result size
    that will later will be garbage collected
 */
public class TreapIds implements Ids {
    private Treap.Range range;

    public TreapIds(Treap.Range range) {
        this.range = range;
    }

    @Override
    public short nextId() {
        if (range.isEmpty()) {
            return END_OF_IDS;
        } else {
            Node next = range.poll();

            // consider left and right children if they are in query range
            range.offer(next.getLeft());
            range.offer(next.getRight());

            return next.getId();
        }
    }

}
