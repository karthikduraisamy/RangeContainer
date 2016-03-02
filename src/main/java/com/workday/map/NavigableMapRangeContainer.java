package com.workday.map;

import com.workday.Ids;
import com.workday.RangeContainer;

import java.util.Collection;
import java.util.HashSet;
import java.util.NavigableMap;
import java.util.Set;

public class NavigableMapRangeContainer implements RangeContainer {

    private NavigableMap<Long, Set<Short>> dataToIdMap;

    public NavigableMapRangeContainer(NavigableMap<Long, Set<Short>> map) {
        dataToIdMap = map;
    }

    public Ids findIdsInRange(long fromValue, long toValue, boolean fromInclusive, boolean toInclusive) {
        // TreeMap.subMap() operation has O(1) time complexity
        // O(k) memory for rangeMap will be garbage collected
        NavigableMap<Long, Set<Short>> rangeMap = dataToIdMap.subMap(fromValue, fromInclusive, toValue, toInclusive);

        // O(k) memory allocated for collections ids and flatIds will be garbage collected
        Collection<Set<Short>> ids = rangeMap.values();
        Set<Short> flatIds = flattenMultiSet(ids);

        return new NavigableMapIds(flatIds);
    }

    public static <T> Set<T> flattenMultiSet(Collection<Set<T>> multiSet) {
        Set<T> flatSet = new HashSet<>(multiSet.size());
        multiSet.forEach(flatSet::addAll);
        return flatSet;
    }
}
