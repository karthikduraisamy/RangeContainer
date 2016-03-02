package com.workday.array;

import com.workday.Ids;
import com.workday.RangeContainer;

import java.util.Arrays;

public class SortedArrayRangeContainer implements RangeContainer {
    private final RangeData[] rangeData;

    public SortedArrayRangeContainer(RangeData[] rangeData) {
        this.rangeData = rangeData;
    }

    @Override
    public Ids findIdsInRange(long fromValue, long toValue, boolean fromInclusive, boolean toInclusive) {
        // O(log(k)) complexity to find the bounds of query result
        int fromIndex = Arrays.binarySearch(rangeData, new RangeData(null, fromValue), Comparators.dataComparator());
        int toIndex = Arrays.binarySearch(rangeData, new RangeData(null, toValue), Comparators.dataComparator());

        RangeData[] result;

        // if there is an overlap between the query range (fromValue, toValue) and the data in the container
        if (fromIndex < rangeData.length && toIndex != -1) {
            // O(k) memory space is allocated per query to hold query results
            result = getSubArray(fromIndex, toIndex, fromInclusive, toInclusive);
        } else {
            // there is no overlap
            result = new RangeData[0];
        }

        return new SortedArrayIds(result);
    }

    RangeData[] getSubArray(int fromIndex, int toIndex, boolean fromInclusive, boolean toInclusive) {
        fromIndex = sanitizeFromIndex(fromIndex, fromInclusive);
        toIndex = sanitizeToIndex(toIndex, toInclusive, rangeData.length - 1);

        return (toIndex < rangeData.length && fromIndex >= 0 && fromIndex <= toIndex) ?
                Arrays.copyOfRange(rangeData, fromIndex, toIndex + 1) :
                new RangeData[0];
    }

    int sanitizeFromIndex(int id, boolean inclusive) {
        // if value is not not found in the array but can be inserted at between elements array[id-1] and array[id],
        // then Arrays.binarySearch() return -(id + 1) as value insertion point
        if (id < 0) return -(id + 1);
        else if (!inclusive) {
            // skip over all fromValue occurrences
            long fromValue = rangeData[id].getData();
            while (id < rangeData.length && rangeData[id].getData() == fromValue) ++id;
            return id;
        } else return id;
    }

    int sanitizeToIndex(int id, boolean inclusive, int max) {
        int index;
        // if value is not not found in the array but can be inserted at between elements array[id-1] and array[id],
        // then Arrays.binarySearch() return -(id + 1) as value insertion point
        if (id < 0) index = -(id + 1);
        else if (!inclusive) {
            // skip over all toValue occurrences
            long toValue = rangeData[id].getData();
            while (id >= 0 && rangeData[id].getData() == toValue) --id;
            index = id;
        } else index = id;
        // if value is not found in the array but is larger than all the elements in the array,
        // then Arrays.binarySearch() returns array.length as value insertion point
        if (index > max) index = max;

        return index;
    }

}
