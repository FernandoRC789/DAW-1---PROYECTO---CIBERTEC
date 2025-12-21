import { Injectable, inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class AuthService {

  private apiUrl = 'http://localhost:8081/api/auth';
  private platformId = inject(PLATFORM_ID);

  constructor(private http: HttpClient) {}

  login(username: string, password: string) {
    return this.http.post<any>(`${this.apiUrl}/login`, {
      username,
      password
    });
  }

  guardarSesion(usuario: any, password: string) {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.setItem('usuario', JSON.stringify({
        id: usuario.id,
        username: usuario.username,
        rol: usuario.rol,
        password
      }));
    }
  }

  obtenerUsuario() {
    if (isPlatformBrowser(this.platformId)) {
      const u = localStorage.getItem('usuario');
      return u ? JSON.parse(u) : null;
    }
    return null;
  }

  cerrarSesion() {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.clear();
    }
  }

  estaLogueado(): boolean {
    return !!this.obtenerUsuario();
  }

  obtenerAuthHeader(): string {
    const username = localStorage.getItem('username');
    const password = localStorage.getItem('password');
    if (username && password) {
      return 'Basic ' + btoa(`${username}:${password}`);
    }
    return '';
  }

}
