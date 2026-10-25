import { Producto } from './producto';

export interface DetalleVenta {
  id: number;
  producto: Producto;
  cantidad: number;
  precioUnitario: number;
  subtotal: number;
}

export interface Venta {
  id: number;
  codigo: string;
  fecha: string;
  cliente: string;
  usuario: string;
  metodoPago: string;
  estado: string;
  total: number;
  detalles: DetalleVenta[];
}

export interface DetalleVentaRequest {
  productoId: number;
  cantidad: number;
}

export interface VentaRequest {
  cliente: string;
  usuario: string;
  metodoPago: string;
  detalles: DetalleVentaRequest[];
}

export interface ProductoVendido {
  producto: string;
  cantidad: number;
  total: number;
}

export interface UsuarioVenta {
  usuario: string;
  cantidadVentas: number;
  total: number;
}

export interface ReporteVentas {
  totalVendido: number;
  cantidadVentas: number;
  ventasAnuladas: number;
  promedioVenta: number;
  productosMasVendidos: ProductoVendido[];
  ventasPorUsuario: UsuarioVenta[];
}
