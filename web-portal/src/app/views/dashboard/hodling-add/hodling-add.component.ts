import { Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import * as moment from 'moment';

@Component({
  selector: 'app-hodling-add',
  templateUrl: './hodling-add.component.html',
  styleUrls: ['./hodling-add.component.css']
})
export class HodlingAddComponent implements OnInit {
  @ViewChild('closeModalBtn') closeModalBtn:ElementRef;
  @ViewChild('closeEditModalBtn') closeEditModalBtn:ElementRef;
  @Input()
  public availableCryptosMetadata: any[];
  @Input()
  public hodlings: any[];
  @Output() public hodlingAdded: EventEmitter<string> = new EventEmitter<string>();


  public addHodlingForm: FormGroup;
  public editHodlingForm: FormGroup;

  editingHodling: any;
  editingCrypto: any;

  constructor() { }

  ngOnInit() {
    this.addHodlingForm = new FormGroup({
      inCrypto: new FormControl('', Validators.required),
      inQty: new FormControl('', [Validators.required, Validators.pattern('\\d+([.]\\d+)?'), Validators.min(0)]),
      inDate: new FormControl('', Validators.required),
      inTime: new FormControl('', Validators.required)
    });

    this.editHodlingForm = new FormGroup({
      inCrypto: new FormControl('', Validators.required),
      inQty: new FormControl('', [Validators.required, Validators.pattern('\\d+([.]\\d+)?'), Validators.min(0)]),
      inDate: new FormControl('', Validators.required),
      inTime: new FormControl('', Validators.required)
    });
  }

  
  onSubmit(event: any){
    let newHodling:any =  {};

    let cryptoSymbol: any = this.addHodlingForm.get('inCrypto').value;
    let cryptoIdx = this.availableCryptosMetadata.findIndex((e => e.symbol === cryptoSymbol));

    let cryptoName: any = this.availableCryptosMetadata[cryptoIdx].crypto;
    let cryptoLogo: any = this.availableCryptosMetadata[cryptoIdx].logo;
    let date: Date = new Date(this.addHodlingForm.get('inDate').value + ' '  + this.addHodlingForm.get('inTime').value);
    let fmtDate = moment(date).format();

    newHodling.crypto = cryptoName;
    newHodling.symbol = cryptoSymbol;
    newHodling.quantity = this.addHodlingForm.get('inQty').value;
    newHodling.initDate = fmtDate;
    newHodling.logo = cryptoLogo;

    this.hodlings.push(newHodling);
    this.hodlingAdded.emit(newHodling);
    this.addHodlingForm.reset();
    this.availableCryptosMetadata[cryptoIdx].isActive = false
    this.closeModalBtn.nativeElement.click();

    console.info(this.hodlings)
  }

  deleteHodling(symbol: string){
    var hodlingIdx = this.findHodlingByCrypto(symbol);
    var cryptoIdx = this.availableCryptosMetadata.findIndex((e => e.symbol === symbol));
    this.hodlings.splice(hodlingIdx, 1)
    this.availableCryptosMetadata[cryptoIdx].isActive = true;
  }

  onEditClick(symbol: string){
    this.editingHodling = this.findHodlingByCrypto(symbol);
    this.editingCrypto = this.hodlings[this.editingHodling].crypto
  }

  editHodling(){
    let oldCrypto = this.hodlings[this.editingHodling].symbol;
    let oldCryptoIdx = this.availableCryptosMetadata.findIndex((e => e.symbol === oldCrypto));

    let cryptoSymbol: any = this.editHodlingForm.get('inCrypto').value;
    let cryptoIdx = this.availableCryptosMetadata.findIndex((e => e.symbol === cryptoSymbol));

    let cryptoName: any = this.availableCryptosMetadata[cryptoIdx].crypto;
    let cryptoLogo: any = this.availableCryptosMetadata[cryptoIdx].logo;
    let date: Date = new Date(this.editHodlingForm.get('inDate').value + ' '  + this.editHodlingForm.get('inTime').value);
    let fmtDate = moment(date).format();

    this.hodlings[this.editingHodling].crypto = cryptoName;
    this.hodlings[this.editingHodling].symbol = cryptoSymbol;
    this.hodlings[this.editingHodling].quantity = this.editHodlingForm.get('inQty').value;
    this.hodlings[this.editingHodling].initDate = fmtDate;
    this.hodlings[this.editingHodling].logo = cryptoLogo;

    this.hodlingAdded.emit(this.hodlings[this.editingHodling]);
    this.editHodlingForm.reset();
    this.availableCryptosMetadata[cryptoIdx].isActive = false
    this.availableCryptosMetadata[oldCryptoIdx].isActive = true
    this.closeEditModalBtn.nativeElement.click();
  }

  findHodlingByCrypto(symbol: string){
    return this.hodlings.findIndex((e => e.symbol === symbol))
  }

}
