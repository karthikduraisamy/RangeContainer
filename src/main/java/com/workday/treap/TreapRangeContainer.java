package com.workday.treap;

import com.workday.Ids;
import com.workday.RangeContainer;

public class TreapRangeContainer implements RangeContainer {
    // uses a Treap (a binary search tree in which nodes are inserted according to id sort order)
    // https://en.wikipedia.org/wiki/Treap

    // to find the range,
    // (1) looks up the start and end of the range in the BST
    // (2) defines a min heap based on node id
    // (3) places both ends of the range and their common ancestor in the min heap
    // (4) serves the heap minimum when Ids.nextId() is called and then adds node's children to heap
    // (5) if no new node can be added (because it's not in the range) then return -1
    //
    // (1) space: allocates only a heap as extra memory (produces O(k) garbage per query where k is result size)
    // (2) time: does not require sorting so skips upfront k.log(k) required for sorting,
    //           still requires k.log(k) for lazily inserting k elements into min heap
    // (3) worst case: O(N + N.log(N)) time + O(N) space

    private Treap treap;

    public TreapRangeContainer(Treap treap) {
        this.treap = treap;
    }

    @Override
    public Ids findIdsInRange(long fromValue, long toValue, boolean fromInclusive, boolean toInclusive) {
        return treap.find(fromValue, toValue, fromInclusive, toInclusive);
    }
}
