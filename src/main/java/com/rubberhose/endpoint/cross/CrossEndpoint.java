package com.rubberhose.endpoint.cross;

import com.rubberhose.business.CrossBusiness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.time.DayOfWeek;

import static com.rubberhose.endpoint.URL.CROSS;


/**
 * Created by root on 05/12/16.
 */
@RestController
public class CrossEndpoint {

    private CrossBusiness crossBusiness;

    public CrossEndpoint(CrossBusiness crossBusiness) {
        this.crossBusiness = crossBusiness;
    }

    /**
     * Save all crosses
     *
     * @param crosses can't be null or empty, otherwise MethodArgumentNotValidException will be thrown
     * @return
     */
    @RequestMapping(value = CROSS,method = RequestMethod.POST)
    public ResponseEntity<Void> save(@RequestBody @Valid CrossDTO crosses)
    {
        crossBusiness.save(crosses);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = CROSS,method = RequestMethod.GET)
    public ResponseEntity<CrossBroadStatisticDTO> get(@RequestParam(required = false) DayOfWeek dayOfWeek)
    {

        return new ResponseEntity<>(crossBusiness.getStatistics(dayOfWeek),HttpStatus.OK);
    }







}
