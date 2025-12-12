import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LoggedinComponent } from './loggedin.component';

describe('LoggedinComponent', () => {
  let component: LoggedinComponent;
  let fixture: ComponentFixture<LoggedinComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LoggedinComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LoggedinComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
