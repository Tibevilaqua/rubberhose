package com.rubberhose.infrastructure.cross;

import com.rubberhose.business.CrossBusiness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by root on 10/12/16.
 *
 * CrossCache is responsible to maintain the cache of the cars' crosses.
 * The cache is updated every 5 minutes.
 */
@Component
public class CrossCache {

//    private static final int FIVE_MINUTES = 300000;
    private static final int FIVE_MINUTES = 6000;

    @Autowired
    private CrossBusiness crossBusiness;


    @Scheduled(fixedRate = FIVE_MINUTES, initialDelay = 6000)
    private void populateStatisticsCache(){
        System.out.println("----- starting to update cross cache -----");

        if(crossBusiness.createStatistics()){
            System.out.println("----- update finished -----");
        }else{
            System.out.println("----- Empty database, nothing was updated... -----");
        }
    }
}
