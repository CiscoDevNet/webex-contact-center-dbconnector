import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConnectorComponent } from './connector.component';

describe('ConnectorComponent', () => {
  let component: ConnectorComponent;
  let fixture: ComponentFixture<ConnectorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ConnectorComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ConnectorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
