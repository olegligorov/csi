import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GalleryPageContainerComponent } from './gallery-page-container.component';

describe('GalleryPageContainerComponent', () => {
  let component: GalleryPageContainerComponent;
  let fixture: ComponentFixture<GalleryPageContainerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GalleryPageContainerComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GalleryPageContainerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
