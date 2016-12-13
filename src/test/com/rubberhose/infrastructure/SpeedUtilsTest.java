package com.rubberhose.infrastructure;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static com.rubberhose.infrastructure.SpeedUtils.SPEED_LIMIT_IN_MILLS;
import static com.rubberhose.infrastructure.SpeedUtils.getAverageKMBasedOnMills;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by root on 13/12/16.
 */
public class SpeedUtilsTest {

    @Test
    public void shouldReturnMaxLimitInMills_when_limitIs60Km(){
        assertTrue(150 == SpeedUtils.SPEED_LIMIT_IN_MILLS);
    }

    @Test
    public void shouldReturnTheRightSpeed_when_carsAreEitherFasterOrSlower(){
        assertEquals(Integer.valueOf(48), getAverageKMBasedOnMills(10, 300l));
        assertEquals(Integer.valueOf(0), getAverageKMBasedOnMills(10, 1500l));
        assertEquals(Integer.valueOf(72), getAverageKMBasedOnMills(10, -300l));
        assertEquals(Integer.valueOf(120), getAverageKMBasedOnMills(10, -1500l));
        assertEquals(Integer.valueOf(60), getAverageKMBasedOnMills(100, 60l));
    }


    @Test
    public void shouldReturnCorrectSpeed_when_crossesMakeSense(){
        Long differenceInMills = SpeedUtils.getDifferenceInMills(Arrays.asList(100, 260, 300, 460));
        assertEquals(20l, differenceInMills.longValue());
    }


}
