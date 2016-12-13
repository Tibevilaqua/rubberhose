package com.rubberhose.endpoint.cross;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by root on 13/12/16.
 */
public class PeakPeriodDTO {


    private String period;
    private Integer numberOfCars;

    public String getPeriod() {
        return period;
    }

    public Integer getNumberOfCars() {
        return numberOfCars;
    }

    public PeakPeriodDTO() {
    }

    public PeakPeriodDTO(String period, Integer numberOfCars) {
        this.period = period;
        this.numberOfCars = numberOfCars;
    }

    @Override
    public String toString() {
        return "PeakPeriodDTO{" +
                "period='" + period + '\'' +
                ", numberOfCars=" + numberOfCars +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PeakPeriodDTO that = (PeakPeriodDTO) o;

        if (period != null ? !period.equals(that.period) : that.period != null) return false;
        return numberOfCars != null ? numberOfCars.equals(that.numberOfCars) : that.numberOfCars == null;

    }

    @Override
    public int hashCode() {
        int result = period != null ? period.hashCode() : 0;
        result = 31 * result + (numberOfCars != null ? numberOfCars.hashCode() : 0);
        return result;
    }
}
