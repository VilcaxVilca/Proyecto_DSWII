import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Producto } from '../../models/producto';
import { DetalleVentaRequest, Venta } from '../../models/venta';
import { AuthService } from '../../services/auth.service';
import { ProductoService } from '../../services/producto.service';
import { VentaService } from '../../services/venta.service';

@Component({
  selector: 'app-ventas',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './ventas.html',
  styleUrl: './ventas.css'
})
export class Ventas implements OnInit {
  ventas: Venta[] = [];
  productos: Producto[] = [];
  carrito: DetalleVentaRequest[] = [];
  cliente = 'Consumidor final';
  metodoPago = 'Efectivo';
  productoId = 0;
  cantidad = 1;
  inicio = '';
  fin = '';
  mensaje = '';
  error = '';

  constructor(
    private ventaService: VentaService,
    private productoService: ProductoService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.listar();
    this.cargarProductos();
  }

  listar(): void {
    this.ventaService.listar(this.inicio, this.fin).subscribe({
      next: (data) => this.ventas = data,
      error: () => this.error = 'No se pudo cargar el listado de ventas.'
    });
  }

  agregarProducto(): void {
    this.mensaje = '';
    this.error = '';
    const producto = this.productos.find((item) => item.id === Number(this.productoId));
    if (!producto) {
      this.error = 'Selecciona un producto.';
      return;
    }
    if (this.cantidad <= 0) {
      this.error = 'La cantidad debe ser mayor a cero.';
      return;
    }
    if (producto.stock < this.cantidad) {
      this.error = `Stock insuficiente. Disponible: ${producto.stock}.`;
      return;
    }

    const existente = this.carrito.find((item) => item.productoId === producto.id);
    if (existente) {
      existente.cantidad += this.cantidad;
    } else {
      this.carrito.push({ productoId: producto.id, cantidad: this.cantidad });
    }
    this.productoId = 0;
    this.cantidad = 1;
  }

  quitarProducto(productoId: number): void {
    this.carrito = this.carrito.filter((item) => item.productoId !== productoId);
  }

  registrar(): void {
    this.mensaje = '';
    this.error = '';
    if (this.carrito.length === 0) {
      this.error = 'Agrega al menos un producto a la venta.';
      return;
    }

    this.ventaService.registrar({
      cliente: this.cliente,
      usuario: this.authService.obtenerSesion()?.username ?? 'admin',
      metodoPago: this.metodoPago,
      detalles: this.carrito
    }).subscribe({
      next: () => {
        this.mensaje = 'Venta registrada correctamente.';
        this.carrito = [];
        this.cliente = 'Consumidor final';
        this.metodoPago = 'Efectivo';
        this.listar();
        this.cargarProductos();
      },
      error: (response) => this.error = response.error?.message ?? 'No se pudo registrar la venta.'
    });
  }

  anular(venta: Venta): void {
    if (!this.puedeAnular() || venta.estado === 'ANULADA') {
      return;
    }
    const confirmado = confirm(`Anular la venta ${venta.codigo}?`);
    if (!confirmado) {
      return;
    }
    this.ventaService.anular(venta.id).subscribe({
      next: () => {
        this.mensaje = 'Venta anulada correctamente.';
        this.listar();
        this.cargarProductos();
      },
      error: () => this.error = 'No se pudo anular la venta.'
    });
  }

  productoCarrito(item: DetalleVentaRequest): Producto | undefined {
    return this.productos.find((producto) => producto.id === item.productoId);
  }

  subtotal(item: DetalleVentaRequest): number {
    const producto = this.productoCarrito(item);
    return producto ? Number(producto.precio) * item.cantidad : 0;
  }

  get totalVenta(): number {
    return this.carrito.reduce((total, item) => total + this.subtotal(item), 0);
  }

  puedeAnular(): boolean {
    return this.authService.tieneRol(['ADMINISTRADOR', 'FARMACEUTICO']);
  }

  private cargarProductos(): void {
    this.productoService.listar().subscribe({
      next: (data) => this.productos = data.filter((producto) => producto.activo)
    });
  }
}
