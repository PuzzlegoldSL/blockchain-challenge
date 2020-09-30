import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {CRYPTOCURRENCY_API_URL} from '../utils/constants';

@Injectable({
    providedIn: 'root'
})
export class CryptoHodlingDetailsService {

    constructor(private http: HttpClient) { }

    public sample() {
        this.http.get('https://google.com').toPromise().then(ok => console.log('ok'));
    }

    public getYearReturn(hodlingInfo: any) {
         return this.http.post(`${CRYPTOCURRENCY_API_URL}yearReturn?targetCurrency=${hodlingInfo.convert}`, hodlingInfo).toPromise();
        // return Promise.resolve({
        //     "BTC": 264025.7,
        //     "ETH": 188.2857
        // });
    }
    public getLastYearReturn(hodlingInfo: any) {
         return this.http.post(`${CRYPTOCURRENCY_API_URL}lastYearReturn?targetCurrency=${hodlingInfo.convert}`, hodlingInfo).toPromise();
        // return Promise.resolve({
        //     "BTC": 264025.7,
        //     "ETH": 188.2857
        // });
    }
    public getCurrentReturn(hodlingInfo: any) {
         return this.http.post(`${CRYPTOCURRENCY_API_URL}currentReturn?targetCurrency=${hodlingInfo.convert}`, hodlingInfo).toPromise();
        // return Promise.resolve({
        //     "BTC": -122.23 ,
        //     "ETH": 18.2857
        // });
    }

    getCurrentValue(cryptoConvertInfo: any) {
         return this.http.get(`${CRYPTOCURRENCY_API_URL}value?cryptos=${cryptoConvertInfo.cryptos}&quoteCurrency=${cryptoConvertInfo.quoteCurrency}`).toPromise();
        // return Promise.resolve({
        //     "BTC": 8708.442,
        //     "XRP": 0.23181863,
        //     "DOT": 108.9091,
        //     "ETH": 153.68597,
        //     "LTC": 58.626076
        // });
    }

    getInitialValue(hodlingInfo: any) {
        return this.http.post(`${CRYPTOCURRENCY_API_URL}initialValue?targetCurrency=${hodlingInfo.convert}`, hodlingInfo).toPromise();
        // return Promise.resolve({
        //     "XRP": 0.0023 ,
        //     "ETH": 18.2857
        // });
    }
}
