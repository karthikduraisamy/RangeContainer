package com.workday.array;

import com.workday.RangeContainer;
import com.workday.RangeContainerFactory;

import java.util.Arrays;

public class SortedArrayRangeContainerFactory implements RangeContainerFactory {
    @Override
    public RangeContainer createContainer(long[] data) {
        // wrap long data into RangeData objects
        // requires O(N) additional memory for the new array (per container)
        RangeData[] rangeData = getRangeDataFrom(data);
        // sort based on data values (to facilitate binary search)
        // O(N.log(N)) time to sort N data points
        Arrays.sort(rangeData, Comparators.dataComparator());

        return new SortedArrayRangeContainer(rangeData);
    }

    private RangeData[] getRangeDataFrom(long[] data) {
        RangeData[] rangeData = new RangeData[data.length];
        short id = 0;
        for (long point : data) {
            rangeData[id] = new RangeData(id, point);
            ++id;
        }
        return rangeData;
    }
}
