package com.rubberhose.endpoint.cross;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.function.BiFunction;

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
    private Integer averageSpeed;
    private TrafficDTO traffic;
    private DistanceDTO distanceDTO;



    public CrossBroadStatisticDTO() {
    }

    private CrossBroadStatisticDTO(Integer morning, Integer evening, Integer hourly, Integer everyThirtyMinutes, Integer everyTwentyMinutes, Integer everyFifteenMinutes, TrafficDTO traffic, Integer averageSpeed, DistanceDTO distanceDTO) {
        this.morning = morning;
        this.evening = evening;
        this.hourly = hourly;
        this.everyThirtyMinutes = everyThirtyMinutes;
        this.everyTwentyMinutes = everyTwentyMinutes;
        this.everyFifteenMinutes = everyFifteenMinutes;
        this.averageSpeed = averageSpeed;
        this.traffic = traffic;
        this.distanceDTO = distanceDTO;
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

    public DistanceDTO getDistanceDTO() {
        return distanceDTO;
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
        if (everyFifteenMinutes != null ? !everyFifteenMinutes.equals(that.everyFifteenMinutes) : that.everyFifteenMinutes != null)
            return false;
        if (averageSpeed != null ? !averageSpeed.equals(that.averageSpeed) : that.averageSpeed != null) return false;
        if (traffic != null ? !traffic.equals(that.traffic) : that.traffic != null) return false;
        return distanceDTO != null ? distanceDTO.equals(that.distanceDTO) : that.distanceDTO == null;

    }

    @Override
    public int hashCode() {
        int result = morning != null ? morning.hashCode() : 0;
        result = 31 * result + (evening != null ? evening.hashCode() : 0);
        result = 31 * result + (hourly != null ? hourly.hashCode() : 0);
        result = 31 * result + (everyThirtyMinutes != null ? everyThirtyMinutes.hashCode() : 0);
        result = 31 * result + (everyTwentyMinutes != null ? everyTwentyMinutes.hashCode() : 0);
        result = 31 * result + (everyFifteenMinutes != null ? everyFifteenMinutes.hashCode() : 0);
        result = 31 * result + (averageSpeed != null ? averageSpeed.hashCode() : 0);
        result = 31 * result + (traffic != null ? traffic.hashCode() : 0);
        result = 31 * result + (distanceDTO != null ? distanceDTO.hashCode() : 0);
        return result;
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
                ", averageSpeed=" + averageSpeed +
                ", traffic=" + traffic +
                ", distanceDTO=" + distanceDTO +
                '}';
    }


    public static class CrossBroadStatisticDTOBuilder {
        private Integer morning;
        private Integer evening;
        private Integer hourly;
        private Integer everyThirtyMinutes;
        private Integer everyTwentyMinutes;
        private Integer everyFifteenMinutes;
        private TrafficDTO.TrafficDTOBuilder traffic;
        private Integer averageSpeed;
        private DistanceDTO distanceDTO;

        public CrossBroadStatisticDTOBuilder setMorning(Integer morning) {
            this.morning = morning;
            return this;
        }

        public CrossBroadStatisticDTOBuilder setEvening(Integer evening) {
            this.evening = evening;
            return this;
        }

        public CrossBroadStatisticDTOBuilder setHourly(Integer hourly) {
            this.hourly = hourly;
            return this;
        }

        public CrossBroadStatisticDTOBuilder setEveryThirtyMinutes(Integer everyThirtyMinutes) {
            this.everyThirtyMinutes = everyThirtyMinutes;
            return this;
        }

        public CrossBroadStatisticDTOBuilder setEveryTwentyMinutes(Integer everyTwentyMinutes) {
            this.everyTwentyMinutes = everyTwentyMinutes;
            return this;
        }

        public CrossBroadStatisticDTOBuilder setEveryFifteenMinutes(Integer everyFifteenMinutes) {
            this.everyFifteenMinutes = everyFifteenMinutes;
            return this;
        }

        public CrossBroadStatisticDTOBuilder setTraffic(TrafficDTO.TrafficDTOBuilder traffic) {
            this.traffic = traffic;
            return this;
        }

        public CrossBroadStatisticDTOBuilder setAverageSpeed(Integer averageSpeed) {
            this.averageSpeed = averageSpeed;
            return this;
        }

        public CrossBroadStatisticDTOBuilder setDistanceDTO(DistanceDTO distanceDTO) {
            this.distanceDTO = distanceDTO;
            return this;
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

        public TrafficDTO.TrafficDTOBuilder getTraffic() {
            return traffic;
        }

        public Integer getAverageSpeed() {
            return averageSpeed;
        }

        public DistanceDTO getDistanceDTO() {
            return distanceDTO;
        }

        public CrossBroadStatisticDTO createCrossBroadStatisticDTO() {
            return new CrossBroadStatisticDTO(morning, evening, hourly, everyThirtyMinutes, everyTwentyMinutes, everyFifteenMinutes, traffic.createTrafficDTO(), averageSpeed, distanceDTO);
        }
    }


}
