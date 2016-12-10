package com.rubberhose.repository;

import com.rubberhose.repository.CrossRepository;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created by root on 08/12/16.
 */
public class CrossRepositoryTest {

    private static final List<String> CROSSES_TO_SAVE_MODEL = Arrays.asList("A638379", "B638382");


    @Test
    public void shouldSaveAndGetCrosses_when_everythingIsFine(){

        CrossRepository crossRepository = new CrossRepository();


        crossRepository.save(CROSSES_TO_SAVE_MODEL);
        List<String> returnedCrosses = crossRepository.getCrossCollection();

        Assert.assertEquals(CROSSES_TO_SAVE_MODEL,returnedCrosses);
    }

    @Test
    public void shouldSaveAndNotDuplicate_when_everythingIsFine(){

        CrossRepository crossRepository = new CrossRepository();

        final int initialCrossesSize = CROSSES_TO_SAVE_MODEL.size();
        crossRepository.save(CROSSES_TO_SAVE_MODEL);
        //Trying to save the same two crosses (Must be ignored)
        crossRepository.save(CROSSES_TO_SAVE_MODEL);
        Assert.assertEquals(CROSSES_TO_SAVE_MODEL.size(),initialCrossesSize);
    }


}
