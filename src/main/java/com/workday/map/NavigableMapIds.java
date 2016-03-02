package com.workday.map;

import com.workday.Ids;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

public class NavigableMapIds implements Ids {
    private Iterator<Short> iterator;

    public NavigableMapIds(Collection<Short> ids) {
        // inserting k ids into TreeSet is O(k.log(k))
        // O(k) memory for TreeSet will be garbage collected
        // both time/space has to be allocated even if the client never invokes nextId() on the query results
        iterator = new TreeSet<>(ids).iterator();
    }

    public short nextId() {
        return iterator.hasNext() ? iterator.next() : END_OF_IDS;
    }
}
