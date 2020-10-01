package com.egomezle.blockchain.training.clients.coinmarketcap.ws;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class CoinMarketCapAPIClient {

    public static final String URI_CRYPTOCURRENCY_MAP = "cryptocurrency/map";
    public static final String URI_CRYPTOCURRENCY_METADATA = "cryptocurrency/info?symbol=%s";
    public static final String URI_CRYPTOCURRENCY_LISTING_LATEST = "cryptocurrency/listings/latest?convert=EUR&aux=num_market_pairs";
    public static final String URI_CRYPTOCURRENCY_QUOTES_LATEST = "cryptocurrency/quotes/latest?symbol=%s&convert=%s&aux=num_market_pairs";
    public static final String URI_CRYPTOCURRENCY_QUOTES_HISTORICAL = "cryptocurrency/quotes/historical?symbol=%s&convert=%s" +
            "&time_start=%s&time_end=%s&interval=5m";

    private static final String API_KEY_API_HEADER = "X-CMC_PRO_API_KEY";
    private static final MediaType CONTENT_TYPE = MediaType.APPLICATION_JSON;
    private static final String API_KEY = "097eed22-0e9c-4837-b7c7-915a192a4bd1";
    private static final String BASE_PATH = "https://pro-api.coinmarketcap.com/v1/";

    public <R, T> ResponseEntity<T> doRequest(final String uri, final HttpMethod method,
                                                         final  R request, Class<T> responseClassType){
        final HttpHeaders headers = new HttpHeaders();
        headers.set(API_KEY_API_HEADER, API_KEY);
        headers.setContentType(CONTENT_TYPE);
        HttpEntity<R> rqEntity = new HttpEntity<>(request, headers);

        final RestTemplate restTemplate = new RestTemplate();
        final String fullUrl = BASE_PATH + uri;

        return restTemplate.exchange(fullUrl, method, rqEntity, responseClassType);
    }
}
