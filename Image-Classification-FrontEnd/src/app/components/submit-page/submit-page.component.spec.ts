import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubmitPageComponent } from './submit-page.component';

describe('SubmitPageComponent', () => {
  let component: SubmitPageComponent;
  let fixture: ComponentFixture<SubmitPageComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SubmitPageComponent]
    });
    fixture = TestBed.createComponent(SubmitPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
