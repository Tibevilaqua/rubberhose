package com.rubberhose.repository;

import com.rubberhose.endpoint.cross.CrossBroadStatisticDTO;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.*;

/**
 * Created by root on 08/12/16.
 */
@Repository
public class CrossRepository {

    //Simulating MongoDB database (cross collection)
    private Map<DayOfWeek, Set<String>> crossCollection = new HashMap<>();

    {
        for(DayOfWeek day : DayOfWeek.values()){
            crossCollection.put(day,new HashSet<>());
        }
    }

    public void save(DayOfWeek dayOfTheWeek, List<String> crosses){
        crossCollection.get(dayOfTheWeek).addAll(crosses);
    }


    public List<String> getCrossCollection() {
        return getCrossCollection(null);
    }

    public List<String> getCrossCollection(DayOfWeek dayOfWeek) {
        List<String> result = new ArrayList<>();

        if(!Optional.ofNullable(dayOfWeek).isPresent()){
            crossCollection.keySet().forEach(eachKey -> result.addAll(crossCollection.get(eachKey)));
        }else{
            result.addAll(crossCollection.get(dayOfWeek));
        }
        return result;
    }


}
