import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { ReporteVentas, Venta, VentaRequest } from '../models/venta';

@Injectable({
  providedIn: 'root'
})
export class VentaService {

  private apiUrl = 'http://localhost:8080/api/ventas';

  private opciones = {
    withCredentials: true
  };

  constructor(private http: HttpClient) {}

  listar(inicio = '', fin = ''): Observable<Venta[]> {
    let params = new HttpParams();

    if (inicio && fin) {
      params = params
        .set('inicio', inicio)
        .set('fin', fin);
    }

    return this.http.get<Venta[]>(this.apiUrl, {
      ...this.opciones,
      params
    });
  }

  registrar(venta: VentaRequest): Observable<Venta> {
    return this.http.post<Venta>(
      this.apiUrl,
      venta,
      this.opciones
    );
  }

  anular(id: number): Observable<Venta> {
    return this.http.put<Venta>(
      `${this.apiUrl}/${id}/anular`,
      {},
      this.opciones
    );
  }

  reporte(inicio = '', fin = ''): Observable<ReporteVentas> {
    let params = new HttpParams();

    if (inicio && fin) {
      params = params
        .set('inicio', inicio)
        .set('fin', fin);
    }

    return this.http.get<ReporteVentas>(
      `${this.apiUrl}/reporte`,
      {
        ...this.opciones,
        params
      }
    );
  }
}

