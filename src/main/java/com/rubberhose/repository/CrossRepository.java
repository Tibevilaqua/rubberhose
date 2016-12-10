package com.rubberhose.repository;

import com.rubberhose.endpoint.cross.CrossBroadStatisticDTO;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by root on 08/12/16.
 */
@Repository
public class CrossRepository {

    private CrossBroadStatisticDTO cachedStatistics;


    //Simulating MongoDB database (cross collection)
    private static Set<String> crossCollection = new HashSet<>();

    public boolean save(List<String> crosses){
        return crossCollection.addAll(crosses);
    }

    public List<String> getCrossCollection() {
        return new ArrayList<>(crossCollection);
    }

    public CrossBroadStatisticDTO getCachedStatistics() {
        return cachedStatistics;
    }

    public void setCachedStatistics(CrossBroadStatisticDTO cachedStatistics) {
        this.cachedStatistics = cachedStatistics;
    }
}
