package com.egomezle.blockchain.training.service;

import com.egomezle.blockchain.training.clients.coinapi.service.CoinApiService;
import com.egomezle.blockchain.training.clients.coinmarketcap.model.*;
import com.egomezle.blockchain.training.clients.coinmarketcap.service.CoinMarketCapService;
import com.egomezle.blockchain.training.model.CountHodlingDays;
import com.egomezle.blockchain.training.model.TotalBalanceRq;
import com.egomezle.blockchain.training.model.CryptosHodlingInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.*;

@Service
public class CryptoService {

    @Autowired
    private CoinMarketCapService coinMarketCapService;
    @Autowired
    private CoinApiService coinApiService;

    public Map<String, String> getCryptoId(final String[] cryptos) {
        final CryptoCurrencyMapRspDTO response = coinMarketCapService.getCryptoSymbol();
        final Cryptocurrency[] currencies = response.getData();
        final Map<String, String> cryptoMapping = new HashMap<>();

        Arrays.stream(currencies)
                .filter(curr -> {
                    for (String c : cryptos) {
                        if (c.equalsIgnoreCase(curr.getName())) {
                            return true;
                        }
                    }
                    return false;
                })
                .forEach(curr -> cryptoMapping.put(curr.getName(), curr.getSymbol()));

        return cryptoMapping;
    }

    public Map<String, Float> getCryptoValue(String[] cryptos, String quoteCurrency) {
        final CryptocurrencyQuoteLatestRspDTO response = coinMarketCapService.getCryptoQuotesLatest(cryptos, quoteCurrency);
        final Map<String, CryptocurrencyPriceData> currencies = response.getData();
        final Map<String, Float> cryptoMapping = new HashMap<>();

        currencies.forEach((k,v) -> cryptoMapping.put(k, v.getQuote().get(quoteCurrency).getPrice()));

        return cryptoMapping;
    }

    public Map<String, Long> countHodlingDays(final CountHodlingDays request) {
        final Map<String, Long> days = new HashMap<>();
        request.getInterval().forEach((k,v) -> days.put(k,v.calculateDiffDays()));
        return  days;
    }

    public Float getTotalBalance(TotalBalanceRq request, String targetCurrency) {
        float balance = 0f;
        final String [] cryptos = request.getCoinBalance().keySet().toArray(new String[0]);
        final Map<String, Float> cryptosValues = getCryptoValue(cryptos, targetCurrency);

        for(Map.Entry<String, Float> entry: request.getCoinBalance().entrySet()){
            balance += entry.getValue() * cryptosValues.get(entry.getKey());
        }

        return balance;

    }

    public Map<String, Float> getYearReturn(CryptosHodlingInfo request, String targetCurrency) {
        final Map<String, Float> cryptosReturn = new HashMap<>();

        for(Map.Entry<String, HodlingInit> entry: request.getHodlingInitInfo().entrySet()){
            ZonedDateTime hodlingInitDate = ZonedDateTime.parse(entry.getValue().getInitDate());

            float cryptoInitValue = getCryptoValueOfDate(entry.getKey(), hodlingInitDate, targetCurrency);
            float cryptoYearValue = getCryptoValueOfDate(entry.getKey(),  hodlingInitDate.plusYears(1), targetCurrency);

            cryptosReturn.put(entry.getKey(),
                    (cryptoYearValue * entry.getValue().getInitCoins()) - (cryptoInitValue * entry.getValue().getInitCoins()));
        }


        return cryptosReturn;
    }


    public Map<String, Float> getLastYearReturn(CryptosHodlingInfo request, String targetCurrency) {
        final Map<String, Float> cryptosReturn = new HashMap<>();

        for(Map.Entry<String, HodlingInit> entry: request.getHodlingInitInfo().entrySet()){
            final ZonedDateTime hodlingInitDate = ZonedDateTime.parse(entry.getValue().getInitDate());
            final ZonedDateTime lastYear = ZonedDateTime.now(hodlingInitDate.getZone()).minusYears(1);

            if(!lastYear.isBefore(hodlingInitDate)){
                float cryptoInitValue = getCryptoValueOfDate(entry.getKey(), hodlingInitDate, targetCurrency);
                float cryptoEndValue = getCryptoValueOfDate(entry.getKey(),  lastYear, targetCurrency);

                cryptosReturn.put(entry.getKey(),
                        (cryptoEndValue * entry.getValue().getInitCoins()) - (cryptoInitValue * entry.getValue().getInitCoins()));
            } else {
                cryptosReturn.put(entry.getKey(), 0f);
            }
        }

        return cryptosReturn;
    }


    public Map<String, Float> getCurrentReturn(CryptosHodlingInfo request, String targetCurrency) {
        final Map<String, Float> cryptosReturn = new HashMap<>();

        for(Map.Entry<String, HodlingInit> entry: request.getHodlingInitInfo().entrySet()){
            final ZonedDateTime hodlingInitDate = ZonedDateTime.parse(entry.getValue().getInitDate());

            float cryptoInitValue = getCryptoValueOfDate(entry.getKey(), hodlingInitDate, targetCurrency);
            float cryptoEndValue = getCryptoValueOfDate(entry.getKey(),  ZonedDateTime.now(hodlingInitDate.getZone()), targetCurrency);

            cryptosReturn.put(entry.getKey(),
                    (cryptoEndValue * entry.getValue().getInitCoins()) - (cryptoInitValue * entry.getValue().getInitCoins()));
        }

        return cryptosReturn;
    }


    public Float getCurrentTotalReturn(CryptosHodlingInfo request, String targetCurrency) {
        float totalReturn = 0f;

        for(Map.Entry<String, HodlingInit> entry: request.getHodlingInitInfo().entrySet()){
            final ZonedDateTime hodlingInitDate = ZonedDateTime.parse(entry.getValue().getInitDate());

            float cryptoInitValue = getCryptoValueOfDate(entry.getKey(), hodlingInitDate, targetCurrency);
            float cryptoEndValue = getCryptoValueOfDate(entry.getKey(),  ZonedDateTime.now(hodlingInitDate.getZone()), targetCurrency);

            float cryptoReturn = (cryptoEndValue * entry.getValue().getInitCoins()) - (cryptoInitValue * entry.getValue().getInitCoins());
            totalReturn += cryptoReturn;
        }

        return totalReturn;
    }

    public Float getCurrentTotalReturnPercentage(CryptosHodlingInfo request, String targetCurrency) {
        float initBalance = 0f;
        float endBalance = 0f;

        for(Map.Entry<String, HodlingInit> entry: request.getHodlingInitInfo().entrySet()){
            final ZonedDateTime hodlingInitDate = ZonedDateTime.parse(entry.getValue().getInitDate());

            float cryptoInitValue = getCryptoValueOfDate(entry.getKey(), hodlingInitDate, targetCurrency);
            float cryptoEndValue = getCryptoValueOfDate(entry.getKey(),  ZonedDateTime.now(hodlingInitDate.getZone()), targetCurrency);

            initBalance += cryptoInitValue * entry.getValue().getInitCoins();
            endBalance += cryptoEndValue * entry.getValue().getInitCoins();
        }

        float hodlingReturn =  endBalance - initBalance;


        return initBalance != 0 ? (hodlingReturn * 100) / initBalance : 0;
    }


    public Map<String, Float> getInitialValue(CryptosHodlingInfo request, String targetCurrency) {
        final Map<String, Float> cryptosReturn = new HashMap<>();

        for(Map.Entry<String, HodlingInit> entry: request.getHodlingInitInfo().entrySet()){
            final ZonedDateTime hodlingInitDate = ZonedDateTime.parse(entry.getValue().getInitDate());
            float cryptoInitValue = getCryptoValueOfDate(entry.getKey(), hodlingInitDate, targetCurrency);

            cryptosReturn.put(entry.getKey(),cryptoInitValue);
        }

        return cryptosReturn;
    }

    private Float getCryptoValueOfDate(String crypto, ZonedDateTime date, String targetCurrency){
        if(ZonedDateTime.now(date.getZone()).isBefore(date)){
            date = ZonedDateTime.now(date.getZone());
        }

        return coinApiService.getCryptoValueOfDate(crypto, date, targetCurrency);
    }

    public List<Map<String, String>> getCryptoMetadata(String[] cryptos) {
        final Map<String, String> mapping = this.getCryptoId(cryptos);
        final CryptoCurrencyMetadataRspDTO metadata = coinMarketCapService.getCryptoMetadata(mapping.values());
        final List<Map<String, String>> response = new ArrayList<>();
        metadata.getData().forEach((k, v) ->
                response.add(Map.of(
                        "crypto", v.getName(),
                        "symbol", v.getSymbol(),
                        "logo", v.getLogo()
                ))
        );
        return response;
    }
}
