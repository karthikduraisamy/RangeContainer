package com.workday.array;

public class RangeData {
    private Short id;
    private long data;

    public RangeData(Short id, long data) {
        this.id = id;
        this.data = data;
    }

    public short getId() {
        return id;
    }

    public long getData() {
        return data;
    }
}
