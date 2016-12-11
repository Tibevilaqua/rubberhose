package com.rubberhose.business;

import com.rubberhose.endpoint.cross.CrossBroadStatisticDTO;
import com.rubberhose.endpoint.cross.CrossDTO;
import com.rubberhose.infrastructure.cross.CrossCache;
import com.rubberhose.infrastructure.exception.CustomException;
import com.rubberhose.infrastructure.utils.CrossUtils;
import com.rubberhose.repository.CrossRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.rubberhose.infrastructure.exception.ExceptionEnum.INNACURATE_CROSS_PATTERN;

/**
 * Created by root on 08/12/16.
 */
@Service
public class CrossBusiness {

    @Autowired
    private CrossRepository crossRepository;

    @Autowired
    private CrossCache crossCache;

    @Autowired
    private CrossStatisticsFactory crossStatisticsFactory;




    public CrossBusiness(CrossRepository crossRepository, CrossCache crossCache,CrossStatisticsFactory crossStatisticsFactory) {
        this.crossRepository = crossRepository;
        this.crossCache = crossCache;
        this.crossStatisticsFactory = crossStatisticsFactory;
    }


    public void save(CrossDTO crossDTO){

        if(CrossUtils.isListInaccurate(crossDTO)){
            throw new CustomException(INNACURATE_CROSS_PATTERN);
        }

        crossRepository.save(crossDTO.getDayOfWeek(), crossDTO.getCrosses());
    }

    public CrossBroadStatisticDTO getStatistics(DayOfWeek dayOfWeek){
        return crossCache.getCachedStatistics(dayOfWeek);
    }


    public Optional<CrossBroadStatisticDTO> createStatistics(DayOfWeek dayOfWeek) {


        List<String> crossCollection = crossRepository.getCrossCollection(dayOfWeek);

        if(Objects.nonNull((crossCollection)) && crossCollection.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(crossStatisticsFactory.createStatistics(crossCollection,null));
    }

    public Optional<CrossBroadStatisticDTO> createStatistics() {

        List<String> crossCollection = crossRepository.getCrossCollection();

        if(Objects.nonNull((crossCollection)) && crossCollection.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(crossStatisticsFactory.createStatistics(crossCollection,crossCache.numberOfdaysWithCrosses()));
    }






}
