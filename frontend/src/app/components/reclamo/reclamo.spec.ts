import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Reclamo } from './reclamo';

describe('Reclamo', () => {
  let component: Reclamo;
  let fixture: ComponentFixture<Reclamo>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Reclamo]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Reclamo);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
