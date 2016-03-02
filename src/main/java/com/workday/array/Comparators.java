package com.workday.array;

import java.util.Comparator;

public abstract class Comparators {
    private static final Comparator<RangeData> DATA_COMPARATOR = new DataComparator();
    private static final Comparator<RangeData> ID_COMPARATOR = new IdComparator();

    static class DataComparator implements Comparator<RangeData> {
        @Override
        public int compare(RangeData o1, RangeData o2) {
            return Long.compare(o1.getData(), o2.getData());
        }
    }

    static class IdComparator implements Comparator<RangeData> {
        @Override
        public int compare(RangeData o1, RangeData o2) {
            return Short.compare(o1.getId(), o2.getId());
        }
    }

    public static Comparator<RangeData> dataComparator() {
        return DATA_COMPARATOR;
    }

    public static Comparator<RangeData> idComparator() {
        return ID_COMPARATOR;
    }

}
