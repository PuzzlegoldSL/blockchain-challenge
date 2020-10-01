package com.egomezle.blockchain.training.clients.coinapi.service;

import com.egomezle.blockchain.training.clients.coinapi.ws.CoinApiClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
@Slf4j
public class CoinApiService {

    @Autowired
    private CoinApiClient apiClient;


    public Float getCryptoValueOfDate(String crypto, ZonedDateTime date, String targetCurrency) {

        final String uri = String.format(CoinApiClient.URI_EXCHANGE_RATES,
                crypto, targetCurrency, date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ")));
        log.info(uri);
        return ((Double) apiClient.doRequest(uri, HttpMethod.GET, Map.of(), Map.class).getBody().get("rate")).floatValue();
    }
}
