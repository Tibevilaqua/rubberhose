package com.rubberhose.infrastructure.cross;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rubberhose.business.CrossBusiness;
import com.rubberhose.endpoint.cross.CrossBroadStatisticDTO;
import com.rubberhose.endpoint.cross.CrossDTO;
import com.rubberhose.endpoint.cross.CrossEndpoint;
import com.rubberhose.repository.CrossRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by root on 10/12/16.
 *
 * CrossCache is responsible to maintain the cache of the cars' crosses.
 * The cache is updated every 5 minutes.
 *
 * As well as the initial import from files in the resource/data repository
 */
@Component
public class CrossCacheController {

    private static final int FIVE_MINUTES = 300000;
    private static final String DATA_TO_IMPORT ="src/main/resources/data/";
    private static boolean shouldImportFiles = true;

    @Autowired
    private CrossBusiness crossBusiness;
    @Autowired
    private CrossCache crossCache;
    @Autowired
    private CrossEndpoint crossEndpoint;

    /**
     * Import the data from every file inside the /resource/data repository
     * @throws IOException
     */
    @Scheduled(initialDelay = 2000, fixedDelay = Long.MAX_VALUE)
    private void importDataFromFiles() throws IOException {
        System.out.println("----- Importing data from files to the repository -----");

        //Just import once, when the app is started.
        if(shouldImportFiles) {

            Path dataDirectory = Paths.get(DATA_TO_IMPORT);

            //Just in case it does not exist
            Files.createDirectories(dataDirectory);

            List<Path> filesToImport = Files.find(dataDirectory, 1, (path, fileAttributes) -> fileAttributes.isRegularFile()).collect(Collectors.toList());

            for (Path eachFile : filesToImport) {

                CrossDTO crossDTO = new ObjectMapper().readValue(Files.lines(eachFile).collect(Collectors.joining()), CrossDTO.class);
                crossEndpoint.save(crossDTO);
            }

            System.out.println("----- Import finished -----");
            shouldImportFiles = false;

        }
    }


    /**
     * Populates the statistics based on the data in the repository
     * @throws IOException
     */
    @Scheduled(fixedRate = FIVE_MINUTES, initialDelay = 6000)
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
