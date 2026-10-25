import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Producto } from '../../models/producto';
import { Venta } from '../../models/venta';
import { ProductoService } from '../../services/producto.service';
import { VentaService } from '../../services/venta.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class Dashboard implements OnInit {
  productos: Producto[] = [];
  ventas: Venta[] = [];
  cargando = true;
  error = '';

  constructor(
    private productoService: ProductoService,
    private ventaService: VentaService
  ) {}

  ngOnInit(): void {
  this.productoService.listar().subscribe({
    next: (data) => {
      this.productos = data;
      this.cargando = false;
    },
    error: (err) => {
      console.error('Error al cargar productos:', err);
      this.error = 'No se pudo cargar el resumen de inventario.';
      this.cargando = false;
    }
  });

  this.ventaService.listar().subscribe({
    next: (data) => {
      this.ventas = data;
      console.log('Ventas cargadas:', data);
    },
    error: (err) => {
      console.error('Error al cargar ventas:', err);
      this.error = 'No se pudo cargar el resumen de ventas.';
    }
  });
}

  get totalProductos(): number {
    return this.productos.length;
  }

  get productosActivos(): number {
    return this.productos.filter((producto) => producto.activo).length;
  }

  get bajoStock(): Producto[] {
    return this.productos
      .filter((producto) => producto.stock <= 10)
      .sort((a, b) => a.stock - b.stock);
  }

  get porVencer(): Producto[] {
    const hoy = new Date();
    const limite = new Date();
    limite.setDate(hoy.getDate() + 60);

    return this.productos
      .filter((producto) => {
        const vencimiento = new Date(`${producto.fechaVencimiento}T00:00:00`);
        return vencimiento >= hoy && vencimiento <= limite;
      })
      .sort((a, b) => a.fechaVencimiento.localeCompare(b.fechaVencimiento));
  }

  get valorInventario(): number {
    return this.productos.reduce((total, producto) => total + Number(producto.precio) * producto.stock, 0);
  }

  get categoriasUnicas(): string {
    const categorias = new Set(this.productos.map((producto) => producto.categoria.nombre));
    return Array.from(categorias).join(', ');
  }

  get ventasDelDia(): Venta[] {
    const hoy = new Date().toISOString().slice(0, 10);
    return this.ventas.filter((venta) => venta.fecha.slice(0, 10) === hoy && venta.estado === 'REGISTRADA');
  }

  get totalVendidoDia(): number {
    return this.ventasDelDia.reduce((total, venta) => total + Number(venta.total), 0);
  }
}
