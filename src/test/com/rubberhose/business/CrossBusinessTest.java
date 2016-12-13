package com.rubberhose.business;

import com.rubberhose.endpoint.cross.CrossBroadStatisticDTO;
import com.rubberhose.endpoint.cross.CrossDTO;
import com.rubberhose.endpoint.cross.PeakPeriodDTO;
import com.rubberhose.infrastructure.cross.CrossCache;
import com.rubberhose.infrastructure.exception.CustomException;
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

        CrossBroadStatisticDTO expectedResult = new CrossBroadStatisticDTO(40,70,16,8,5,4,new PeakPeriodDTO("3:00PM",10));

        when(crossCache.getCachedStatistics(MONDAY)).thenReturn(expectedResult);
        CrossBroadStatisticDTO result = crossBusiness.getStatistics(MONDAY);

        Assert.assertEquals(expectedResult,result);
    }

    @Test
    public void shouldSaveCross_when_valuesFollowTheRightPattern(){
        List<String> crosses = Arrays.asList("A1", "B1234567890", "B66633");
        crossBusiness.save(new CrossDTO(crosses));
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

        final int sixHundredthOf24HoursInMills =144000;

        //Peak 00:30 (1 cross more than the other periods)
        crosses.add("A1800000");

        for(int i = 1; i <= 600; i++){
            String lane = i < 300 ? "A" : "B";
            crosses.add(String.format("%s%s",lane,sixHundredthOf24HoursInMills * i));
        }

        //Preparing calls
        when(crossRepository.getCrossCollection(MONDAY)).thenReturn(crosses);

        //Creating statistics
        Optional<CrossBroadStatisticDTO> statistics = crossBusiness.createStatistics(MONDAY);
        crossCache.setCachedStatistics(MONDAY,statistics.get());
        CrossBroadStatisticDTO result = crossCache.getCachedStatistics(MONDAY);

        CrossBroadStatisticDTO expectedResult = new CrossBroadStatisticDTO(100,100,8,4,3,2, new PeakPeriodDTO("00:30AM", 2));

        Assert.assertEquals(expectedResult,result);

    }


}
