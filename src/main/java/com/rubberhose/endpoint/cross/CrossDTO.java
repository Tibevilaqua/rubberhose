package com.rubberhose.endpoint.cross;


import org.hibernate.validator.constraints.NotEmpty;


import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.util.List;

/**
 * Created by root on 08/12/16.
 */
public class CrossDTO {


    @NotNull(message = "Day of the week must be sent along with the crosses")
    private DayOfWeek dayOfWeek;

    @NotEmpty(message = "Crosses should be sent")
    private List<String> crosses;

    public CrossDTO() {
    }

    public CrossDTO(DayOfWeek dayOfWeek, List<String> crosses) {
        this.dayOfWeek = dayOfWeek;
        this.crosses = crosses;
    }

    public CrossDTO(List<String> crosses) {
        this.crosses = crosses;
    }

    public List<String> getCrosses() {
        return crosses;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    @Override
    public String toString() {
        return "CrossDTO{" +
                "dayOfWeek=" + dayOfWeek +
                ", crosses=" + crosses +
                '}';
    }
}
