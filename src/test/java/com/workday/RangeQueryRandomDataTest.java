package com.workday;

import com.workday.array.SortedArrayRangeContainerFactory;
import com.workday.map.NavigableMapRangeContainerFactory;
import com.workday.treap.TreapRangeContainerFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class RangeQueryRandomDataTest {
    private final long[] randomData = generateRandomLongData(NavigableMapRangeContainerFactory.MAX_CONTAINER_SIZE);
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

    public RangeQueryRandomDataTest(RangeContainerFactory rf) {
        this.rf = rf;
    }

    @Before
    public void setUp() {
        rc = rf.createContainer(randomData);
    }

    @Test
    public void findIdsInRange_randomData_pointQueries() {
        int pointIndex = 0;
        for (long point : randomData) {
            Ids iterator = rc.findIdsInRange(point, point, true, true);
            assertEquals(pointIndex++, iterator.nextId());

            assertEquals(Ids.END_OF_IDS, rc.findIdsInRange(point, point, true, false).nextId());
            assertEquals(Ids.END_OF_IDS, rc.findIdsInRange(point, point, false, true).nextId());
            assertEquals(Ids.END_OF_IDS, rc.findIdsInRange(point, point, false, false).nextId());
        }
    }

    @Test
    public void nextId_randomData_fullRangeQuery() {
        int pointIndex = 0;
        short currentId, previousId = Short.MIN_VALUE;

        Ids iterator = rc.findIdsInRange(Long.MIN_VALUE, Long.MAX_VALUE, true, true);

        while ((currentId = iterator.nextId()) != Ids.END_OF_IDS) {
            assertTrue(currentId > previousId);
            previousId = currentId;
            ++pointIndex;
        }

        assertEquals(NavigableMapRangeContainerFactory.MAX_CONTAINER_SIZE, pointIndex);
    }

    private long[] generateRandomLongData(int size) {
        long[] data = new long[size];
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < size; ++i) {
            data[i] = random.nextLong();
        }
        return data;
    }

}
