import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SqlserverComponent } from './sqlserver.component';

describe('SqlserverComponent', () => {
  let component: SqlserverComponent;
  let fixture: ComponentFixture<SqlserverComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SqlserverComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SqlserverComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
