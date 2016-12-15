package com.rubberhose.endpoint;

import com.rubberhose.business.CrossBusiness;
import com.rubberhose.business.CrossStatisticsFactory;
import com.rubberhose.endpoint.cross.CrossBroadStatisticDTO;
import com.rubberhose.endpoint.cross.DistanceDTO;
import com.rubberhose.endpoint.cross.PeriodDTO;
import com.rubberhose.endpoint.cross.TrafficDTO;
import com.rubberhose.infrastructure.cross.CrossCache;
import com.rubberhose.repository.CrossRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by root on 14/12/16.
 */
public class CrossStatisticsFactoryTest {

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
    public void shouldReturnExactValue_when_onlyLaneAIsUsed(){

       Mockito.when(crossRepository.getCrossCollection(DayOfWeek.MONDAY)).thenReturn(Arrays.asList("A900000","A900165","A900200","A900365"));

       PeriodDTO expectedPeriod = new PeriodDTO.PeriodDTOBuilder().setPeriod("00:15AM").setNumberOfCars(2).createPeriodDTO();

       CrossBroadStatisticDTO expectedResult = new CrossBroadStatisticDTO(2,0,0,0,0,0,new TrafficDTO("00:15AM",2,Arrays.asList(expectedPeriod)),54,new DistanceDTO(1,"00:15AM", Arrays.asList(new PeriodDTO.PeriodDTOBuilder().setDistanceInMeters(1).setPeriod("00:15AM").createPeriodDTO())));

       CrossBroadStatisticDTO result = crossBusiness.createStatistics(DayOfWeek.MONDAY).get();

       Assert.assertEquals(expectedResult,result);
   }

    @Test
    public void shouldReturnExactValue_when_onlyLaneBIsUsed(){

        Mockito.when(crossRepository.getCrossCollection(DayOfWeek.MONDAY)).thenReturn(Arrays.asList("A900000","B900010","A900150","B900205","A46800000","B46800100","A46800135","B46800265","A46900000","B46900100","A46900135","B46900265"));


        PeriodDTO firstExpectedPeriod = new PeriodDTO.PeriodDTOBuilder().setPeriod("00:15AM").setNumberOfCars(1).createPeriodDTO();
        PeriodDTO secondExpectedPeriod = new PeriodDTO.PeriodDTOBuilder().setPeriod("13:00PM").setNumberOfCars(2).createPeriodDTO();

        CrossBroadStatisticDTO expectedResult = new CrossBroadStatisticDTO(1,2,0,0,0,0,new TrafficDTO("13:00PM", 2,Arrays.asList(firstExpectedPeriod,secondExpectedPeriod)),54,new DistanceDTO(1662,"13:00PM", Arrays.asList(new PeriodDTO.PeriodDTOBuilder().setDistanceInMeters(1662).setPeriod("13:00PM").createPeriodDTO())));

        CrossBroadStatisticDTO result = crossBusiness.createStatistics(DayOfWeek.MONDAY).get();

        Assert.assertEquals(expectedResult,result);
    }


}
