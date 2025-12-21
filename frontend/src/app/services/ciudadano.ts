import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth';

@Injectable({
  providedIn: 'root'
})
export class CiudadanoService {

  private url = 'http://localhost:8081/api/ciudadanos';

  constructor(private http: HttpClient, private auth: AuthService) {}

  listar(): Observable<any[]> {
    const headers = new HttpHeaders({
      'Authorization': this.auth.obtenerAuthHeader()
    });
    return this.http.get<any[]>(this.url, { headers });
  }

  registrar(ciudadano: any): Observable<any> {
    const headers = new HttpHeaders({
      'Authorization': this.auth.obtenerAuthHeader(),
      'Content-Type': 'application/json'
    });
    return this.http.post<any>(`${this.url}/registrar`, ciudadano, { headers });
  }

  eliminar(id: number) {
    const headers = new HttpHeaders({
      'Authorization': this.auth.obtenerAuthHeader()
    });
    return this.http.delete(`${this.url}/${id}`, { headers, responseType: 'text' });
  }
}
