import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GalleryImageContainerComponent } from './gallery-image-container.component';

describe('GalleryImageContainerComponent', () => {
  let component: GalleryImageContainerComponent;
  let fixture: ComponentFixture<GalleryImageContainerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GalleryImageContainerComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GalleryImageContainerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
