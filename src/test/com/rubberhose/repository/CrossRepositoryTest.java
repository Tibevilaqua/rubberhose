package com.rubberhose.repository;

import com.rubberhose.endpoint.cross.*;
import com.rubberhose.infrastructure.cross.CrossCache;
import org.junit.Assert;
import org.junit.Test;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.List;

import static java.time.DayOfWeek.MONDAY;

/**
 * Created by root on 08/12/16.
 */
public class CrossRepositoryTest {

    private static final CrossDTO CROSSES_TO_SAVE_MODEL = new CrossDTO(MONDAY, Arrays.asList("A638379", "B638382"));


    @Test
    public void shouldSaveAndGetCrosses_when_everythingIsFine(){

        CrossRepository crossRepository = new CrossRepository();

        crossRepository.save(CROSSES_TO_SAVE_MODEL.getDayOfWeek(),CROSSES_TO_SAVE_MODEL.getCrosses());
        List<String> returnedCrosses = crossRepository.getCrossCollection(MONDAY);

        Assert.assertEquals(CROSSES_TO_SAVE_MODEL.getCrosses(),returnedCrosses);
    }

    @Test
    public void shouldSaveAndNotDuplicate_when_everythingIsFine(){

        CrossRepository crossRepository = new CrossRepository();

        final int initialCrossesSize = CROSSES_TO_SAVE_MODEL.getCrosses().size();
        crossRepository.save(CROSSES_TO_SAVE_MODEL.getDayOfWeek(),CROSSES_TO_SAVE_MODEL.getCrosses());
        //Trying to save the same two crosses (Must be ignored)
        crossRepository.save(CROSSES_TO_SAVE_MODEL.getDayOfWeek(),CROSSES_TO_SAVE_MODEL.getCrosses());
        Assert.assertEquals(CROSSES_TO_SAVE_MODEL.getCrosses().size(),initialCrossesSize);
    }
    @Test
    public void shouldSaveBoth_when_dataValuesIsDifferent(){

        CrossRepository crossRepository = new CrossRepository();

        crossRepository.save(CROSSES_TO_SAVE_MODEL.getDayOfWeek(),CROSSES_TO_SAVE_MODEL.getCrosses());
        List<String> additionalCrosses = Arrays.asList("A638378", "B638381");
        crossRepository.save(CROSSES_TO_SAVE_MODEL.getDayOfWeek(),additionalCrosses);
        Assert.assertEquals(CROSSES_TO_SAVE_MODEL.getCrosses().size() + additionalCrosses.size(),crossRepository.getCrossCollection(CROSSES_TO_SAVE_MODEL.getDayOfWeek()).size());
    }
    @Test
    public void shouldOverwrite_when_dataIsCachedAgain(){

        CrossCache crossCache = new CrossCache();


        crossCache.setCachedStatistics(MONDAY, new CrossBroadStatisticDTO.CrossBroadStatisticDTOBuilder().setMorning(1).setEvening(2).setHourly(3).setEveryThirtyMinutes(4).setEveryTwentyMinutes(5).setEveryFifteenMinutes(6).setTraffic(new TrafficDTO.TrafficDTOBuilder()).setAverageSpeed(1).setDistance(null).createCrossBroadStatisticDTO());

        CrossBroadStatisticDTO secondCrossBroadStatisticDTO = new CrossBroadStatisticDTO.CrossBroadStatisticDTOBuilder().setMorning(2).setEvening(3).setHourly(4).setEveryThirtyMinutes(5).setEveryTwentyMinutes(6).setEveryFifteenMinutes(7).setTraffic(new TrafficDTO.TrafficDTOBuilder()).setAverageSpeed(1).setDistance(null).createCrossBroadStatisticDTO();
        crossCache.setCachedStatistics(MONDAY, secondCrossBroadStatisticDTO);
        Assert.assertEquals(crossCache.getCachedStatistics(MONDAY), secondCrossBroadStatisticDTO);
    }

    @Test
    public void shouldSaveAndReturn_when_noParametersAreSent(){

        CrossRepository crossRepository = new CrossRepository();
        crossRepository.save(CROSSES_TO_SAVE_MODEL.getDayOfWeek(),CROSSES_TO_SAVE_MODEL.getCrosses());

        List<String> result = crossRepository.getCrossCollection(null);

        Assert.assertEquals(result,CROSSES_TO_SAVE_MODEL.getCrosses());
    }

    @Test
    public void shouldReturnNothing_when_thereAreNoCrossesForTheDay(){

        CrossRepository crossRepository = new CrossRepository();
        crossRepository.save(CROSSES_TO_SAVE_MODEL.getDayOfWeek(),CROSSES_TO_SAVE_MODEL.getCrosses());
        Assert.assertTrue(crossRepository.getCrossCollection(DayOfWeek.TUESDAY).isEmpty());
    }

    @Test
    public void shouldReturnNothing_when_thereAreNoCrossesForTheDayInTheCache(){

        CrossCache crossCache = new CrossCache();
        crossCache.setCachedStatistics(CROSSES_TO_SAVE_MODEL.getDayOfWeek(), new CrossBroadStatisticDTO.CrossBroadStatisticDTOBuilder().setMorning(1).setEvening(2).setHourly(3).setEveryThirtyMinutes(4).setEveryTwentyMinutes(5).setEveryFifteenMinutes(6).setTraffic(new TrafficDTO.TrafficDTOBuilder()).setAverageSpeed(1).setDistance(null).createCrossBroadStatisticDTO());
        CrossBroadStatisticDTO cachedStatistics = crossCache.getCachedStatistics(DayOfWeek.TUESDAY);
        Assert.assertTrue(cachedStatistics == null);
    }





}
