import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {Router} from '@angular/router';
import { RouterModule } from '@angular/router';
import { CiudadanoService} from '../../../services/ciudadano';

@Component({
  selector: 'app-registrar-ciudadano',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './registrar-ciudadano.html',
  styleUrls: ['./registrar-ciudadano.css']
})
export class RegistrarCiudadanoComponent {
  nuevoCiudadano = {
    dni: '',
    nombres: '',
    apellidos: '',
    direccion: '',
    telefono: '',
    correo: ''
  };

  mensajeExito = '';
  mensajeError = '';

  constructor(private ciudadanoService: CiudadanoService, private router: Router) {}

  registrar() {
    this.mensajeExito = '';
    this.mensajeError = '';

    const { dni, nombres, apellidos, direccion, telefono, correo } = this.nuevoCiudadano;

    // Validaciones
    if (!dni) { this.mensajeError = 'El DNI es obligatorio.'; return; }
    if (!nombres) { this.mensajeError = 'El nombre es obligatorio.'; return; }
    if (!apellidos) { this.mensajeError = 'El apellido es obligatorio.'; return; }
    if (!direccion) { this.mensajeError = 'La dirección es obligatoria.'; return; }
    if (!telefono) { this.mensajeError = 'El teléfono es obligatorio.'; return; }
    if (!correo) { this.mensajeError = 'El correo es obligatorio.'; return; }

    // DNI: exactamente 8 dígitos
    if (!/^\d{8}$/.test(dni)) {
      this.mensajeError = 'El DNI debe tener exactamente 8 números.';
      return;
    }

    // Teléfono Perú: 9 dígitos, no necesita +51
    if (!/^[9]\d{8}$/.test(telefono)) {
      this.mensajeError = 'El teléfono debe ser válido en Perú (9 dígitos, empieza con 9).';
      return;
    }

    // Correo: obligatorio @gmail.com
    if (!/^[\w._%+-]+@gmail\.com$/.test(correo)) {
      this.mensajeError = 'El correo debe ser válido y terminar en @gmail.com';
      return;
    }

    // Registrar
    this.ciudadanoService.registrar(this.nuevoCiudadano).subscribe({
      next: (data) => {
        this.mensajeExito = 'Se registró correctamente en la base de datos.';
        this.nuevoCiudadano = { dni: '', nombres: '', apellidos: '', direccion: '', telefono: '', correo: '' };
        // Redirigir automáticamente después de 1.5 seg
        setTimeout(() => this.router.navigate(['/ciudadanos']), 1500);
      },
      error: (err) => {
        console.error('Error al registrar ciudadano', err);
        this.mensajeError = 'Error al registrar ciudadano. Verifica los datos.';
      }
    });
  }
}
