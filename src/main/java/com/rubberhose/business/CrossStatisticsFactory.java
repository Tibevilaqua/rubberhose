package com.rubberhose.business;

import com.rubberhose.endpoint.cross.*;
import com.rubberhose.infrastructure.LaneEnum;
import com.rubberhose.infrastructure.OccurrencePerDayEnum;
import com.rubberhose.infrastructure.utils.CrossUtils;
import com.rubberhose.infrastructure.utils.PeriodUtils;
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
    protected CrossBroadStatisticDTO createStatistics(List<String> crossCollection) {
        return this.getDefaultValues(crossCollection).createCrossBroadStatisticDTO();
    }

    protected CrossBroadStatisticDTO createStatistics(List<String> crossCollection, Integer numberOfDays) {

        CrossBroadStatisticDTO.CrossBroadStatisticDTOBuilder defaultValues = this.getDefaultValues(crossCollection);
        getAverageOfValuesBasedOn(numberOfDays, defaultValues);

        return defaultValues.createCrossBroadStatisticDTO();

    }

    private void getAverageOfValuesBasedOn(Integer numberOfDays, CrossBroadStatisticDTO.CrossBroadStatisticDTOBuilder defaultValues) {
        // If no numberOfDays is sent, therefore, the code considers it and divides based on it,
        // otherwise, it's a particular weekday.
        if(Objects.nonNull(numberOfDays) && numberOfDays > 0){
            defaultValues.setMorning(dividePer(numberOfDays, defaultValues.getMorning()));
            defaultValues.setEvening(dividePer(numberOfDays, defaultValues.getEvening()));
            defaultValues.setHourly(dividePer(numberOfDays, defaultValues.getHourly()));
            defaultValues.setEveryThirtyMinutes(dividePer(numberOfDays, defaultValues.getEveryThirtyMinutes()));
            defaultValues.setEveryTwentyMinutes(dividePer(numberOfDays, defaultValues.getEveryTwentyMinutes()));
            defaultValues.setEveryFifteenMinutes(dividePer(numberOfDays, defaultValues.getEveryFifteenMinutes()));
        }

        //Updating trafficDTO
        TrafficDTO.TrafficDTOBuilder trafficDTOBuilder = defaultValues.getTraffic();
        trafficDTOBuilder.setNumberOfCars(BigDecimal.valueOf(defaultValues.getTraffic().getNumberOfCars()).divide(BigDecimal.valueOf(numberOfDays),0,BigDecimal.ROUND_HALF_UP).intValue());
        trafficDTOBuilder.getPeriods().stream().forEach(period -> period.setNumberOfCars(BigDecimal.valueOf(period.getNumberOfCars()).divide(BigDecimal.valueOf(numberOfDays),0,BigDecimal.ROUND_HALF_UP).intValue()));

    }


    private CrossBroadStatisticDTO.CrossBroadStatisticDTOBuilder getDefaultValues(List<String> crossCollection){
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

        TrafficDTO.TrafficDTOBuilder periodDTO = this.getPeakPeriod(crossCollection);

        return new CrossBroadStatisticDTO.CrossBroadStatisticDTOBuilder().setMorning(morningCount).setEvening(eveningCount).setHourly(hourlyAverageCount).setEveryThirtyMinutes(thirtyMinutesAverageCount).setEveryTwentyMinutes(twentyMinutesAverageCount).setEveryFifteenMinutes(fifteenMinutesAverageCount).setTraffic(periodDTO).setAverageSpeed(averageSpeed).setDistanceDTO(distance);

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
        Map<String, Integer> northLaneDistancePeriod = PeriodUtils.getPeriodsOf(crosses, LaneEnum.NORTHBOUND, CrossUtils.GET_DISTANCE_BETWEEN_CARS);
        Map<String, Integer> southLaneDistancePeriod = PeriodUtils.getPeriodsOf(crosses, LaneEnum.SOUTHBOUND, CrossUtils.GET_DISTANCE_BETWEEN_CARS);


        Integer closestDistanceInMeters = Integer.MAX_VALUE;
        String timePeriod = null;

        for(String eachPeriod : getKeys(northLaneDistancePeriod, southLaneDistancePeriod)){
            Integer northLaneDistance = northLaneDistancePeriod.containsKey(eachPeriod) ? northLaneDistancePeriod.get(eachPeriod) : 0;
            Integer southLaneDistance = southLaneDistancePeriod.containsKey(eachPeriod) ? southLaneDistancePeriod.get(eachPeriod)  : 0;

            Integer lanesUsed = usedLanes(northLaneDistance, southLaneDistance);

            //If both lanes are used, divide by 2, if 1, then divide by one.
            Integer distanceBetweenCarsInThisPeriod = BigDecimal.valueOf(northLaneDistance + southLaneDistance).divide(BigDecimal.valueOf(lanesUsed),BigDecimal.ROUND_HALF_UP).intValue();

            if(distanceBetweenCarsInThisPeriod > 0){

                periods.add(new PeriodDTO.PeriodDTOBuilder().setPeriod(eachPeriod).setDistanceInMeters(distanceBetweenCarsInThisPeriod).createPeriodDTO());

                // Is there any car in between this period?
                if(distanceBetweenCarsInThisPeriod < closestDistanceInMeters) {

                    //trough?
                    if(distanceBetweenCarsInThisPeriod < closestDistanceInMeters){
                        closestDistanceInMeters = distanceBetweenCarsInThisPeriod;
                        timePeriod = eachPeriod;
                    }
                }
            }
        }

        return closestDistanceInMeters == Integer.MAX_VALUE ? new DistanceDTO() : new DistanceDTO(closestDistanceInMeters, timePeriod,periods);
    }

    private Integer usedLanes(Integer northLaneDistance, Integer southLaneDistance) {
        Integer lanesUsed = 2;
        lanesUsed -= northLaneDistance == 0 ? 1 : 0;
        lanesUsed -= southLaneDistance == 0 ? 1 : 0;
        return lanesUsed;
    }

    /**
     * Compare how many crosses were captured based on each period of 15 minutes and return the closest period and the number of crosses altogether
     */
    private TrafficDTO.TrafficDTOBuilder getPeakPeriod(List<String> crosses) {

        Map<String, Integer> northLanePeriod = PeriodUtils.getPeriodsOf(crosses, LaneEnum.NORTHBOUND,CrossUtils.GET_NUMBER_OF_CARS);
        Map<String, Integer> southLanePeriod = PeriodUtils.getPeriodsOf(crosses, LaneEnum.SOUTHBOUND,CrossUtils.GET_NUMBER_OF_CARS);


        List<PeriodDTO.PeriodDTOBuilder> periods = new ArrayList<>(northLanePeriod.size());
        String peakPeriod = "";
        Integer maxNumberOfCars = 0;

        for(String eachPeriod : getKeys(northLanePeriod, southLanePeriod)){
            Integer numberOfCarsInThisPeriod = northLanePeriod.containsKey(eachPeriod) ? northLanePeriod.get(eachPeriod) : 0;
            numberOfCarsInThisPeriod += southLanePeriod.containsKey(eachPeriod) ? southLanePeriod.get(eachPeriod) : 0;

            // Is there any car in between this period?
            if(numberOfCarsInThisPeriod > 0) {
                periods.add(new PeriodDTO.PeriodDTOBuilder().setPeriod(eachPeriod).setNumberOfCars(numberOfCarsInThisPeriod));

                //peak?
                if(numberOfCarsInThisPeriod > maxNumberOfCars){
                    maxNumberOfCars = numberOfCarsInThisPeriod;
                    peakPeriod = eachPeriod;
                }
            }
        }

        return new TrafficDTO.TrafficDTOBuilder().setPeak(peakPeriod).setNumberOfCars(maxNumberOfCars).setPeriods(periods);

    }

    private Set<String> getKeys(Map<String, Integer> northLanePeriod, Map<String, Integer> southLanePeriod) {
        Set<String> keys = new LinkedHashSet<>(northLanePeriod.keySet());
        PeriodUtils.PERIOD_OF_FIFTEEN_MINUTES_MILLS.keySet().stream().filter(period -> (northLanePeriod.containsKey(period) && !keys.contains(period)) || (southLanePeriod.containsKey(period) && !keys.contains(period))).forEach(keys::add);
        return keys;

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


    /**
     * Based on the number of used lanes (where at least one car has passed),
     * an Integer value representing the required division to get the average of cars is returned.
     */
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
