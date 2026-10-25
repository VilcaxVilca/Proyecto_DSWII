
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CategoriaProducto } from '../models/categoria-producto';
import { Producto, ProductoRequest } from '../models/producto';
import { Proveedor } from '../models/proveedor';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class ProductoService {

  private apiUrl = 'http://localhost:8082/api/productos';
  private categoriasUrl = 'http://localhost:8080/api/categorias';
  private proveedoresUrl = 'http://localhost:8080/api/proveedores';

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) {}

  private obtenerHeaders(): HttpHeaders {
    const token = this.authService.obtenerToken();

    return token
      ? new HttpHeaders({ Authorization: `Bearer ${token}` })
      : new HttpHeaders();
  }

  // Listar y buscar productos
  listar(texto = ''): Observable<Producto[]> {
    const params = texto
      ? new HttpParams().set('nombre', texto)
      : undefined;

    return this.http.get<Producto[]>(this.apiUrl, {
      params,
      headers: this.obtenerHeaders()
    });
  }

  // Registrar producto
  registrar(producto: ProductoRequest): Observable<Producto> {
    return this.http.post<Producto>(
      this.apiUrl,
      producto,
      { headers: this.obtenerHeaders() }
    );
  }

  // Actualizar producto
  actualizar(
    id: number,
    producto: ProductoRequest
  ): Observable<Producto> {
    return this.http.put<Producto>(
      `${this.apiUrl}/${id}`,
      producto,
      { headers: this.obtenerHeaders() }
    );
  }

  // Eliminar producto
  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(
      `${this.apiUrl}/${id}`,
      { headers: this.obtenerHeaders() }
    );
  }

  // Listar categorías (servicio anterior)
listarCategorias(): Observable<CategoriaProducto[]> {
  return this.http.get<CategoriaProducto[]>(
    this.categoriasUrl,
    { headers: this.obtenerHeaders() }
  );
}

// Listar proveedores
listarProveedores(): Observable<Proveedor[]> {
  return this.http.get<Proveedor[]>(
    this.proveedoresUrl,
    { headers: this.obtenerHeaders() }
  );
}
}
