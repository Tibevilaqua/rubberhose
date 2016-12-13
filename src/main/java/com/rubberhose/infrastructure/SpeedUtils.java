package com.rubberhose.infrastructure;

import com.rubberhose.infrastructure.utils.CrossUtils;

import java.math.BigDecimal;
import java.util.List;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static java.math.BigDecimal.valueOf;

/**
 * Created by root on 13/12/16.
 */
public class SpeedUtils {

    public static final Integer SPEED_LIMIT = 60;
    private static final float DISTANCE_BETWEEN_AXLES = 2.5f;
    public static final Integer SPEED_LIMIT_IN_MILLS = populateMillsLimitBetweenAxles();

    private static Integer populateMillsLimitBetweenAxles() {
        BigDecimal kmToMettersPerHour = valueOf(SPEED_LIMIT).multiply(valueOf(1000));
        BigDecimal hourToMinutes = kmToMettersPerHour.divide(valueOf(60),10, ROUND_HALF_UP);
        BigDecimal minutesToSeconds = hourToMinutes.divide(valueOf(60),10, ROUND_HALF_UP);
        BigDecimal secondsToMilliseconds = minutesToSeconds.divide(valueOf(1000),10, ROUND_HALF_UP);
        BigDecimal result = valueOf(DISTANCE_BETWEEN_AXLES).divide(secondsToMilliseconds,0, ROUND_HALF_UP);

        return result.intValue();
    }

    public static Long getDifferenceInMills(List<Integer> crossCollection){

        if(crossCollection == null || crossCollection.isEmpty()){
            return 0l;
        }

        Integer firstAxleCross = crossCollection.get(0), secondAxleCross;
        Long totalDifference = 0l;
        for(int i = 1; i < crossCollection.size(); i++){

            Integer crossValue = crossCollection.get(i);

            if(i % 2 == 0){
                firstAxleCross = crossValue;
            }else{
                secondAxleCross = crossValue;
                totalDifference+= secondAxleCross - (firstAxleCross + SPEED_LIMIT_IN_MILLS);
            }
        }

        return totalDifference;

    }


    public static Integer getAverageKMBasedOnMills(Integer numberOfCrosses, Long differenceInMills){
        if(insignificantDifference(differenceInMills)){
            return SPEED_LIMIT;
        }

        BigDecimal differencePerCarInMills = valueOf(differenceInMills).divide(valueOf(numberOfCrosses), ROUND_HALF_UP);
        BigDecimal percentageTaken = differencePerCarInMills.multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(SPEED_LIMIT_IN_MILLS),0,BigDecimal.ROUND_HALF_UP);
        BigDecimal finalSpeedAverage = percentageTaken.multiply(BigDecimal.valueOf(SPEED_LIMIT)).divide(BigDecimal.valueOf(100),0, BigDecimal.ROUND_HALF_UP);


        return SPEED_LIMIT - finalSpeedAverage.intValue();
    }

    private static boolean insignificantDifference(Long differenceInMills) {
        if(Long.valueOf(0).compareTo(differenceInMills) == 0 || (differenceInMills > 0 && differenceInMills < SPEED_LIMIT_IN_MILLS) || (differenceInMills < 0 && differenceInMills > SPEED_LIMIT_IN_MILLS*-1)){
            return true;
        }
        return false;
    }

}
