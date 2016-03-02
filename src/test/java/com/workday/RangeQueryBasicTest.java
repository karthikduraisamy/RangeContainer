package com.workday;

import com.workday.array.SortedArrayRangeContainerFactory;
import com.workday.map.NavigableMapRangeContainerFactory;
import com.workday.treap.TreapRangeContainerFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class RangeQueryBasicTest {
    private final long[] uniqueData = new long[]{10, 12, 17, 21, 2, 15, 16};
    private final long[] containsDuplicateData = new long[]{10, 12, 17, 15, 2, 15, 15};
    private final long[] duplicateData = new long[]{10, 10, 10, 10, 10};

    private RangeContainerFactory rf;
    private RangeContainer rc;

    @Parameterized.Parameters
    public static Iterable<Object[]> getParameters() {
        return Arrays.asList(new Object[][]{
                {new NavigableMapRangeContainerFactory()},
                {new SortedArrayRangeContainerFactory()},
                {new TreapRangeContainerFactory()}
        });
    }

    public RangeQueryBasicTest(RangeContainerFactory rf) {
        this.rf = rf;
    }

    @Before
    public void setUp() {
        rc = rf.createContainer(uniqueData);
    }

    @Test
    public void runARangeQuery() {
        Ids ids = rc.findIdsInRange(14, 17, true, true);

        assertEquals(2, ids.nextId());
        assertEquals(5, ids.nextId());
        assertEquals(6, ids.nextId());
        assertEquals(Ids.END_OF_IDS, ids.nextId());

        ids = rc.findIdsInRange(14, 17, true, false);

        assertEquals(5, ids.nextId());
        assertEquals(6, ids.nextId());
        assertEquals(Ids.END_OF_IDS, ids.nextId());

        ids = rc.findIdsInRange(20, Long.MAX_VALUE, false, true);

        assertEquals(3, ids.nextId());
        assertEquals(Ids.END_OF_IDS, ids.nextId());
    }

    @Test
    public void nextId_outsideRange_returnsNothing() {
        Ids ids = rc.findIdsInRange(Long.MIN_VALUE, 0, true, true);
        assertEquals(Ids.END_OF_IDS, ids.nextId());

        ids = rc.findIdsInRange(Long.MIN_VALUE, 2, true, false);
        assertEquals(Ids.END_OF_IDS, ids.nextId());

        ids = rc.findIdsInRange(25, Long.MAX_VALUE, true, true);
        assertEquals(Ids.END_OF_IDS, ids.nextId());

        ids = rc.findIdsInRange(21, Long.MAX_VALUE, false, true);
        assertEquals(Ids.END_OF_IDS, ids.nextId());
    }

    @Test
    public void nextId_singlePointQuery_returnsSinglePoint() {
        Ids ids = rc.findIdsInRange(17, 17, true, true);

        assertEquals(2, ids.nextId());
        assertEquals(Ids.END_OF_IDS, ids.nextId());
    }

    @Test
    public void nextId_singlePointQuery_returnsNothing() {
        Ids ids = rc.findIdsInRange(17, 17, false, true);
        assertEquals(Ids.END_OF_IDS, ids.nextId());

        ids = rc.findIdsInRange(17, 17, true, false);
        assertEquals(Ids.END_OF_IDS, ids.nextId());

        ids = rc.findIdsInRange(17, 17, false, false);
        assertEquals(Ids.END_OF_IDS, ids.nextId());
    }

    @Test
    public void nextId_emptyRange_returnsNothing() {
        Ids ids = rc.findIdsInRange(30, 40, true, true);

        assertEquals(Ids.END_OF_IDS, ids.nextId());
    }

    @Test
    public void nextId_hasDuplicates_includesDuplicates() {
        RangeContainer rc = rf.createContainer(containsDuplicateData);
        Ids ids = rc.findIdsInRange(14, 17, true, true);

        assertEquals(2, ids.nextId());
        assertEquals(3, ids.nextId());
        assertEquals(5, ids.nextId());
        assertEquals(6, ids.nextId());
        assertEquals(Ids.END_OF_IDS, ids.nextId());
    }

    @Test
    public void nextId_hasDuplicates_excludesDuplicates() {
        RangeContainer rc = rf.createContainer(containsDuplicateData);
        Ids ids = rc.findIdsInRange(15, 17, false, true);

        assertEquals(2, ids.nextId());
        assertEquals(Ids.END_OF_IDS, ids.nextId());
    }

    @Test
    public void nextId_allDuplicates_includesDuplicates() {
        RangeContainer rc = rf.createContainer(duplicateData);
        Ids ids = rc.findIdsInRange(5, 15, false, false);

        assertEquals(0, ids.nextId());
        assertEquals(1, ids.nextId());
        assertEquals(2, ids.nextId());
        assertEquals(3, ids.nextId());
        assertEquals(4, ids.nextId());
        assertEquals(Ids.END_OF_IDS, ids.nextId());
    }

    @Test
    public void nextId_allDuplicates_excludesDuplicates() {
        RangeContainer rc = rf.createContainer(duplicateData);
        Ids ids = rc.findIdsInRange(10, 15, false, false);

        assertEquals(Ids.END_OF_IDS, ids.nextId());
    }

}