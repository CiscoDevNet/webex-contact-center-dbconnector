import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EndpointComponent } from './endpoint.component';

describe('EndpointComponent', () => {
  let component: EndpointComponent;
  let fixture: ComponentFixture<EndpointComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EndpointComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EndpointComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
