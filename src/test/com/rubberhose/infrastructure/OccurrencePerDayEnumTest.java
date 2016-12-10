package com.rubberhose.infrastructure;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by root on 10/12/16.
 */
public class OccurrencePerDayEnumTest {

    @Test
    public void valuesShouldBeMultiple_when_requested(){
        Assert.assertEquals(OccurrencePerDayEnum.HOURLY.getValue(), 24);
        Assert.assertEquals(OccurrencePerDayEnum.EVERY_THIRTY_MINUTES.getValue(), 48);
        Assert.assertEquals(OccurrencePerDayEnum.EVERY_TWENTY_MINUTES.getValue(), 72);
        Assert.assertEquals(OccurrencePerDayEnum.EVERY_FIFTEEN_MINUTES.getValue(), 96);
    }

}
