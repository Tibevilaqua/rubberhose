package com.rubberhose.endpoint.cross;

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


    public CrossBroadStatisticDTO() {
    }

    public CrossBroadStatisticDTO(Integer morning, Integer evening, Integer hourly, Integer everyThirtyMinutes, Integer everyTwentyMinutes, Integer everyFifteenMinutes) {
        this.morning = morning;
        this.evening = evening;
        this.hourly = hourly;
        this.everyThirtyMinutes = everyThirtyMinutes;
        this.everyTwentyMinutes = everyTwentyMinutes;
        this.everyFifteenMinutes = everyFifteenMinutes;
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

    @Override
    public String toString() {
        return "CrossBroadStatisticDTO{" +
                "morning=" + morning +
                ", evening=" + evening +
                ", hourly=" + hourly +
                ", everyThirtyMinutes=" + everyThirtyMinutes +
                ", everyTwentyMinutes=" + everyTwentyMinutes +
                ", everyFifteenMinutes=" + everyFifteenMinutes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CrossBroadStatisticDTO that = (CrossBroadStatisticDTO) o;

        if (morning != null ? !morning.equals(that.morning) : that.morning != null) return false;
        if (evening != null ? !evening.equals(that.evening) : that.evening != null) return false;
        if (hourly != null ? !hourly.equals(that.hourly) : that.hourly != null) return false;
        if (everyThirtyMinutes != null ? !everyThirtyMinutes.equals(that.everyThirtyMinutes) : that.everyThirtyMinutes != null)
            return false;
        if (everyTwentyMinutes != null ? !everyTwentyMinutes.equals(that.everyTwentyMinutes) : that.everyTwentyMinutes != null)
            return false;
        return everyFifteenMinutes != null ? everyFifteenMinutes.equals(that.everyFifteenMinutes) : that.everyFifteenMinutes == null;

    }

    @Override
    public int hashCode() {
        int result = morning != null ? morning.hashCode() : 0;
        result = 31 * result + (evening != null ? evening.hashCode() : 0);
        result = 31 * result + (hourly != null ? hourly.hashCode() : 0);
        result = 31 * result + (everyThirtyMinutes != null ? everyThirtyMinutes.hashCode() : 0);
        result = 31 * result + (everyTwentyMinutes != null ? everyTwentyMinutes.hashCode() : 0);
        result = 31 * result + (everyFifteenMinutes != null ? everyFifteenMinutes.hashCode() : 0);
        return result;
    }
}
