package com.rubberhose.endpoint;

import com.rubberhose.business.CrossBusiness;
import com.rubberhose.endpoint.cross.CrossDTO;
import com.rubberhose.endpoint.cross.CrossEndpoint;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

/**
 * Created by root on 05/12/16.
 */
public class CrossEndpointTest {

    CrossBusiness crossBusiness;



    @Before
    public void setUp(){
        crossBusiness = Mockito.mock(CrossBusiness.class);
    }


    @Test
    public void shouldReturnCreated_when_crossesAreSaved(){
        CrossDTO crossDTO = new CrossDTO(Arrays.asList("A638379", "B638382"));

        Mockito.doNothing().when(crossBusiness).save(crossDTO);

        CrossEndpoint crossEndpoint = new CrossEndpoint(crossBusiness);
        ResponseEntity<Void> result = crossEndpoint.save(crossDTO);

        Assert.assertEquals("Crosses should be saved",result.getStatusCode(), HttpStatus.CREATED);
    }


    /**
     * Guarantees that annotation @Valid will be present at the save method inside VehicleEndpoint,
     * preventing the crosses list of being null
     */
    @Test
    public void shouldBeFine_when_saveMethodContainsValidAnnotation(){

        Method saveCrossesMethod = null;

        try {

            saveCrossesMethod = CrossEndpoint.class.getMethod("save", CrossDTO.class);
        } catch (NoSuchMethodException e) {
            //If there's no save method, then, fail.
            Assert.fail();
        }


        Parameter[] parameters = saveCrossesMethod.getParameters();
        boolean isValidPresent = false;
        for(Parameter p : parameters){
            if(p.getAnnotation(Valid.class) != null){
                isValidPresent = true;
            }
         }


        Assert.assertTrue("Annotation @Valid must be present", isValidPresent);

    }








}
