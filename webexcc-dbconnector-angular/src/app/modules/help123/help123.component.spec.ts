import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Help123Component } from './help123.component';

describe('Help123Component', () => {
  let component: Help123Component;
  let fixture: ComponentFixture<Help123Component>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ Help123Component ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Help123Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
