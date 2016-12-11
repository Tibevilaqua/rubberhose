package com.rubberhose.infrastructure.cross;

import com.rubberhose.endpoint.cross.CrossBroadStatisticDTO;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by root on 12/12/16.
 */
@Component
public class CrossCache {

    private Map<DayOfWeek, CrossBroadStatisticDTO> cachedDailyStatistics = new HashMap<>();;
    private CrossBroadStatisticDTO cachedGeneralStatistics;


    public int numberOfdaysWithCrosses(){
        return cachedDailyStatistics.keySet().size();
    }

    public CrossBroadStatisticDTO getCachedStatistics(DayOfWeek dayOfWeek) {
        return Objects.isNull(dayOfWeek) ?  cachedGeneralStatistics :  cachedDailyStatistics.get(dayOfWeek);
    }

    public void setCachedStatistics(CrossBroadStatisticDTO cachedStatistics) {
        this.cachedGeneralStatistics = cachedStatistics;
    }

    public void setCachedStatistics(DayOfWeek dayOfWeek, CrossBroadStatisticDTO cachedStatistics) {
        this.cachedDailyStatistics.put(dayOfWeek,cachedStatistics);
    }

}
