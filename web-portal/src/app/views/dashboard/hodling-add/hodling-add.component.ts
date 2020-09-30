import { Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import * as moment from 'moment';
import { Subject } from 'rxjs';

@Component({
  selector: 'app-hodling-add',
  templateUrl: './hodling-add.component.html',
  styleUrls: ['./hodling-add.component.css']
})
export class HodlingAddComponent implements OnInit {
  @ViewChild('closeModalBtn') closeModalBtn:ElementRef;
  @Input()
  public availableCryptosMetadata: any[];
  @Input()
  public hodlings: any[];
  @Output() public hodlingAdded: EventEmitter<string> = new EventEmitter<string>();


  private readonly _onClose: Subject<any> = new Subject<any>();

  public addHodlingForm: FormGroup;

  constructor() { }

  ngOnInit() {
    this.addHodlingForm = new FormGroup({
      inCrypto: new FormControl('', Validators.required),
      inQty: new FormControl('', [Validators.required, Validators.pattern('\\d+([.]\\d+)?'), Validators.min(0)]),
      inDate: new FormControl('', Validators.required),
      inTime: new FormControl('', Validators.required)
    });
  }

  
  onSubmit(event: any){
    let newHodling:any =  {};
    let cryptoSymbol: any = this.addHodlingForm.get('inCrypto').value;
    let cryptoName: any = this.availableCryptosMetadata.filter(crypto => crypto.symbol == cryptoSymbol)[0].crypto;
    let cryptoLogo: any = this.availableCryptosMetadata.filter(crypto => crypto.symbol == cryptoSymbol)[0].logo;

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
    this.closeModalBtn.nativeElement.click();
  }

}
