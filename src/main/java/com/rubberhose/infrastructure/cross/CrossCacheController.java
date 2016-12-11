package com.rubberhose.infrastructure.cross;

import com.rubberhose.business.CrossBusiness;
import com.rubberhose.endpoint.cross.CrossBroadStatisticDTO;
import com.rubberhose.repository.CrossRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

/**
 * Created by root on 10/12/16.
 *
 * CrossCache is responsible to maintain the cache of the cars' crosses.
 * The cache is updated every 5 minutes.
 */
@Component
public class CrossCacheController {

//    private static final int FIVE_MINUTES = 300000;
    private static final int FIVE_MINUTES = 30000000;

    @Autowired
    private CrossBusiness crossBusiness;
    @Autowired
    private CrossCache crossCache;


    @Scheduled(fixedRate = FIVE_MINUTES, initialDelay = 60000)
    private void populateStatisticsCache(){
        System.out.println("----- starting to update cross cache -----");

        populateStatisticsDaysOfWeek();
        populateStatisticsWholeWeek();

        System.out.println("----- update finished -----");

    }

    private void populateStatisticsWholeWeek() {
        Optional<CrossBroadStatisticDTO> statistics = crossBusiness.createStatistics();
        statistics.ifPresent(statistic -> crossCache.setCachedStatistics(statistic));
    }

    private void populateStatisticsDaysOfWeek() {
        for(DayOfWeek dayOfWeek : DayOfWeek.values()){

            Optional<CrossBroadStatisticDTO> statistics = crossBusiness.createStatistics(dayOfWeek);
            statistics.ifPresent(statistic -> crossCache.setCachedStatistics(dayOfWeek, statistic));
        }
    }
}
