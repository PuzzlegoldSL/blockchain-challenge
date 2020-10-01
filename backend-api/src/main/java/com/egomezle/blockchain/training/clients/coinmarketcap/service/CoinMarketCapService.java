package com.egomezle.blockchain.training.clients.coinmarketcap.service;

import com.egomezle.blockchain.training.clients.coinmarketcap.model.*;
import com.egomezle.blockchain.training.clients.coinmarketcap.ws.CoinMarketCapAPIClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

@Service
@Slf4j
public class CoinMarketCapService {

    @Autowired
    private CoinMarketCapAPIClient apiClient;

    public CryptoCurrencyMapRspDTO getCryptoSymbol(){
        return apiClient.doRequest(CoinMarketCapAPIClient.URI_CRYPTOCURRENCY_MAP, HttpMethod.GET, Map.of(), CryptoCurrencyMapRspDTO.class).getBody();
    }

    public CryptoCurrencyMetadataRspDTO getCryptoMetadata(final Collection<String> cryptos){
        final StringBuilder cryptosStr = new StringBuilder();
        cryptos.forEach(crypto -> cryptosStr.append(crypto).append(","));
        final String uri = String.format(CoinMarketCapAPIClient.URI_CRYPTOCURRENCY_METADATA, StringUtils.removeEnd(cryptosStr.toString(),","));
        return apiClient.doRequest(uri, HttpMethod.GET, Map.of(), CryptoCurrencyMetadataRspDTO.class).getBody();
    }

    public CryptocurrencyListingLatestRspDTO getCryptoListingLatest() {
        return apiClient.doRequest(CoinMarketCapAPIClient.URI_CRYPTOCURRENCY_LISTING_LATEST, HttpMethod.GET, Map.of(), CryptocurrencyListingLatestRspDTO.class).getBody();
    }
    public CryptocurrencyQuoteLatestRspDTO getCryptoQuotesLatest(final String [] cryptos, final String quoteCurr) {
        final StringBuilder cryptosStr = new StringBuilder();
        Arrays.stream(cryptos).forEach(crypto -> cryptosStr.append(crypto).append(","));
        final String uri = String.format(CoinMarketCapAPIClient.URI_CRYPTOCURRENCY_QUOTES_LATEST, StringUtils.removeEnd(cryptosStr.toString(),","), quoteCurr);
        return apiClient.doRequest(uri, HttpMethod.GET, Map.of(), CryptocurrencyQuoteLatestRspDTO.class).getBody();
    }
}
