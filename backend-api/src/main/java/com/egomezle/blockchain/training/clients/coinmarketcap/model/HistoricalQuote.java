package com.egomezle.blockchain.training.clients.coinmarketcap.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HistoricalQuote {
    private ZonedDateTime timestamp;
    private Map<String, QuoteCurr> quote;

}
