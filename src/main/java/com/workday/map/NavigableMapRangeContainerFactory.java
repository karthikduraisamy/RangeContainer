package com.workday.map;

import com.workday.RangeContainer;
import com.workday.RangeContainerFactory;

import java.util.HashSet;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

public class NavigableMapRangeContainerFactory implements RangeContainerFactory {

    public static final int MAX_CONTAINER_SIZE = 32 * 1024;

    public RangeContainer createContainer(long[] data) {
        assert data.length <= MAX_CONTAINER_SIZE : "data size exceeds 32K limit: " + data.length;

        // TreeSet setup requires O(N.log(N)) time and O(N) memory
        NavigableMap<Long, Set<Short>> dataToIdMap = new TreeMap<>();

        short id = 0;
        for (long point : data) {
            if (dataToIdMap.containsKey(point)) {
                dataToIdMap.get(point).add(id++);
            } else {
                Set<Short> idSet = new HashSet<>();
                idSet.add(id++);
                dataToIdMap.put(point, idSet);
            }
        }

        return new NavigableMapRangeContainer(dataToIdMap);
    }
}
