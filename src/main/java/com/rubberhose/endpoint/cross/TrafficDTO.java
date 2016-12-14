package com.rubberhose.endpoint.cross;

import java.time.Period;
import java.util.List;

/**
 * Created by root on 15/12/16.
 */
public class TrafficDTO {

    private String peak;
    private Integer numberOfCars;

    private List<PeriodDTO> periods;

    public TrafficDTO(String peak, Integer numberOfCars, List<PeriodDTO> periods) {
        this.peak = peak;
        this.numberOfCars = numberOfCars;
        this.periods = periods;
    }

    public TrafficDTO() {
    }

    public String getPeak() {
        return peak;
    }

    public Integer getNumberOfCars() {
        return numberOfCars;
    }

    public List<PeriodDTO> getPeriods() {
        return periods;
    }

    @Override
    public String toString() {
        return "TrafficDTO{" +
                "peak='" + peak + '\'' +
                ", numberOfCars=" + numberOfCars +
                ", periods=" + periods +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TrafficDTO that = (TrafficDTO) o;

        if (peak != null ? !peak.equals(that.peak) : that.peak != null) return false;
        if (numberOfCars != null ? !numberOfCars.equals(that.numberOfCars) : that.numberOfCars != null) return false;
        return periods != null ? periods.equals(that.periods) : that.periods == null;

    }

    @Override
    public int hashCode() {
        int result = peak != null ? peak.hashCode() : 0;
        result = 31 * result + (numberOfCars != null ? numberOfCars.hashCode() : 0);
        result = 31 * result + (periods != null ? periods.hashCode() : 0);
        return result;
    }
}
