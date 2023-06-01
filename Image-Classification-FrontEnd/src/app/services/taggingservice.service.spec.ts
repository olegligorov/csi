import { TestBed } from '@angular/core/testing';

import { TaggingserviceService } from './taggingservice.service';

describe('TaggingserviceService', () => {
  let service: TaggingserviceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TaggingserviceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
