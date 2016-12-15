package com.rubberhose.business;

import com.rubberhose.endpoint.cross.CrossBroadStatisticDTO;
import com.rubberhose.endpoint.cross.DistanceDTO;
import com.rubberhose.endpoint.cross.PeriodDTO;
import com.rubberhose.endpoint.cross.TrafficDTO;
import com.rubberhose.infrastructure.LaneEnum;
import com.rubberhose.infrastructure.OccurrencePerDayEnum;
import com.rubberhose.infrastructure.utils.CrossUtils;
import com.rubberhose.infrastructure.utils.PeriodUtils;
import com.rubberhose.infrastructure.utils.SpeedUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.rubberhose.infrastructure.MillsEnum.END_OF_MORNING;
import static com.rubberhose.infrastructure.utils.SpeedUtils.getAverageKMBasedOnMills;
import static com.rubberhose.infrastructure.utils.SpeedUtils.getDifferenceInMills;
import static com.rubberhose.infrastructure.utils.CrossUtils.dividePer;
import static com.rubberhose.infrastructure.utils.CrossUtils.getMillsFrom;

/**
 * Created by root on 12/12/16.
 */
@Component
public class CrossStatisticsFactory {





    /**
     * Grab all the necessary information from the database and through mathematic operations
     * transform the data into CrossBroadStatisticDTO object.
     *
     * Note, the information retrieved by this method considers the average number of cars
     * and not how many times each pneumatic rubber belt was reached
     *
     * @return
     */
    protected CrossBroadStatisticDTO createStatistics(List<String> crossCollection, Integer numberOfDays) {

            Integer lanesByHosesInUse = divisionRequiredToGetNumberOfCars(crossCollection);

            List<Integer> crossCollectionsMills = getMillsFrom(crossCollection);

            Integer morningCount = this.getMorningCount(crossCollectionsMills);

            Integer eveningCount = dividePer(lanesByHosesInUse,this.getEveningCount(crossCollectionsMills,morningCount));
            morningCount = dividePer(lanesByHosesInUse, morningCount);

            Integer averageSpeed = this.getAverageSpeed(crossCollection);

            DistanceDTO distance = this.getAverageDistance(crossCollection);

            Integer hourlyAverageCount = dividePer(lanesByHosesInUse,this.getHourAverageCount(crossCollectionsMills))
                    , thirtyMinutesAverageCount = dividePer(lanesByHosesInUse,this.getHalfHourAverageCount(crossCollectionsMills))
                    , twentyMinutesAverageCount = dividePer(lanesByHosesInUse,this.getEveryTwentyMinutesAverageCount(crossCollectionsMills))
                    , fifteenMinutesAverageCount = dividePer(lanesByHosesInUse,this.getEveryFifteenMinutesAverageCount(crossCollectionsMills));

        TrafficDTO periodDTO = this.getPeakPeriod(crossCollection);


        // If no numberOfDays is sent, therefore, the code considers it and divide based on it,
        // otherwise, it's a particular weekday.
        if(Objects.nonNull(numberOfDays) && numberOfDays > 0){
                morningCount = dividePer(numberOfDays, morningCount);
                eveningCount = dividePer(numberOfDays, eveningCount);
                hourlyAverageCount = dividePer(numberOfDays, hourlyAverageCount);
                thirtyMinutesAverageCount = dividePer(numberOfDays, thirtyMinutesAverageCount);
                twentyMinutesAverageCount = dividePer(numberOfDays, twentyMinutesAverageCount);
                fifteenMinutesAverageCount = dividePer(numberOfDays, fifteenMinutesAverageCount);
            }

        return  new CrossBroadStatisticDTO(morningCount,eveningCount,hourlyAverageCount,thirtyMinutesAverageCount,twentyMinutesAverageCount,fifteenMinutesAverageCount, periodDTO,averageSpeed,distance);

    }


    private Integer getAverageSpeed(List<String> crossCollection) {

        Long northLane = getDifferenceInMills(getMillsFrom(crossCollection, LaneEnum.NORTHBOUND),LaneEnum.NORTHBOUND);
        Long southLane = getDifferenceInMills(getMillsFrom(crossCollection, LaneEnum.SOUTHBOUND),LaneEnum.SOUTHBOUND);

        Integer numberOfCars = BigDecimal.valueOf(crossCollection.size()).divide(BigDecimal.valueOf(divisionRequiredToGetNumberOfCars(crossCollection)),BigDecimal.ROUND_HALF_UP).intValue();

        Integer finalSPeed = getAverageKMBasedOnMills(numberOfCars, northLane.longValue() + southLane.longValue());

        return finalSPeed;
    }


    private Integer getMorningCount(List<Integer> crossCollection) {
        return (int) crossCollection.stream().filter(eachCross -> eachCross.compareTo(END_OF_MORNING.value()) <= 0).count();
    }

    private Integer getEveningCount(List<Integer> crossCollectionsMills, Integer morningCount) {
        //If it's not morning, therefore, it's evening
        return crossCollectionsMills.size() - morningCount;
    }

    private Integer getHourAverageCount(List<Integer> crossCollectionsMills) {
        return this.getPeriodAverage.apply(crossCollectionsMills, OccurrencePerDayEnum.HOURLY);
    }

    private Integer getHalfHourAverageCount(List<Integer> crossCollectionsMills) {
        return this.getPeriodAverage.apply(crossCollectionsMills, OccurrencePerDayEnum.EVERY_THIRTY_MINUTES);
    }
    private Integer getEveryTwentyMinutesAverageCount(List<Integer> crossCollectionsMills) {
        return this.getPeriodAverage.apply(crossCollectionsMills, OccurrencePerDayEnum.EVERY_TWENTY_MINUTES);

    }
    private Integer getEveryFifteenMinutesAverageCount(List<Integer> crossCollectionsMills) {
        return this.getPeriodAverage.apply(crossCollectionsMills,OccurrencePerDayEnum.EVERY_FIFTEEN_MINUTES);

    }


    private DistanceDTO getAverageDistance(List<String> crosses) {

        List<PeriodDTO> periods = new ArrayList<>();
        Map<String, Integer> northLaneDistancePeriod = PeriodUtils.getPeriodsWithDistanceBetweenCars(crosses, LaneEnum.NORTHBOUND);
        Map<String, Integer> southLaneDistancePeriod = PeriodUtils.getPeriodsWithDistanceBetweenCars(crosses, LaneEnum.SOUTHBOUND);


        Integer closestDistanceInMeters = Integer.MAX_VALUE;
        String timePeriod = null;
        Integer divideBy = 0;
        for(String eachPeriod : northLaneDistancePeriod.keySet()){
            Integer northLaneDistance = northLaneDistancePeriod.get(eachPeriod), southLaneDistance = southLaneDistancePeriod.get(eachPeriod);
            divideBy+= northLaneDistance > 0 ? 1 : 0;
            divideBy+= southLaneDistance > 0 ? 1 : 0;
            if(divideBy == 0){
                continue;
            }

            //If both lanes are used, divide by 2, if 1, then divide by one.
            Integer distanceBetweenCarsInThisPeriod = BigDecimal.valueOf(northLaneDistance + southLaneDistance).divide(BigDecimal.valueOf(divideBy),BigDecimal.ROUND_HALF_UP).intValue();

            periods.add(new PeriodDTO.PeriodDTOBuilder().setPeriod(eachPeriod).setDistanceInMeters(distanceBetweenCarsInThisPeriod).createPeriodDTO());

            // Is there any car in between this period?
            if(distanceBetweenCarsInThisPeriod < closestDistanceInMeters) {

                //trough?
                if(distanceBetweenCarsInThisPeriod < closestDistanceInMeters){
                    closestDistanceInMeters = distanceBetweenCarsInThisPeriod;
                    timePeriod = eachPeriod;
                }
            }
            divideBy = 0;
        }

        return closestDistanceInMeters == Integer.MAX_VALUE ? new DistanceDTO() : new DistanceDTO(closestDistanceInMeters, timePeriod,periods);
    }

    /**
     * Compare how many crosses were captured based on each period of 15 minutes and return the closest period and the number of crosses altogether
     */
    private TrafficDTO getPeakPeriod(List<String> crosses) {

        Map<String, Integer> northLanePeriod = PeriodUtils.getPeriodsWithNumberOfCars(crosses, LaneEnum.NORTHBOUND);
        Map<String, Integer> southLanePeriod = PeriodUtils.getPeriodsWithNumberOfCars(crosses, LaneEnum.SOUTHBOUND);
        List<PeriodDTO> periods = new ArrayList<>(northLanePeriod.size());
        String peakPeriod = "";
        Integer maxNumberOfCars = 0;

        for(String eachPeriod : northLanePeriod.keySet()){
            Integer numberOfCarsInThisPeriod = northLanePeriod.get(eachPeriod) + southLanePeriod.get(eachPeriod);

            // Is there any car in between this period?
            if(numberOfCarsInThisPeriod > 0) {
                periods.add(new PeriodDTO.PeriodDTOBuilder().setPeriod(eachPeriod).setNumberOfCars(numberOfCarsInThisPeriod).createPeriodDTO());

                //peak?
                if(numberOfCarsInThisPeriod > maxNumberOfCars){
                    maxNumberOfCars = numberOfCarsInThisPeriod;
                    peakPeriod = eachPeriod;
                }

            }
        }

        return new TrafficDTO(peakPeriod, maxNumberOfCars,periods);

    }

    /**
     * Given a list of crosses and the necessary occurrencePerDay, the average is extracted and returned as an Integer
     */
    private BiFunction<List<Integer>, OccurrencePerDayEnum, Integer> getPeriodAverage = (crossCollectionsMills, occurrencePerDayEnum) -> {
        List<Integer> occurrencesOfTheDayInMills = IntStream.rangeClosed(0, occurrencePerDayEnum.getValue()).mapToObj(value -> Integer.valueOf(value * occurrencePerDayEnum.getRespectiveMillsEnum().value())).collect(Collectors.toList());

        List<Integer> countPerOccurrences = IntStream.range(1, occurrencesOfTheDayInMills.size())
                .mapToObj(eachHour -> (int) crossCollectionsMills.stream()
                        .filter(eachCross -> eachCross.compareTo(occurrencesOfTheDayInMills.get(eachHour-1)) > 0
                                && eachCross.compareTo(occurrencesOfTheDayInMills.get(eachHour)) <= 0)
                        .count())
                .collect(Collectors.toList());

        return new BigDecimal(countPerOccurrences.stream().mapToInt(Integer::intValue).sum()).divide(new BigDecimal(countPerOccurrences.size()),BigDecimal.ROUND_HALF_UP).intValue();

    };


    public Integer divisionRequiredToGetNumberOfCars(List<String> crosses) {

        boolean isNorthLaneInTheList = CrossUtils.isLaneUsed.test(crosses,LaneEnum.NORTHBOUND);
        boolean isSouthLaneInTheList = CrossUtils.isLaneUsed.test(crosses,LaneEnum.SOUTHBOUND);

        //If both lanes are used, then, divide by 3 to get the number of cars at the end
        if(isNorthLaneInTheList && isSouthLaneInTheList){
            return 3;
        }else if(isNorthLaneInTheList && !isSouthLaneInTheList){
            return 2;
        }else {
            return 4;
        }
    }
}
