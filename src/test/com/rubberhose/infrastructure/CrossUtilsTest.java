package com.rubberhose.infrastructure;

import com.rubberhose.infrastructure.utils.CrossUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created by root on 10/12/16.
 */
public class CrossUtilsTest {


    @Test
    public void shouldReturnOnlyMillsFromCrosses_when_itIsExcepted(){
        List<Integer> expectedResult = Arrays.asList(222222,3434,1);
        List<Integer> result = CrossUtils.getMillsFrom(Arrays.asList("A222222", "B3434", "C1"));
        Assert.assertEquals(expectedResult,result);
    }


}
