import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HodlingAddComponent } from './hodling-add.component';

describe('HodlingAddComponent', () => {
  let component: HodlingAddComponent;
  let fixture: ComponentFixture<HodlingAddComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HodlingAddComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HodlingAddComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
