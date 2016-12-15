package com.rubberhose.infrastructure;

import com.rubberhose.infrastructure.utils.CrossUtils;
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
    public void shouldReturnAccuratePeriodsOfLaneA_when_numberOfCarsIsRequested(){
        List<String> crosses = Arrays.asList("A900000","A900150","A900300","A900500","A64800000","A64800050","A64800300","A64800350");
        Map<String, Integer> periods = PeriodUtils.getPeriodsOf(crosses, LaneEnum.NORTHBOUND, CrossUtils.GET_NUMBER_OF_CARS);

        Assert.assertEquals( 2,periods.get("00:15AM").intValue());
        Assert.assertEquals(2,periods.get("18:00PM").intValue());

        validateListOfPeriods(periods, Arrays.asList("00:15AM","18:00PM"));

    }

    @Test
    public void shouldReturnAccuratePeriodsOfLaneB_when_numberOfCarsIsRequested(){
        List<String> crosses = Arrays.asList("A900000","B900150","A900300","B900500","A64800000","B64800050","A64800300","B64800350","A64800800","B64800850","A64800900","B64800950");
        Map<String, Integer> periods = PeriodUtils.getPeriodsOf(crosses, LaneEnum.SOUTHBOUND, CrossUtils.GET_NUMBER_OF_CARS);

        Assert.assertEquals(periods.get("00:15AM").intValue(), 1);
        Assert.assertEquals(periods.get("18:00PM").intValue(), 2);
        validateListOfPeriods(periods, Arrays.asList("00:15AM","18:00PM"));
    }

    @Test
    public void shouldReturnAccuratePeriodsOfLaneA_when_distanceBetweenCarsIsRequested(){
        List<String> crosses = Arrays.asList("A900000","A900150","A900300","A900500","A64800000","A64800050","A64800300","A64800350");
        Map<String, Integer> periods = PeriodUtils.getPeriodsOf(crosses, LaneEnum.NORTHBOUND, CrossUtils.GET_DISTANCE_BETWEEN_CARS);

        Assert.assertEquals(3, periods.get("00:15AM").intValue());
        Assert.assertEquals(4,periods.get("18:00PM").intValue());

        validateListOfPeriods(periods, Arrays.asList("00:15AM","18:00PM"));

    }

    @Test
    public void shouldReturnAccuratePeriodsOfLaneB_when_distanceBetweenCarsIsRequested(){
        List<String> crosses = Arrays.asList("A900000","B900150","A900300","B900500","A64800000","B64800050","A64800300","B64800350","A64800800","B64800850","A64800900","B64800950");
        Map<String, Integer> periods = PeriodUtils.getPeriodsOf(crosses, LaneEnum.SOUTHBOUND, CrossUtils.GET_DISTANCE_BETWEEN_CARS);

        Assert.assertEquals(8,periods.get("18:00PM").intValue());
        validateListOfPeriods(periods, Arrays.asList("18:00PM"));

    }


    private void validateListOfPeriods(Map<String,Integer> periods, List<String> exceptedValues){

        //Verifying that all other periods have no value
        for(String period : periods.keySet()){
            if(exceptedValues.contains(period)){
                continue;
            }

            Assert.assertEquals(0, periods.get(period).intValue());
        }


    }




}
