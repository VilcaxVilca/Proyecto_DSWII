import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';

import { LoginRequest, LoginResponse } from '../models/login';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = 'http://localhost:8081/api/auth/login';
  private sesionKey = 'botica_sesion';

  constructor(private http: HttpClient) {}

  login(request: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(
      this.apiUrl,
      request
    ).pipe(
      tap(response => {
        if (response.autenticado && response.token) {
          this.guardarSesion(response);
        }
      })
    );
  }

  guardarSesion(response: LoginResponse): void {
    sessionStorage.setItem(
      this.sesionKey,
      JSON.stringify(response)
    );
  }

  obtenerSesion(): LoginResponse | null {
    const sesion = sessionStorage.getItem(this.sesionKey);

    return sesion
      ? JSON.parse(sesion) as LoginResponse
      : null;
  }

  obtenerToken(): string | null {
    return this.obtenerSesion()?.token ?? null;
  }

  estaAutenticado(): boolean {
    return Boolean(
      this.obtenerSesion()?.autenticado &&
      this.obtenerToken()
    );
  }

  rolActual(): string {
    return this.obtenerSesion()?.rol ?? '';
  }

  tieneRol(roles: string[]): boolean {
    return roles.includes(this.rolActual());
  }

  cerrarSesion(): void {
    sessionStorage.removeItem(this.sesionKey);
    localStorage.removeItem(this.sesionKey);
  }
}
