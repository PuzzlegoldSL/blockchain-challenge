package com.egomezle.blockchain.training.controller;

import com.egomezle.blockchain.training.model.CountHodlingDays;
import com.egomezle.blockchain.training.model.TotalBalanceRq;
import com.egomezle.blockchain.training.model.CryptosHodlingInfo;
import com.egomezle.blockchain.training.service.CryptoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cryptocurrency")
public class CryptoController {

    @Autowired
    private CryptoService cryptoService;

    @GetMapping("/mapping")
    public ResponseEntity<Map<String, String>> getCryptocurrencyId(@RequestParam String[] cryptos){

        Map<String, String> mapping = cryptoService.getCryptoId(cryptos);

        return new ResponseEntity<>(mapping, HttpStatus.OK);
    }

    @GetMapping("/value")
    public ResponseEntity<Map<String, Float>> getCryptocurrencyValue(@RequestParam String[] cryptos,
                                                                   @RequestParam String quoteCurrency){

        Map<String, Float> values = cryptoService.getCryptoValue(cryptos, quoteCurrency);

        return new ResponseEntity<>(values, HttpStatus.OK);
    }

    @PostMapping("/hodlingDays")
    public ResponseEntity<Map<String, Long>> countHodlingDays(@RequestBody CountHodlingDays request){
        Map<String, Long> days = cryptoService.countHodlingDays(request);

        return new ResponseEntity<>(days, HttpStatus.OK);

    }

    @PostMapping("/totalBalance")
    public ResponseEntity<Float> getTotalBalance(@RequestBody TotalBalanceRq request,
                                                 @RequestParam String targetCurrency){
        Float balance = cryptoService.getTotalBalance(request, targetCurrency);

        return new ResponseEntity<>(balance, HttpStatus.OK);

    }

    @PostMapping("/yearReturn")
    public ResponseEntity<Map<String, Float>> getYearReturn(@RequestBody CryptosHodlingInfo request,
                                                            @RequestParam String targetCurrency){
        final Map<String, Float> returns = cryptoService.getYearReturn(request, targetCurrency);

        return new ResponseEntity<>(returns, HttpStatus.OK);
    }

    @PostMapping("/lastYearReturn")
    public ResponseEntity<Map<String, Float>> getLastYearReturn(@RequestBody CryptosHodlingInfo request,
                                                                    @RequestParam String targetCurrency){
        final Map<String, Float> returns = cryptoService.getLastYearReturn(request, targetCurrency);

        return new ResponseEntity<>(returns, HttpStatus.OK);
    }

    @PostMapping("/currentReturn")
    public ResponseEntity<Map<String, Float>> getCurrentReturn(@RequestBody CryptosHodlingInfo request,
                                                                @RequestParam String targetCurrency){
        final Map<String, Float> returns = cryptoService.getCurrentReturn(request, targetCurrency);
        return new ResponseEntity<>(returns, HttpStatus.OK);
    }

    @PostMapping("/currentTotalReturn")
    public ResponseEntity<Float> getCurrentTotalReturn(@RequestBody CryptosHodlingInfo request,
                                                               @RequestParam String targetCurrency){
        final Float returns = cryptoService.getCurrentTotalReturn(request, targetCurrency);
        return new ResponseEntity<>(returns, HttpStatus.OK);
    }

    @PostMapping("/currentTotalReturnPercentage")
    public ResponseEntity<Float> getCurrentTotalReturnPercentage(@RequestBody CryptosHodlingInfo request,
                                                               @RequestParam String targetCurrency){
        final Float returns = cryptoService.getCurrentTotalReturnPercentage(request, targetCurrency);
        return new ResponseEntity<>(returns, HttpStatus.OK);
    }

    @PostMapping("/initialValue")
    public ResponseEntity<Map<String, Float>> getInitialValue(@RequestBody CryptosHodlingInfo request,
                                                            @RequestParam String targetCurrency){
        final Map<String, Float> returns = cryptoService.getInitialValue(request, targetCurrency);

        return new ResponseEntity<>(returns, HttpStatus.OK);
    }

    @GetMapping("/metadata")
    public ResponseEntity<List<Map<String, String>>> getMetadata(@RequestParam String[] cryptos){

        List<Map<String, String>> mapping = cryptoService.getCryptoMetadata(cryptos);

        return new ResponseEntity<>(mapping, HttpStatus.OK);
    }
}
