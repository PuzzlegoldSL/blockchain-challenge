import {Component, OnInit} from '@angular/core';
import {getStyle, hexToRgba} from '@coreui/coreui/dist/js/coreui-utilities';
import {CustomTooltips} from '@coreui/coreui-plugin-chartjs-custom-tooltips';
import {CryptoHodlingDetailsService} from '../../services/crypto-hodling-details.service';
import {CryptoMetadataService} from '../../services/crypto-metadata.service';
import * as moment from 'moment';
import {BodyOutputType, Toast, ToasterService} from 'angular2-toaster';
import {CryptoHodlingBalanceService} from '../../services/crypto-holding-balance.service';

@Component({
    templateUrl: 'dashboard.component.html'
})
export class DashboardComponent implements OnInit {
    private availableCryptos: any[] = ['Bitcoin', 'Ethereum', 'XRP', 'Polkadot', 'Litecoin'];
    cryptos: any[];
    totalBalanceInEur: any;
    totalBalanceInBtc: any;

    hodlings: any[] = [];
    availableCryptosMetadata: any[] = [];
    totalEurosProfit: number;
    totalEurosProfitPercent: number;
    showBalance = false;

    constructor(private cryptoHodlingService: CryptoHodlingDetailsService, private cryptoMetadataService: CryptoMetadataService,
                private toasterService: ToasterService,
                private cryptoHodlingBalanceService: CryptoHodlingBalanceService) {
    }


    ngOnInit(): void {

        this.cryptoMetadataService.getMetadata(this.availableCryptos.toString()).then(result => {
            this.availableCryptosMetadata = result;
            this.availableCryptosMetadata.map((e) => {
                e.isActive = true;
            })
            console.log(result);
        });
        // this.processHodlingDetailResponses([{},{},{},{},{}])
        // this.cryptos = this.hodlings;
    }


    private requestHodlingDetailData() {
        const hodlingInfo = this.buildBodyRequestHodling();
        Promise.all([this.cryptoHodlingService.getYearReturn(hodlingInfo),
            this.cryptoHodlingService.getLastYearReturn(hodlingInfo),
            this.cryptoHodlingService.getCurrentReturn(hodlingInfo),
            this.cryptoHodlingService.getInitialValue(hodlingInfo),
            this.cryptoHodlingService.getCurrentValue({
                quoteCurrency: hodlingInfo.convert,
                cryptos: this.hodlings.map(hodling => hodling.symbol).join(',')
            })
        ])
            .then(hodlingDetailResponse => this.processHodlingDetailResponses(hodlingDetailResponse))
            .catch(err => this.showError(err));
    }

    private processHodlingDetailResponses(hodlingDetailResponse) {
        const firstYearReturnFromResponse = hodlingDetailResponse[0];
        const lastYearReturnFromResponse = hodlingDetailResponse[1];
        const currentReturnFromResponse = hodlingDetailResponse[2];
        const initialValueResponse = hodlingDetailResponse[3];
        const currentValueFromResponse = hodlingDetailResponse[4];
        const hodlingDetailList = this.hodlings.map(hodling => ({
            crypto: hodling.crypto,
            symbol: hodling.symbol,
            initDate: hodling.initDate,
            logo: hodling.logo
        }));
        hodlingDetailList.forEach((hodlingDetail: any) => {
            hodlingDetail.currentValue = currentValueFromResponse[hodlingDetail.symbol] || '-';
            hodlingDetail.initialValue = initialValueResponse[hodlingDetail.symbol] || '-';
            hodlingDetail.currentReturn = currentReturnFromResponse[hodlingDetail.symbol] || '-';
            hodlingDetail.firstYearReturn = firstYearReturnFromResponse[hodlingDetail.symbol] || '-';
            hodlingDetail.lastYearReturn = lastYearReturnFromResponse[hodlingDetail.symbol] || '-';
            hodlingDetail.hodlingDays = moment.duration(moment().diff(moment(moment(hodlingDetail.initDate)))).asDays().toFixed(0);
        });
        this.cryptos = hodlingDetailList;
        console.log('%o', this.cryptos);
    }

    private buildBodyRequestHodlingData(hodling, customDate?: string) {
        return {
            convert: 'EUR',
            hodlingInitInfo: ({
                [hodling.symbol]: {
                    initCoins: hodling.quantity,
                    initDate: customDate || hodling.initDate
                }
            })
        };
    }

    private buildBodyRequestHodling(customDate?: string) {
        return {
            convert: 'EUR',
            hodlingInitInfo: this.hodlings.map(hodling => ({
                    [hodling.symbol]: {
                        initCoins: hodling.quantity,
                        initDate: customDate || hodling.initDate
                    }
                })
            ).reduce((hodlinginfo, hodling) => ({...hodlinginfo, ...hodling}), {})

        };
    }

    private showError(err: any) {
        const toast: Toast = {
            type: 'error',
            title: 'Lo sentimos',
            body: `<span>${err.message || (err.error)}</span>`,
            bodyOutputType: BodyOutputType.TrustedHtml
        };
        this.toasterService.pop(toast);
    }

    async onHodlingAdded() {
        this.showBalance = true;
        this.cryptos = this.hodlings;
        this.requestHodlingDetailData();

        this.showBalance = true;
        const coinBalance = {
            coinBalance: this.hodlings.map(hodlingIt => ({[hodlingIt.symbol]: hodlingIt.quantity}))
                .reduce((hodlinginfo, hodlingIt) => ({...hodlinginfo, ...hodlingIt}), {})
        };
        // const coinBalance = { coinBalance: { BTC: 1, ETH: 1 } };
        this.totalBalanceInEur = await this.cryptoHodlingBalanceService.totalBalance(coinBalance, 'EUR')
            .catch((err: any) => this.showError(err));
        this.totalBalanceInBtc = await this.cryptoHodlingBalanceService.totalBalance(coinBalance, 'BTC')
            .catch((err: any) => this.showError(err));
        const hodlingInfo = this.buildBodyRequestHodling();
        this.totalEurosProfit = await this.cryptoHodlingBalanceService.currentTotalReturn(hodlingInfo, 'EUR')
            .catch((err: any) => this.showError(err));
        this.totalEurosProfitPercent = await this.cryptoHodlingBalanceService.currentTotalReturnPercentage(hodlingInfo, 'EUR')
            .catch((err: any) => this.showError(err));
    }

    get hasHodlings() {
        return this.hodlings && this.hodlings.length > 0;
    }

    clearAllHodling() {
        clearArray(this.hodlings);
        clearArray(this.cryptos);
        this.showBalance = false;
    }
}

export function clearArray(arr: any[]) {
    if (arr) { arr.splice(0, arr.length); }
}
