package com.egomezle.blockchain.training.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CountHodlingDays {

    private Map<String, CountHodlingDaysInterval> interval;

}
