package com.rubberhose.endpoint.cross;

/**
 * Created by root on 13/12/16.
 */
public class PeriodDTO {


    private String period;
    private Integer numberOfCars;


    public String getPeriod() {
        return period;
    }

    public Integer getNumberOfCars() {
        return numberOfCars;
    }

    public PeriodDTO() {
    }

    public PeriodDTO(String period, Integer numberOfCars) {
        this.period = period;
        this.numberOfCars = numberOfCars;
    }

    @Override
    public String toString() {
        return "PeriodDTO{" +
                "period='" + period + '\'' +
                ", numberOfCars=" + numberOfCars +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PeriodDTO that = (PeriodDTO) o;

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
