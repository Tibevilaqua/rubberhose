package com.rubberhose.business;

import com.rubberhose.endpoint.cross.CrossBroadStatisticDTO;
import com.rubberhose.endpoint.cross.CrossDTO;
import com.rubberhose.infrastructure.exception.CustomException;
import com.rubberhose.repository.CrossRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.rubberhose.infrastructure.exception.ExceptionEnum.INNACURATE_CROSS_PATTERN;
import static org.mockito.Mockito.when;

/**
 * Created by root on 10/12/16.
 */
public class CrossBusinessTest {

    private CrossRepository crossRepository;
    private CrossBusiness crossBusiness;

    @Before
    public void setUp(){
        crossRepository = Mockito.mock(CrossRepository.class);
        crossBusiness = new CrossBusiness(crossRepository);
    }

    @Test
    public void shouldReturnCrossStatistics_when_someValueIsCached(){
        CrossBroadStatisticDTO expectedResult = new CrossBroadStatisticDTO(40,70,16,8,5,4);

        when(crossRepository.getCachedStatistics()).thenReturn(expectedResult);
        CrossBroadStatisticDTO result = crossBusiness.getStatistics();

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

        for(int i = 1; i <= 600; i++){
            String lane = i < 300 ? "A" : "B";
            crosses.add(String.format("%s%s",lane,sixHundredthOf24HoursInMills * i));
        }

        //Preparing calls
        when(crossRepository.getCrossCollection()).thenReturn(crosses);
        Mockito.doCallRealMethod().when(crossRepository).setCachedStatistics(Mockito.any(CrossBroadStatisticDTO.class));
        when(crossRepository.getCachedStatistics()).thenCallRealMethod();

        //Creating statistics
        Assert.assertTrue(crossBusiness.createStatistics());
        CrossBroadStatisticDTO result = crossRepository.getCachedStatistics();
        CrossBroadStatisticDTO expectedResult = new CrossBroadStatisticDTO(100,100,8,4,3,2);

        Assert.assertEquals(result,expectedResult);

    }

    @Test
    public void shouldNotCreateStatiscts_when_thereIsNoValuesInsideTheCollection() {
        when(crossRepository.getCrossCollection()).thenReturn(new ArrayList<>());

        Assert.assertFalse(crossBusiness.createStatistics());
        when(crossRepository.getCrossCollection()).thenReturn(null);
        Assert.assertFalse(crossBusiness.createStatistics());
    }

}
