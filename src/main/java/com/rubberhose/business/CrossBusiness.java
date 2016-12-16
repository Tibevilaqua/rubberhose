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
import static com.rubberhose.infrastructure.exception.ExceptionEnum.INVALID_CROSS_PATTERN;
import static com.rubberhose.infrastructure.exception.ExceptionEnum.INVALID_NUMBER_OF_CROSS;

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
        applyBusinessRulesOn(crossDTO);


        crossRepository.save(crossDTO.getDayOfWeek(), crossDTO.getCrosses());
    }

    /**
     * Business rules regarding crosses
     * @param crossDTO
     */
    private void applyBusinessRulesOn(CrossDTO crossDTO) {

        // Apply regex patterns on it
        if(CrossUtils.isListInaccurate(crossDTO)){
            throw new CustomException(INNACURATE_CROSS_PATTERN);
        }

        //If not even number (multiples of 2 or 4)
        if(crossDTO.getCrosses().size() % 2 != 0){
            throw new CustomException(INVALID_NUMBER_OF_CROSS);
        }


        // All data sent must be accurate, If B lane, then 4 lines, if A, then 2.
        //OBS: this validation will prevent mal-formed data to be insert (Ex: Interchanging values of the Lanes A and B at the same time)
        if(CrossUtils.getNumberOfValidCrosses(crossDTO) < crossDTO.getCrosses().size()){
            throw new CustomException(INVALID_CROSS_PATTERN);
        }
    }

    public CrossBroadStatisticDTO getStatistics(DayOfWeek dayOfWeek){
        return crossCache.getCachedStatistics(dayOfWeek);
    }


    public Optional<CrossBroadStatisticDTO> createStatistics(DayOfWeek dayOfWeek) {

        List<String> crossCollection = crossRepository.getCrossCollection(dayOfWeek);

        if(Objects.nonNull((crossCollection)) && crossCollection.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(crossStatisticsFactory.createStatistics(crossCollection));
    }

    public Optional<CrossBroadStatisticDTO> createStatistics() {

        List<String> crossCollection = crossRepository.getCrossCollection();

        if(Objects.nonNull((crossCollection)) && crossCollection.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(crossStatisticsFactory.createStatistics(crossCollection,crossCache.numberOfdaysWithCrosses()));
    }






}
