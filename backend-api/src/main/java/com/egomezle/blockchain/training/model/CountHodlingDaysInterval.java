package com.egomezle.blockchain.training.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CountHodlingDaysInterval {
    private ZonedDateTime initDate;
    private ZonedDateTime endDate;


    public long calculateDiffDays(){
        return ChronoUnit.DAYS.between(this.initDate, this.endDate);
    }
}
