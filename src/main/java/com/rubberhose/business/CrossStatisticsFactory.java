package com.rubberhose.business;

import com.rubberhose.endpoint.cross.CrossBroadStatisticDTO;
import com.rubberhose.infrastructure.OccurrencePerDayEnum;
import com.rubberhose.infrastructure.utils.CrossUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
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
     * it is necessary to divide per three to get the accurate number of cars.
     */
    private static final int THREE = 3;



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


            if(Objects.nonNull(numberOfDays)){
                morningCount = dividePer(numberOfDays, morningCount);
                eveningCount = dividePer(numberOfDays, eveningCount);
                hourlyAverageCount = dividePer(numberOfDays, hourlyAverageCount);
                thirtyMinutesAverageCount = dividePer(numberOfDays, thirtyMinutesAverageCount);
                twentyMinutesAverageCount = dividePer(numberOfDays, twentyMinutesAverageCount);
                fifteenMinutesAverageCount = dividePer(numberOfDays, fifteenMinutesAverageCount);
            }


            return  new CrossBroadStatisticDTO(morningCount,eveningCount,hourlyAverageCount,thirtyMinutesAverageCount,twentyMinutesAverageCount,fifteenMinutesAverageCount);

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
