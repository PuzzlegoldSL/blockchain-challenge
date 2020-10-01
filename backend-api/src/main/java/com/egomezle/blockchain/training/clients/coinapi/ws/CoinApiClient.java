package com.egomezle.blockchain.training.clients.coinapi.ws;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class CoinApiClient {

    public static final String URI_EXCHANGE_RATES = "/exchangerate/%s/%s?time=%s";


    private static final MediaType CONTENT_TYPE = MediaType.APPLICATION_JSON;
    private static final String API_KEY = "F3A6517F-9950-4258-84EC-61F84FF9F004";
    private static final String BASE_PATH = "https://rest.coinapi.io/v1/";
    private static final String KEY_QUERY_PARAM = "&apikey="+ API_KEY;


    public <R, T> ResponseEntity<T> doRequest(final String uri, final HttpMethod method,
                                                         final  R request, Class<T> responseClassType){
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(CONTENT_TYPE);
        HttpEntity<R> rqEntity = new HttpEntity<>(request, headers);

        final RestTemplate restTemplate = new RestTemplate();
        final String fullUrl = BASE_PATH + uri + KEY_QUERY_PARAM;

        return restTemplate.exchange(fullUrl, method, rqEntity, responseClassType);
    }
}
