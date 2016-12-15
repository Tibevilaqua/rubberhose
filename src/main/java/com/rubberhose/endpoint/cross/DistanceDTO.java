package com.rubberhose.endpoint.cross;

/**
 * Created by root on 15/12/16.
 */
public class DistanceDTO {

    private Integer AverageDistanceInMeters;

    public DistanceDTO(Integer averageDistanceInMeters) {
        AverageDistanceInMeters = averageDistanceInMeters;
    }

    public DistanceDTO() {
    }

    public Integer getAverageDistanceInMeters() {
        return AverageDistanceInMeters;
    }


    @Override
    public String toString() {
        return "DistanceDTO{" +
                "AverageDistanceInMeters=" + AverageDistanceInMeters +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DistanceDTO that = (DistanceDTO) o;

        return AverageDistanceInMeters != null ? AverageDistanceInMeters.equals(that.AverageDistanceInMeters) : that.AverageDistanceInMeters == null;

    }

    @Override
    public int hashCode() {
        return AverageDistanceInMeters != null ? AverageDistanceInMeters.hashCode() : 0;
    }
}
