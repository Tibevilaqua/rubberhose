package com.rubberhose.endpoint.cross;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by root on 13/12/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PeriodDTO {


    private String period;
    private Integer numberOfCars;
    private Integer distanceInMeters;


    public String getPeriod() {
        return period;
    }

    public Integer getNumberOfCars() {
        return numberOfCars;
    }

    public Integer getDistanceInMeters() {
        return distanceInMeters;
    }

    private PeriodDTO() {
    }


    private PeriodDTO(String period, Integer numberOfCars, Integer distanceInMeters) {
        this.period = period;
        this.numberOfCars = numberOfCars;
        this.distanceInMeters = distanceInMeters;
    }

    @Override
    public String toString() {
        return "PeriodDTO{" +
                "period='" + period + '\'' +
                ", numberOfCars=" + numberOfCars +
                ", distanceInMeters=" + distanceInMeters +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PeriodDTO periodDTO = (PeriodDTO) o;

        if (period != null ? !period.equals(periodDTO.period) : periodDTO.period != null) return false;
        if (numberOfCars != null ? !numberOfCars.equals(periodDTO.numberOfCars) : periodDTO.numberOfCars != null)
            return false;
        return distanceInMeters != null ? distanceInMeters.equals(periodDTO.distanceInMeters) : periodDTO.distanceInMeters == null;

    }

    @Override
    public int hashCode() {
        int result = period != null ? period.hashCode() : 0;
        result = 31 * result + (numberOfCars != null ? numberOfCars.hashCode() : 0);
        result = 31 * result + (distanceInMeters != null ? distanceInMeters.hashCode() : 0);
        return result;
    }


    public static class PeriodDTOBuilder {
        private String period;
        private Integer numberOfCars;
        private Integer distanceInMeters;

        public PeriodDTOBuilder setPeriod(String period) {
            this.period = period;
            return this;
        }

        public PeriodDTOBuilder setNumberOfCars(Integer numberOfCars) {
            this.numberOfCars = numberOfCars;
            return this;
        }

        public PeriodDTOBuilder setDistanceInMeters(Integer distanceInMeters) {
            this.distanceInMeters = distanceInMeters;
            return this;
        }

        public String getPeriod() {
            return period;
        }

        public Integer getNumberOfCars() {
            return numberOfCars;
        }

        public Integer getDistanceInMeters() {
            return distanceInMeters;
        }

        public PeriodDTO createPeriodDTO() {
            return new PeriodDTO(period, numberOfCars, distanceInMeters);
        }
    }

}
