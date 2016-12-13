package com.rubberhose.business;

import com.rubberhose.endpoint.cross.CrossBroadStatisticDTO;
import com.rubberhose.endpoint.cross.PeakPeriodDTO;
import com.rubberhose.infrastructure.MillsEnum;
import com.rubberhose.infrastructure.OccurrencePerDayEnum;
import com.rubberhose.infrastructure.utils.CrossUtils;
import com.rubberhose.infrastructure.utils.PeriodUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.rubberhose.infrastructure.MillsEnum.END_OF_MORNING;
import static com.rubberhose.infrastructure.utils.CrossUtils.dividePer;

/**
 * Created by root on 12/12/16.
 */
@Component
public class CrossStatisticsFactory {

    /**
     * As far as there are two belts on the street (A & B) and belt B is stretched over both lanes
     * it is necessary to divide by three to get the accurate number of cars.
     */
    private static final int THREE = 3;


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

            List<Integer> crossCollectionsMills = CrossUtils.getMillsFrom(crossCollection);

            Integer morningCount = this.getMorningCount(crossCollectionsMills);
            //If it's not morning, therefore, it's evening
            Integer eveningCount = dividePer(THREE,this.getEveningCount(crossCollectionsMills,morningCount));
            morningCount = dividePer(THREE, morningCount);

            Integer hourlyAverageCount = dividePer(THREE,this.getHourAverageCount(crossCollectionsMills))
                    , thirtyMinutesAverageCount = dividePer(THREE,this.getHalfHourAverageCount(crossCollectionsMills))
                    , twentyMinutesAverageCount = dividePer(THREE,this.getEveryTwentyMinutesAverageCount(crossCollectionsMills))
                    , fifteenMinutesAverageCount = dividePer(THREE,this.getEveryFifteenMinutesAverageCount(crossCollectionsMills));

        PeakPeriodDTO peakPeriodDTO = this.getPeakPeriod(crossCollectionsMills);
        Integer peakPeriodNumberOfCrosses = dividePer(THREE, peakPeriodDTO.getNumberOfCars());

        // If no numberOfDays is sent, therefore, the code considers it and divide based on it,
        // otherwise, it's a particular weekday.
        if(Objects.nonNull(numberOfDays)){
                morningCount = dividePer(numberOfDays, morningCount);
                eveningCount = dividePer(numberOfDays, eveningCount);
                hourlyAverageCount = dividePer(numberOfDays, hourlyAverageCount);
                thirtyMinutesAverageCount = dividePer(numberOfDays, thirtyMinutesAverageCount);
                twentyMinutesAverageCount = dividePer(numberOfDays, twentyMinutesAverageCount);
                fifteenMinutesAverageCount = dividePer(numberOfDays, fifteenMinutesAverageCount);
                peakPeriodNumberOfCrosses = dividePer(numberOfDays, peakPeriodNumberOfCrosses);
            }

        peakPeriodDTO = new PeakPeriodDTO(peakPeriodDTO.getPeriod(),peakPeriodNumberOfCrosses);

        return  new CrossBroadStatisticDTO(morningCount,eveningCount,hourlyAverageCount,thirtyMinutesAverageCount,twentyMinutesAverageCount,fifteenMinutesAverageCount, peakPeriodDTO);

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

    /**
     * Compare how many crosses were captured based on each period of 15 minutes and return the closest period and the number of crosses altogether
     */
    private PeakPeriodDTO getPeakPeriod(List<Integer> crossCollectionsMills) {
        Map<String, Integer> periodOfFifteenMinutesMills = PeriodUtils.PERIOD_OF_FIFTEEN_MINUTES_MILLS;
        String peakPeriod = "";
        int maxNumberOfCrosses = 0;

        //Verifies each period and its crosses
        for(String eachPeriod : periodOfFifteenMinutesMills.keySet()){
            int beginningCurrentPeriodInMills = periodOfFifteenMinutesMills.get(eachPeriod).intValue();
            int limitCurrentPeriodInMills = (beginningCurrentPeriodInMills + MillsEnum.FIFTEEN_MINUTES.value());
            int currentNumberOfCrosses = (int) crossCollectionsMills.stream().sequential().filter(cross -> cross >= beginningCurrentPeriodInMills && cross < limitCurrentPeriodInMills).count();
            if(currentNumberOfCrosses > maxNumberOfCrosses){
                peakPeriod = eachPeriod;
                maxNumberOfCrosses = currentNumberOfCrosses;
            }
        }
        return new PeakPeriodDTO(peakPeriod, maxNumberOfCrosses);

    }

    /**
     * Given a list of crosses and the necessary occurencePerDay, the average is extracted and returned as an Integer
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



}
