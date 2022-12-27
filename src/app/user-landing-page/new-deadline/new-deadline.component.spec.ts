import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewDeadlineComponent } from './new-deadline.component';

describe('NewDeadlineComponent', () => {
  let component: NewDeadlineComponent;
  let fixture: ComponentFixture<NewDeadlineComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NewDeadlineComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NewDeadlineComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
