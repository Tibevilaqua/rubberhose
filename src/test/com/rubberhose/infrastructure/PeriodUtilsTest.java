package com.rubberhose.infrastructure;

import com.rubberhose.infrastructure.utils.PeriodUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static com.rubberhose.infrastructure.utils.PeriodUtils.PERIOD_OF_FIFTEEN_MINUTES_MILLS;

/**
 * Created by root on 13/12/16.
 */
public class PeriodUtilsTest {

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


    @Test
    public void shouldReturnAccuratePeriods_when_requested(){
        List<String> crosses = Arrays.asList("A900000","A900150","A900300","A900500","A64800000","A64800050","A64800300","64800350");
        Map<String, Integer> periods = PeriodUtils.getPeriods(crosses, LaneEnum.NORTHBOUND);

        Assert.assertEquals(periods.get("00:15AM").intValue(), 2);
        Assert.assertEquals(periods.get("18:00PM").intValue(), 1);

        //Verifying that all other periods have no value
        for(String period : periods.keySet()){
            if("00:15AM".equals(period) || "18:00PM".equals(period)){
                continue;
            }

            Assert.assertEquals(periods.get(period).intValue(),0);
        }


    }





}
