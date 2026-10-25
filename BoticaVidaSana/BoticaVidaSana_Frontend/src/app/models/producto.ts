import { CategoriaProducto } from './categoria-producto';
import { Proveedor } from './proveedor';

export interface Producto {
  id: number;
  codigo: string;
  nombre: string;
  precio: number;
  stock: number;
  fechaVencimiento: string;
  activo: boolean;
  categoria: CategoriaProducto;
  proveedor: Proveedor;
}

export interface ProductoRequest {
  codigo: string;
  nombre: string;
  precio: number;
  stock: number;
  fechaVencimiento: string;
  activo: boolean;
  categoriaId: number;
  proveedorId: number;
}
