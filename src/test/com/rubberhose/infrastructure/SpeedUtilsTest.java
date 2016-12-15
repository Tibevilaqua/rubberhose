package com.rubberhose.infrastructure;

import com.rubberhose.infrastructure.utils.SpeedUtils;
import org.junit.Test;

import java.util.Arrays;

import static com.rubberhose.infrastructure.utils.SpeedUtils.getAverageKMBasedOnMills;
import static com.rubberhose.infrastructure.utils.SpeedUtils.getDifferenceInMills;
import static com.rubberhose.infrastructure.utils.SpeedUtils.getDistanceInMills;
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
    }


    @Test
    public void shouldReturnCorrectSpeed_when_crossesMakeSense(){
        Long differenceInMills = getDifferenceInMills(Arrays.asList(100, 260, 300, 460),LaneEnum.SOUTHBOUND);
        assertEquals(100l, differenceInMills.longValue());
    }

    @Test
    public void shouldReturRightDistance_when_tested(){

        Long distanceInMillsNorthLane = getDistanceInMills(Arrays.asList(900000,900100,900300,900400,900500,900600), LaneEnum.NORTHBOUND);

        assertEquals(5, distanceInMillsNorthLane.longValue());


        Long distanceInMillsSouthLane = getDistanceInMills(Arrays.asList(900000,900100,900300,900400,900500,900600,900700,900800,901000,901100,901200,901300), LaneEnum.SOUTHBOUND);
        assertEquals(5, distanceInMillsSouthLane.longValue());

    }


}
