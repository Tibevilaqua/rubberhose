package com.rubberhose.infrastructure.utils;

import com.rubberhose.infrastructure.LaneEnum;
import com.rubberhose.infrastructure.MillsEnum;
import com.rubberhose.infrastructure.OccurrencePerDayEnum;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.rubberhose.infrastructure.utils.CrossUtils.getMillsFrom;

/**
 * Created by root on 13/12/16.
 */
public class PeriodUtils {



    public static final Map<String, Integer> PERIOD_OF_FIFTEEN_MINUTES_MILLS = populatePeriodAndMills();

    /**
     *
     * @return Map comprising all the periods eg 00:00AM until 23:45PM according to their values in mills,
     * so, 00:15AM = MillsEnum.FIFTEEN_MINUTES.value()
     */
    private static Map<String,Integer> populatePeriodAndMills() {
        List<Integer> eachFifteenMinutesInMills = IntStream.rangeClosed(0,OccurrencePerDayEnum.EVERY_FIFTEEN_MINUTES.getValue()).map(eachPeriodOfFifteenMinutes -> eachPeriodOfFifteenMinutes * MillsEnum.FIFTEEN_MINUTES.value()).boxed().collect(Collectors.toList());
        Map<String,Integer> result = new LinkedHashMap<>();
        int currentHours = 0;
        int currentMinutes = 0;
        String currentPeriod = "AM";

        for(int i = 1; i <= OccurrencePerDayEnum.EVERY_FIFTEEN_MINUTES.getValue(); i++){
            if(currentMinutes == 60){
                currentHours++;
                currentMinutes=0;
            }


            if(i >= OccurrencePerDayEnum.EVERY_FIFTEEN_MINUTES.getValue() /2){
                currentPeriod = "PM";
            }

            result.put(String.format("%s%s%s%s", ifOneZeroThenTwo(currentHours), ":",ifOneZeroThenTwo(currentMinutes),currentPeriod), eachFifteenMinutesInMills.get(i-1));
            currentMinutes+=15;
        }

        return result;
    }


    public static Map<String, Integer> getPeriodsWithDistanceBetweenCars(List<String> crosses, LaneEnum laneEnum){
        Map<String, Integer> periodOfFifteenMinutesMills = PERIOD_OF_FIFTEEN_MINUTES_MILLS;
        Map<String,Integer> result = new LinkedHashMap<>();

        List<Integer> laneCrossesInMills = CrossUtils.getMillsFrom(crosses, laneEnum);

        for(String eachPeriod : periodOfFifteenMinutesMills.keySet()){
            int beginningCurrentPeriodInMills = periodOfFifteenMinutesMills.get(eachPeriod).intValue();
            int limitCurrentPeriodInMills = (beginningCurrentPeriodInMills + MillsEnum.FIFTEEN_MINUTES.value());

            //TODO Refactor
            List<Integer> crossesBetweenPeriod = laneCrossesInMills.stream().filter(cross -> cross >= beginningCurrentPeriodInMills && cross < limitCurrentPeriodInMills).collect(Collectors.toList());

            Long laneDistanceInMeters = SpeedUtils.getDistanceInMills(crossesBetweenPeriod, laneEnum);

            result.put(eachPeriod, laneDistanceInMeters.intValue());
        }

        return result;
    }

    public static Map<String, Integer> getPeriodsWithNumberOfCars(List<String> crosses, LaneEnum laneEnum){
        Map<String, Integer> periodOfFifteenMinutesMills = PERIOD_OF_FIFTEEN_MINUTES_MILLS;
        Map<String,Integer> result = new LinkedHashMap<>();

            List<Integer> laneCrossesInMills = CrossUtils.getMillsFrom(crosses, laneEnum);

            for(String eachPeriod : periodOfFifteenMinutesMills.keySet()){
                int beginningCurrentPeriodInMills = periodOfFifteenMinutesMills.get(eachPeriod).intValue();
                int limitCurrentPeriodInMills = (beginningCurrentPeriodInMills + MillsEnum.FIFTEEN_MINUTES.value());

                //TODO Refactor
                int currentNumberOfCrosses = CrossUtils.getNumberOfCarsInBetween(beginningCurrentPeriodInMills,limitCurrentPeriodInMills,laneCrossesInMills);

                Integer numberOfCars = BigDecimal.valueOf(currentNumberOfCrosses).divide(BigDecimal.valueOf(laneEnum.getCrossesPerCar()),BigDecimal.ROUND_HALF_UP).intValue();

                result.put(eachPeriod,numberOfCars);
        }

        return result;
    }


    private static String ifOneZeroThenTwo(int value){
        return value == 0 ? "00" : String.valueOf(value);
    }




}
