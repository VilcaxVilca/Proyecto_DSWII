import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ReporteVentas } from '../../models/venta';
import { VentaService } from '../../services/venta.service';

@Component({
  selector: 'app-reporte-ventas',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './reporte-ventas.html',
  styleUrl: './reporte-ventas.css'
})
export class ReporteVentasPage implements OnInit {
  reporte: ReporteVentas | null = null;
  inicio = '';
  fin = '';
  error = '';

  constructor(private ventaService: VentaService) {}

  ngOnInit(): void {
    const hoy = new Date();
    const primerDia = new Date(hoy.getFullYear(), hoy.getMonth(), 1);
    this.inicio = this.formatoFecha(primerDia);
    this.fin = this.formatoFecha(hoy);
    this.generar();
  }

  generar(): void {
    this.error = '';
    this.ventaService.reporte(this.inicio, this.fin).subscribe({
      next: (data) => this.reporte = data,
      error: () => this.error = 'No se pudo generar el reporte de ventas.'
    });
  }

  imprimir(): void {
    window.print();
  }

  private formatoFecha(fecha: Date): string {
    return fecha.toISOString().slice(0, 10);
  }
}
