package com.egomezle.blockchain.training.model;

import com.egomezle.blockchain.training.clients.coinmarketcap.model.HodlingInit;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CryptosHodlingInfo {
    private Map<String, HodlingInit> hodlingInitInfo;
}
