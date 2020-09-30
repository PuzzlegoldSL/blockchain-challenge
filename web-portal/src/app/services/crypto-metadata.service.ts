import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
    providedIn: 'root'
})
export class CryptoMetadataService {

    constructor(private http: HttpClient) { }

    public async getMetadata(availableCryptos: string) {
        return await this.http.get(`http://localhost:8080/cryptocurrency/metadata?cryptos=${ availableCryptos }`).toPromise() as any;
    }
}
