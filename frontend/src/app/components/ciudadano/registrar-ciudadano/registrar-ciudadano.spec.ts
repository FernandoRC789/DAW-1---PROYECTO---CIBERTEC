import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegistrarCiudadano } from './registrar-ciudadano';

describe('RegistrarCiudadano', () => {
  let component: RegistrarCiudadano;
  let fixture: ComponentFixture<RegistrarCiudadano>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegistrarCiudadano]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegistrarCiudadano);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
