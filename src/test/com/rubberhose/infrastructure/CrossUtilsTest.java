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
    public void shouldReturnDifferenceInMills_when_crossesFollowTheCorrectPatter(){

        // A B A B (rest is ignored)
        List<Integer> correctSouthPattern = CrossUtils.getMillsFrom(Arrays.asList("A900000", "B900100", "A900200","B900300","A900400","A900500","B900600","B900700","A1000000", "B1000100", "A1000200","B1000300","A1010101010","A1010101210"),LaneEnum.SOUTHBOUND);
        Assert.assertEquals( Arrays.asList(900000,900100,900200,900300,1000000,1000100,1000200,1000300),correctSouthPattern);

        List<Integer> wrongSouthPattern = CrossUtils.getMillsFrom(Arrays.asList("B222222", "A3434", "C1", "A900400","A900500"),LaneEnum.SOUTHBOUND);
        Assert.assertEquals( Collections.EMPTY_LIST,wrongSouthPattern);

        List<Integer> correctNorthPattern = CrossUtils.getMillsFrom(Arrays.asList("A900000", "B900100", "A900200","B900300","A900400","A900500","B900600","B900700","A1000000", "B1000100", "A1000200","B1000300","A1010101010","A1010101210"),LaneEnum.NORTHBOUND);
        Assert.assertEquals( Arrays.asList(900400,900500,1010101010,1010101210),correctNorthPattern);

        List<Integer> wrongNorthPattern = CrossUtils.getMillsFrom(Arrays.asList("B222222", "A3434", "C1","A900000", "B900100", "A900200","B900300"),LaneEnum.NORTHBOUND);
        Assert.assertEquals( Collections.EMPTY_LIST,wrongNorthPattern);






    }


}
