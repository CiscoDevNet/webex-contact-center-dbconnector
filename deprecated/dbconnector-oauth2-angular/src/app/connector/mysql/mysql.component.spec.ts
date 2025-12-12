import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MysqlComponent } from './mysql.component';

describe('MysqlComponent', () => {
  let component: MysqlComponent;
  let fixture: ComponentFixture<MysqlComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MysqlComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MysqlComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
