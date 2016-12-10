package com.rubberhose.infrastructure;

/**
 * Created by root on 10/12/16.
 */
public enum OccurrencePerDayEnum {

    HOURLY(24,MillsEnum.ONE_HOUR)
    , EVERY_THIRTY_MINUTES(HOURLY.value * 2,MillsEnum.THIRTY_MINUTES)
    , EVERY_TWENTY_MINUTES(HOURLY.value * 3,MillsEnum.TWENTY_MINUTES)
    , EVERY_FIFTEEN_MINUTES(EVERY_THIRTY_MINUTES.value * 2,MillsEnum.FIFTEEN_MINUTES);

    private int value;
    private MillsEnum millsEnum;


    OccurrencePerDayEnum(int value, MillsEnum millsEnum) {
        this.value = value;
        this.millsEnum = millsEnum;
    }

    public int getValue() {
        return value;
    }

    public MillsEnum getRespectiveMillsEnum() {
        return millsEnum;
    }
}
