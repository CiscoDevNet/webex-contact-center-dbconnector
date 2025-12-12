import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OracleComponent } from './oracle.component';

describe('OracleComponent', () => {
  let component: OracleComponent;
  let fixture: ComponentFixture<OracleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OracleComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OracleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
