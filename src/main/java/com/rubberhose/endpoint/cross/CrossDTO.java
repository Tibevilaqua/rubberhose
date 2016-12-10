package com.rubberhose.endpoint.cross;

import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * Created by root on 08/12/16.
 */
public class CrossDTO {

    @NotEmpty(message = "crosses should be sent")
    private List<String> crosses;

    public CrossDTO() {
    }

    public CrossDTO(List<String> crosses) {
        this.crosses = crosses;
    }

    public List<String> getCrosses() {
        return crosses;
    }

}
