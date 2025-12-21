import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { CiudadanoService } from '../../services/ciudadano'; // <-- agregar esto

@Component({
  selector: 'app-ciudadanos',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule], // <-- incluir RouterModule
  templateUrl: './ciudadano.html',
  styleUrls: ['./ciudadano.css']
})
export class CiudadanosComponent implements OnInit {
  ciudadanos: any[] = [];
  mostrarFormulario = false;
  nuevoCiudadano = { dni: '', nombres: '', apellidos: '', direccion: '', telefono: '', correo: '' };
  searchTerm = '';

  constructor(private ciudadanoService: CiudadanoService, private cd: ChangeDetectorRef) {}


  ngOnInit(): void {
    this.listar();
  }

  cargando = false;



  listar() {
    this.cargando = true;
    this.ciudadanoService.listar().subscribe({
      next: (data: any[]) => {
        this.ciudadanos = data || [];
        this.cargando = false;
        this.cd.detectChanges(); // <--- fuerza que Angular refresque la vista
      },
      error: (err) => {
        console.error('Error al listar ciudadanos', err);
        this.ciudadanos = [];
        this.cargando = false;
        this.cd.detectChanges(); // <--- fuerza refresco aunque haya error
      }
    });
  }

  filtrar(): any[] {
    if (!this.searchTerm) return this.ciudadanos;
    return this.ciudadanos.filter(c =>
      c.nombres.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
      c.dni.includes(this.searchTerm)
    );
  }

  eliminar(id?: number) {
    if (!id) {
      alert('Id inválido del ciudadano');
      return;
    }

    if (!confirm('¿Seguro que deseas eliminar este ciudadano?')) return;

    this.ciudadanoService.eliminar(id).subscribe({
      next: (res) => {
        // Aquí res es el string que envía el backend
        alert(res || 'Ciudadano eliminado correctamente');
        // Eliminar del array local para refrescar instantáneamente
        this.ciudadanos = this.ciudadanos.filter(c => c.id !== id);
        this.cd.detectChanges(); // fuerza actualización de la vista
      },
      error: (err) => {
        console.error('Error al eliminar ciudadano', err);
        alert('No se pudo eliminar al ciudadano.');
      }
    });
  }


}
