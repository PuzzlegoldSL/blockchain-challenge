package com.egomezle.blockchain.training.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TotalBalanceRq {
    private Map<String, Float> coinBalance;
}
