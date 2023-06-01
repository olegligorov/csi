import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TaggingServicePageComponent } from './tagging-service-page.component';

describe('TaggingServicePageComponent', () => {
  let component: TaggingServicePageComponent;
  let fixture: ComponentFixture<TaggingServicePageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TaggingServicePageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TaggingServicePageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
