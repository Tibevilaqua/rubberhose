package com.rubberhose.infrastructure;

import com.rubberhose.infrastructure.utils.PeriodUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.rubberhose.infrastructure.utils.PeriodUtils.PERIOD_OF_FIFTEEN_MINUTES_MILLS;

/**
 * Created by root on 13/12/16.
 */
public class PeriodDTOUtilsTest {

    @Test
    public void shouldReturnListOfFifteenMinutesPeriods_when_requested(){
        Map<String, Integer> result = PERIOD_OF_FIFTEEN_MINUTES_MILLS;

        Map<String, Integer> expectedResult = new HashMap<>();
        expectedResult.put("00:00AM",0);
        expectedResult.put("23:45PM",85500000);
        expectedResult.put("12:00PM",43200000);

        Assert.assertEquals(expectedResult.get("00:00AM"), result.get("00:00AM"));
        Assert.assertEquals(expectedResult.get("23:45PM"), result.get("23:45PM"));
        Assert.assertEquals(expectedResult.get("12:00PM"), result.get("12:00PM"));
        Assert.assertTrue(result.size() == 96);

    }


}
