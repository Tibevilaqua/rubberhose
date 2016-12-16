package com.rubberhose.business;

import com.rubberhose.endpoint.cross.*;
import com.rubberhose.infrastructure.LaneEnum;
import com.rubberhose.infrastructure.utils.SpeedUtils;
import com.rubberhose.infrastructure.cross.CrossCache;
import com.rubberhose.infrastructure.exception.CustomException;
import com.rubberhose.infrastructure.exception.ExceptionEnum;
import com.rubberhose.repository.CrossRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.rubberhose.infrastructure.exception.ExceptionEnum.INNACURATE_CROSS_PATTERN;
import static java.time.DayOfWeek.MONDAY;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by root on 10/12/16.
 */
public class CrossBusinessTest {

    private CrossRepository crossRepository;
    private CrossBusiness crossBusiness;
    private CrossCache crossCache;
    private CrossStatisticsFactory crossStatisticsFactory;

    @Before
    public void setUp(){
        crossRepository = Mockito.mock(CrossRepository.class);
        crossCache = Mockito.spy(CrossCache.class);
        crossStatisticsFactory = Mockito.spy(CrossStatisticsFactory.class);

        crossBusiness = new CrossBusiness(crossRepository, crossCache,crossStatisticsFactory);
    }

    @Test
    public void shouldReturnCrossStatistics_when_someValueIsCached(){
        PeriodDTO.PeriodDTOBuilder expectedPeriod = new PeriodDTO.PeriodDTOBuilder().setPeriod("3:00PM").setNumberOfCars(10);
        CrossBroadStatisticDTO expectedResult = new CrossBroadStatisticDTO.CrossBroadStatisticDTOBuilder().setMorning(40).setEvening(70).setHourly(16).setEveryThirtyMinutes(8).setEveryTwentyMinutes(5).setEveryFifteenMinutes(4).setTraffic(new TrafficDTO.TrafficDTOBuilder().setPeak("3:00PM").setNumberOfCars(10).setPeriods(Arrays.asList(expectedPeriod))).setAverageSpeed(1).setDistanceDTO(null).createCrossBroadStatisticDTO();

        when(crossCache.getCachedStatistics(MONDAY)).thenReturn(expectedResult);
        CrossBroadStatisticDTO result = crossBusiness.getStatistics(MONDAY);

        Assert.assertEquals(expectedResult,result);
    }


    @Test
    public void shouldThrowException_when_valuesFollowTheRightPattern(){

        List<String> crosses = Arrays.asList("A112345678901","A", "B", "1","12937B","a","b","-","!","@","#","%","¨","&","*","(",")",null);
        int expectedExceptions = crosses.size();
        int threwException = 0;

        for(String cross : crosses){

            try{
                crossBusiness.save(new CrossDTO(Arrays.asList(cross)));
            }catch (CustomException c){
                threwException++;
                Assert.assertEquals(c.getDescription(), INNACURATE_CROSS_PATTERN.getDescription());
                Assert.assertEquals(c.getHttpStatus(), INNACURATE_CROSS_PATTERN.getHttpStatus());
            }
        }
        Assert.assertEquals(expectedExceptions,threwException);
    }


    @Test
    public void shouldCreateStatistics_when_thereAreCachedValues(){

        List<String> crosses = new ArrayList<>(40);

        // 100 cars lane A = 200 crosses
        // 100 cars lane B = 400 crosses

        final int sixHundredthOf24HoursInMills =288000;

        //Peak 0:30AM (4 crosses more than the other periods)
        crosses.add("A1800000");
        crosses.add(String.format("%s%s","A1800", (SpeedUtils.SPEED_LIMIT_IN_MILLS + 3)));
        crosses.add("A1808000");
        crosses.add(String.format("%s%s","A1808", (SpeedUtils.SPEED_LIMIT_IN_MILLS + 3)));

        for(int i = 1; i <= 224; i++){

            if(i < 150){


                crosses.add(String.format("%s%s",LaneEnum.NORTHBOUND.getValue(),sixHundredthOf24HoursInMills * i));
                crosses.add(String.format("%s%s",LaneEnum.NORTHBOUND.getValue(),sixHundredthOf24HoursInMills * i + SpeedUtils.SPEED_LIMIT_IN_MILLS)); // Always moving at the speed limit
            }else{
                Integer firstLaneAValue = sixHundredthOf24HoursInMills * i;
                Integer secondLaneBValue = sixHundredthOf24HoursInMills * i +100;
                crosses.add(String.format("%s%s",LaneEnum.NORTHBOUND.getValue(),firstLaneAValue));
                crosses.add(String.format("%s%s",LaneEnum.SOUTHBOUND.getValue(),secondLaneBValue)); // Always moving at the speed limit
                crosses.add(String.format("%s%s",LaneEnum.NORTHBOUND.getValue(),firstLaneAValue + SpeedUtils.SPEED_LIMIT_IN_MILLS));
                crosses.add(String.format("%s%s",LaneEnum.SOUTHBOUND.getValue(),secondLaneBValue + SpeedUtils.SPEED_LIMIT_IN_MILLS)); // Always moving at the speed limit

            }

        }

        //Preparing calls
        when(crossRepository.getCrossCollection(MONDAY)).thenReturn(crosses);

        //Creating statistics
        Optional<CrossBroadStatisticDTO> statistics = crossBusiness.createStatistics(MONDAY);
        crossCache.setCachedStatistics(MONDAY,statistics.get());
        CrossBroadStatisticDTO result = crossCache.getCachedStatistics(MONDAY);
        PeriodDTO.PeriodDTOBuilder expectedPeriod = new PeriodDTO.PeriodDTOBuilder().setPeriod("00:30AM").setNumberOfCars(5);
        CrossBroadStatisticDTO expectedResult = new CrossBroadStatisticDTO.CrossBroadStatisticDTOBuilder().setMorning(101).setEvening(100).setHourly(8).setEveryThirtyMinutes(4).setEveryTwentyMinutes(3).setEveryFifteenMinutes(2).setTraffic(new TrafficDTO.TrafficDTOBuilder().setPeak("00:30AM").setNumberOfCars(5).setPeriods(Arrays.asList(expectedPeriod))).setAverageSpeed(SpeedUtils.SPEED_LIMIT).setDistanceDTO(null).createCrossBroadStatisticDTO();

        Assert.assertEquals(expectedResult.getMorning(),result.getMorning());
        Assert.assertEquals(expectedResult.getEvening(),result.getEvening());
        Assert.assertEquals(expectedResult.getAverageSpeed(),result.getAverageSpeed());
        Assert.assertEquals(expectedResult.getEveryFifteenMinutes(),result.getEveryFifteenMinutes());
        Assert.assertEquals(expectedResult.getEveryThirtyMinutes(),result.getEveryThirtyMinutes());
        Assert.assertEquals(expectedResult.getEveryTwentyMinutes(),result.getEveryTwentyMinutes());
        Assert.assertEquals(expectedResult.getHourly(),result.getHourly());
    }


    @Test
    public void shouldThrowExceptions_when_patternsOrValuesAreInvalids(){
        int expectedNumberOfExceptions = 3;
        int exceptionsCaught = 0;

        List<List<String>> crosses = Arrays.asList(Arrays.asList("A1010101010"), Arrays.asList("A1010101010", "B1010101010"), Arrays.asList("B1010101010", "B1010101010"));

        List<ExceptionEnum> expectedExceptions = Arrays.asList(ExceptionEnum.INVALID_NUMBER_OF_CROSS, ExceptionEnum.INVALID_CROSS_PATTERN, ExceptionEnum.INVALID_CROSS_PATTERN);

        for(int i = 0; i < expectedExceptions.size(); i ++){
            try{
                // Not allowed to send data of the 1st axle and don't second second (must be multiple of 2 or 4)
                crossBusiness.save(new CrossDTO(null, crosses.get(i)));
            }catch (CustomException c){
                Assert.assertEquals(expectedExceptions.get(i).getHttpStatus(), c.getHttpStatus());
                Assert.assertEquals(expectedExceptions.get(i).getDescription(), c.getDescription());
                exceptionsCaught++;
            }
        }


        Assert.assertEquals(expectedNumberOfExceptions,exceptionsCaught);

        //All nice, should save..
        crossBusiness.save(new CrossDTO(null, Arrays.asList("A1010101010","A1010101020")));
        crossBusiness.save(new CrossDTO(null, Arrays.asList("A1010101010","B1010101020","A1010101030","B1010101040")));
        crossBusiness.save(new CrossDTO(null, Arrays.asList("A10101010","A10101020","A1010101010","B1010101020","A1010101030","B1010101040")));
    }



    @Test
    public void shouldGetPeakAndPeriods_when_thereIsDataAvailable(){

        //Preparing calls
        when(crossRepository.getCrossCollection(MONDAY)).thenReturn(Arrays.asList("A900000","A900150","A900300","A900500","A64800000","B64800050","A64800300","B64800350"));

        //Creating statistics
        Optional<CrossBroadStatisticDTO> statistics = crossBusiness.createStatistics(MONDAY);
        crossCache.setCachedStatistics(MONDAY,statistics.get());
        CrossBroadStatisticDTO result = crossCache.getCachedStatistics(MONDAY);

        TrafficDTO traffic = result.getTraffic();

        Assert.assertEquals("00:15AM", traffic.getPeak());
        Assert.assertEquals(2, traffic.getNumberOfCars().intValue());

        Assert.assertEquals("18:00PM", traffic.getPeriods().get(1).getPeriod());
        Assert.assertEquals(1, traffic.getPeriods().get(1).getNumberOfCars().intValue());

        Assert.assertEquals(traffic.getPeriods().size(), 2);
    }



}
