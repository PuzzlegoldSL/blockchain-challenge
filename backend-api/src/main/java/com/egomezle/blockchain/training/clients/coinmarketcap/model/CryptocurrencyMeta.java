package com.egomezle.blockchain.training.clients.coinmarketcap.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CryptocurrencyMeta extends Cryptocurrency{
    private String logo;
}
