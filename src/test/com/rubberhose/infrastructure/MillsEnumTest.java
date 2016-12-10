package com.rubberhose.infrastructure;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by root on 10/12/16.
 */
public class MillsEnumTest {

    @Test
    public void valuesShouldBeMultiple_when_requested(){
        Assert.assertEquals(MillsEnum.FIFTEEN_MINUTES.value(), 1000 * 60 * 15);
        Assert.assertEquals(MillsEnum.TWENTY_MINUTES.value(), 1000 * 60 * 20);
        Assert.assertEquals(MillsEnum.THIRTY_MINUTES.value(), 1000 * 60 * 30);
        Assert.assertEquals(MillsEnum.ONE_HOUR.value(), 1000 * 60 * 60);
        Assert.assertEquals(MillsEnum.END_OF_MORNING.value(), (1000 * 60 * 60 * 24) / 2);
    }


}
