package com.rubberhose.business;

import com.rubberhose.endpoint.cross.CrossBroadStatisticDTO;
import com.rubberhose.endpoint.cross.CrossDTO;
import com.rubberhose.infrastructure.OccurrencePerDayEnum;
import com.rubberhose.infrastructure.exception.CustomException;
import com.rubberhose.infrastructure.utils.CrossUtils;
import com.rubberhose.repository.CrossRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.rubberhose.infrastructure.MillsEnum.*;
import static com.rubberhose.infrastructure.exception.ExceptionEnum.INNACURATE_CROSS_PATTERN;

/**
 * Created by root on 08/12/16.
 */
@Service
public class CrossBusiness {

    @Autowired
    private CrossRepository crossRepository;

    public CrossBusiness(CrossRepository crossRepository) {
        this.crossRepository = crossRepository;
    }


    public void save(CrossDTO crossDTO){

        if(CrossUtils.isListInaccurate(crossDTO)){
            throw new CustomException(INNACURATE_CROSS_PATTERN);
        }

        crossRepository.save(crossDTO.getCrosses());
    }

    public CrossBroadStatisticDTO getStatistics(){
        return crossRepository.getCachedStatistics();
    }


    public boolean createStatistics() {

        List<String> crossCollection = crossRepository.getCrossCollection();

        if(Objects.nonNull((crossCollection)) && !crossCollection.isEmpty()) {

            List<Integer> crossCollectionsMills = CrossUtils.getMillsFrom(crossCollection);

            Integer morningCount = this.getMorningCount(crossCollectionsMills);
            //If it's not morning, therefore, it's evening
            Integer eveningCount = crossCollectionsMills.size() - morningCount;

            Integer hourlyAverageCount = this.getPeriodAverage(crossCollectionsMills, OccurrencePerDayEnum.HOURLY);
            Integer thirtyMinutesAverageCount = this.getPeriodAverage(crossCollectionsMills, OccurrencePerDayEnum.EVERY_THIRTY_MINUTES);
            Integer twentyMinutesAverageCount = this.getPeriodAverage(crossCollectionsMills, OccurrencePerDayEnum.EVERY_TWENTY_MINUTES);
            Integer fifteenMinutesAverageCount = this.getPeriodAverage(crossCollectionsMills, OccurrencePerDayEnum.EVERY_FIFTEEN_MINUTES);

            CrossBroadStatisticDTO crossBroadStatisticDTO = new CrossBroadStatisticDTO(dividePerThree(morningCount), dividePerThree(eveningCount), dividePerThree(hourlyAverageCount), dividePerThree(thirtyMinutesAverageCount), dividePerThree(twentyMinutesAverageCount), dividePerThree(fifteenMinutesAverageCount));
            crossRepository.setCachedStatistics(crossBroadStatisticDTO);
            return true;
        }
        return false;
    }

    private Integer dividePerThree(Integer numberOfCrosses){
        return new BigDecimal(numberOfCrosses).divide(new BigDecimal(3),BigDecimal.ROUND_HALF_UP).intValue();
    }

    private Integer getPeriodAverage(List<Integer> crossCollectionsMills, OccurrencePerDayEnum occurrencePerDayEnum) {
        List<Integer> occurrencesOfTheDayInMills = IntStream.rangeClosed(0, occurrencePerDayEnum.getValue()).mapToObj(value -> Integer.valueOf(value * occurrencePerDayEnum.getRespectiveMillsEnum().value())).collect(Collectors.toList());
        List<Integer> countPerOccurrences = IntStream.range(1, occurrencesOfTheDayInMills.size())
                .mapToObj(eachHour -> (int) crossCollectionsMills.stream()
                        .filter(eachCross -> eachCross.compareTo(occurrencesOfTheDayInMills.get(eachHour-1)) > 0
                                             && eachCross.compareTo(occurrencesOfTheDayInMills.get(eachHour)) <= 0)
                        .count())
                .collect(Collectors.toList());

        return new BigDecimal(countPerOccurrences.stream().mapToInt(Integer::intValue).sum()).divide(new BigDecimal(countPerOccurrences.size()),BigDecimal.ROUND_HALF_UP).intValue();
    }

    private Integer getMorningCount(List<Integer> crossCollection) {
        return (int) crossCollection.stream().filter(eachCross -> eachCross.compareTo(END_OF_MORNING.value()) <= 0).count();
    }





}
