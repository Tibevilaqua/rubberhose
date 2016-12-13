package com.rubberhose.infrastructure;

/**
 * Created by root on 13/12/16.
 */
public enum LaneEnum {

    NORTHBOUND("A"),
    SOUTHBOUND("B");

    private String value;

    LaneEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
