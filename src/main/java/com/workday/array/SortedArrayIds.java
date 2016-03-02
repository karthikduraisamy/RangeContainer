package com.workday.array;

import com.workday.Ids;

import java.util.Arrays;

public class SortedArrayIds implements Ids {
    private final RangeData[] results;
    private int index;

    public SortedArrayIds(RangeData[] results) {
        this.results = results;
        index = 0;

        // O(k.log(k)) complexity to sort query results by id
        Arrays.sort(results, Comparators.idComparator());
    }

    @Override
    public short nextId() {
        return index < results.length ? results[index++].getId() : END_OF_IDS;
    }
}
