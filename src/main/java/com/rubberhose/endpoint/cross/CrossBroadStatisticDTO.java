package com.rubberhose.endpoint.cross;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by root on 09/12/16.
 */
public class CrossBroadStatisticDTO {

    private Integer morning;
    private Integer evening;
    private Integer hourly;
    private Integer everyThirtyMinutes;
    private Integer everyTwentyMinutes;
    private Integer everyFifteenMinutes;

    private TrafficDTO traffic;
    private Integer averageSpeed;


    public CrossBroadStatisticDTO() {
    }

    public CrossBroadStatisticDTO(Integer morning, Integer evening, Integer hourly, Integer everyThirtyMinutes, Integer everyTwentyMinutes, Integer everyFifteenMinutes, TrafficDTO traffic, Integer averageSpeed) {
        this.morning = morning;
        this.evening = evening;
        this.hourly = hourly;
        this.everyThirtyMinutes = everyThirtyMinutes;
        this.everyTwentyMinutes = everyTwentyMinutes;
        this.everyFifteenMinutes = everyFifteenMinutes;
        this.traffic = traffic;
        this.averageSpeed = averageSpeed;
    }

    public Integer getAverageSpeed() {
        return averageSpeed;
    }

    public Integer getMorning() {
        return morning;
    }

    public Integer getEvening() {
        return evening;
    }

    public Integer getHourly() {
        return hourly;
    }

    public Integer getEveryThirtyMinutes() {
        return everyThirtyMinutes;
    }

    public Integer getEveryTwentyMinutes() {
        return everyTwentyMinutes;
    }

    public Integer getEveryFifteenMinutes() {
        return everyFifteenMinutes;
    }

    public TrafficDTO getTraffic() {
        return traffic;
    }

    @Override
    public String toString() {
        return "CrossBroadStatisticDTO{" +
                "morning=" + morning +
                ", evening=" + evening +
                ", hourly=" + hourly +
                ", everyThirtyMinutes=" + everyThirtyMinutes +
                ", everyTwentyMinutes=" + everyTwentyMinutes +
                ", everyFifteenMinutes=" + everyFifteenMinutes +
                ", traffic=" + traffic +
                ", averageSpeed=" + averageSpeed +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CrossBroadStatisticDTO that = (CrossBroadStatisticDTO) o;

        if (!morning.equals(that.morning)) return false;
        if (!evening.equals(that.evening)) return false;
        if (!hourly.equals(that.hourly)) return false;
        if (!everyThirtyMinutes.equals(that.everyThirtyMinutes)) return false;
        if (!everyTwentyMinutes.equals(that.everyTwentyMinutes)) return false;
        if (!everyFifteenMinutes.equals(that.everyFifteenMinutes)) return false;
        return averageSpeed.equals(that.averageSpeed);

    }

    @Override
    public int hashCode() {
        int result = morning.hashCode();
        result = 31 * result + evening.hashCode();
        result = 31 * result + hourly.hashCode();
        result = 31 * result + everyThirtyMinutes.hashCode();
        result = 31 * result + everyTwentyMinutes.hashCode();
        result = 31 * result + everyFifteenMinutes.hashCode();
        result = 31 * result + averageSpeed.hashCode();
        return result;
    }
}
