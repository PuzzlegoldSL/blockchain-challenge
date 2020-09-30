import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {CRYPTOCURRENCY_API_URL} from '../utils/constants';

@Injectable({
    providedIn: 'root'
})
export class CryptoHodlingBalanceService {

    constructor(private http: HttpClient) { }

    public async totalBalance(bodyBalance: any, targetCurrency: any) {
        return await this.http.post(`http://localhost:8080/cryptocurrency/totalBalance?targetCurrency=${targetCurrency}`, bodyBalance).toPromise() as any;
    }

    public async currentTotalReturn(hodlingInitInfo, targetCurrency: string) {
        return await this.http.post(`${CRYPTOCURRENCY_API_URL}currentTotalReturn?targetCurrency=${targetCurrency}`, hodlingInitInfo).toPromise() as any;
    }

    public async currentTotalReturnPercentage(hodlingInitInfo, targetCurrency: string) {
        return await this.http
            .post(`${CRYPTOCURRENCY_API_URL}currentTotalReturnPercentage?targetCurrency=${targetCurrency}`, hodlingInitInfo)
            .toPromise() as any;
    }
}
