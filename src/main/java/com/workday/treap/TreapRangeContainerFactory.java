package com.workday.treap;

import com.workday.RangeContainer;
import com.workday.RangeContainerFactory;

public class TreapRangeContainerFactory implements RangeContainerFactory {
    @Override
    public RangeContainer createContainer(long[] data) {
        return new TreapRangeContainer(createTreap(data));
    }

    Treap createTreap(long[] data) {
        Treap treap = new Treap();

        // insert data into BST while preserving id order
        short id = 0;
        for (long point : data) {
            treap.insert(new Node(id++, point));
        }

        return treap;
    }
}
