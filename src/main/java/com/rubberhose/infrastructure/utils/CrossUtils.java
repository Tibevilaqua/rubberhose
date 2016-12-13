package com.rubberhose.infrastructure.utils;

import com.rubberhose.endpoint.cross.CrossDTO;
import com.rubberhose.infrastructure.LaneEnum;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by root on 10/12/16.
 */
public class CrossUtils {


    private static final String MACHINE_VALUE_PATTERN = "^(?!([A-Z][0-9]{1,10})).*$";

    /**
     * Return only the mills (sorted)
     * From: A303030
     * To:   303030
     */
    public static List<Integer> getMillsFrom(List<String> crosses){
        return  crosses.stream().map(eachCross -> Integer.valueOf(eachCross.substring(1, eachCross.length()))).sorted().collect(Collectors.toList());
    }

    /**
     * Return only the mills (sorted)
     * From: A303030
     * To:   303030
     */
    public static List<Integer> getMillsFrom(List<String> crosses, LaneEnum laneEnum){
        return  crosses.stream().filter(eachCross -> laneEnum.getValue().equals(eachCross.substring(0,1))).map(eachCross -> Integer.valueOf(eachCross.substring(1, eachCross.length()))).sorted().collect(Collectors.toList());
    }

    /**
     * True if the cross values do not follow the patten (One Letter + up to 10 Numbers).
     * False otherwise.
     */
    public static boolean isListInaccurate(CrossDTO crossDTO){
        return crossDTO.getCrosses().stream().anyMatch(eachCross -> Objects.isNull(eachCross) || eachCross.length() > 11 || eachCross.matches(CrossUtils.MACHINE_VALUE_PATTERN));
    }



    public static Integer dividePer(Integer division, Integer numberOfCrosses){
        return new BigDecimal(numberOfCrosses).divide(new BigDecimal(division),BigDecimal.ROUND_HALF_UP).intValue();
    }






}
