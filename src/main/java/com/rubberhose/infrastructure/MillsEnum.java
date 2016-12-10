package com.rubberhose.infrastructure;

/**
 * Created by root on 10/12/16.
 */
public enum MillsEnum {

    FIFTEEN_MINUTES(900000),
    TWENTY_MINUTES(1200000),
    THIRTY_MINUTES(FIFTEEN_MINUTES.mills * 2),
    ONE_HOUR(THIRTY_MINUTES.mills * 2),
    END_OF_MORNING(43200000);

    int mills;

    public int value() {
        return mills;
    }

    MillsEnum(int mills) {
        this.mills = mills;
    }


}
