package com.egomezle.blockchain.training.clients.coinmarketcap.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HodlingInit{
    private Float initCoins;
    private String initDate;

}