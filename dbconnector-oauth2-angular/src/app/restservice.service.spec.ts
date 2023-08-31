import { TestBed } from '@angular/core/testing';

import { RestserviceService } from './restservice.service';

describe('RestserviceService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: RestserviceService = TestBed.get(RestserviceService);
    expect(service).toBeTruthy();
  });
});
