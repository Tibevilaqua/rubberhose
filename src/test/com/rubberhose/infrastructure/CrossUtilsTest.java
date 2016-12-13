package com.rubberhose.infrastructure;

import com.rubberhose.infrastructure.utils.CrossUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by root on 10/12/16.
 */
public class CrossUtilsTest {


    @Test
    public void shouldReturnOnlySortedMillsFromCrosses_when_itIsExceptedAndOrSentInAnyOrder(){
        List<Integer> expectedResult = Arrays.asList(1,3434,222222);

        List<Integer> result = CrossUtils.getMillsFrom(Arrays.asList("A222222", "B3434", "C1"));
        Assert.assertEquals(expectedResult,result);
    }


    @Test
    public void shouldReturnOnlySortedMillsFromCrossesForSpecificLane_when_itIsExceptedAndOrSentInAnyOrder(){


        List<Integer> result = CrossUtils.getMillsFrom(Arrays.asList("B222222", "A3434", "C1"), LaneEnum.NORTHBOUND);
        Assert.assertEquals( Arrays.asList(3434),result);

        List<Integer> resultSouthBound = CrossUtils.getMillsFrom(Arrays.asList("B222222", "A3434", "C1"), LaneEnum.SOUTHBOUND);

        Assert.assertEquals( Arrays.asList(222222),resultSouthBound);


    }


}
