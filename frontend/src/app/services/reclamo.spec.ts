import { TestBed } from '@angular/core/testing';

import { Reclamo } from './reclamo';

describe('Reclamo', () => {
  let service: Reclamo;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Reclamo);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
