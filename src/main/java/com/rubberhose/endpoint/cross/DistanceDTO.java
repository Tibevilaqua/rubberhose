package com.rubberhose.endpoint.cross;

import java.util.List;

/**
 * Created by root on 15/12/16.
 */
public class DistanceDTO {

    private Integer closestDistanceInMeters;
    private String timePeriod;
    private List<PeriodDTO> periods;


    public DistanceDTO() {
    }

    public DistanceDTO(Integer closestDistanceInMeters, String timePeriod, List<PeriodDTO> periods) {
        this.closestDistanceInMeters = closestDistanceInMeters;
        this.timePeriod = timePeriod;
        this.periods = periods;
    }

    public Integer getClosestDistanceInMeters() {
        return closestDistanceInMeters;
    }

    public String getTimePeriod() {
        return timePeriod;
    }

    public List<PeriodDTO> getPeriods() {
        return periods;
    }

    @Override
    public String toString() {
        return "DistanceDTO{" +
                "closestDistanceInMeters=" + closestDistanceInMeters +
                ", timePeriod='" + timePeriod + '\'' +
                ", periods=" + periods +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DistanceDTO that = (DistanceDTO) o;

        if (closestDistanceInMeters != null ? !closestDistanceInMeters.equals(that.closestDistanceInMeters) : that.closestDistanceInMeters != null)
            return false;
        if (timePeriod != null ? !timePeriod.equals(that.timePeriod) : that.timePeriod != null) return false;
        return periods != null ? periods.equals(that.periods) : that.periods == null;

    }

    @Override
    public int hashCode() {
        int result = closestDistanceInMeters != null ? closestDistanceInMeters.hashCode() : 0;
        result = 31 * result + (timePeriod != null ? timePeriod.hashCode() : 0);
        result = 31 * result + (periods != null ? periods.hashCode() : 0);
        return result;
    }
}
