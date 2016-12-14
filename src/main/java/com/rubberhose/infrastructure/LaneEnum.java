package com.rubberhose.infrastructure;

/**
 * Created by root on 13/12/16.
 */
public enum LaneEnum {

    NORTHBOUND("A",2),
    SOUTHBOUND("B",4);

    private String value;
    private Integer crossesPerCar;

    LaneEnum(String value, Integer crossesPerCar) {
        this.value = value;
        this.crossesPerCar = crossesPerCar;
    }

    public String getValue() {
        return value;
    }

    public Integer getCrossesPerCar() {
        return crossesPerCar;
    }
}
