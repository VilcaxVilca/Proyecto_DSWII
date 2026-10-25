
import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { CategoriaProducto } from '../../models/categoria-producto';
import { Producto, ProductoRequest } from '../../models/producto';
import { Proveedor } from '../../models/proveedor';

import { ProductoService } from '../../services/producto.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-productos',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './productos.html',
  styleUrl: './productos.css'
})
export class Productos implements OnInit {

  productos: Producto[] = [];
  categorias: CategoriaProducto[] = [];
  proveedores: Proveedor[] = [];

  texto = '';
  mensaje = '';
  error = '';

  editandoId: number | null = null;

  formulario: ProductoRequest = this.nuevoFormulario();

  constructor(
    private productoService: ProductoService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.cargarCombos();
    this.listar();
  }

  // Consultar productos
  listar(): void {
    this.error = '';

    this.productoService.listar(this.texto).subscribe({
      next: (data) => {
        this.productos = data;
      },
      error: (err) => {
        console.error('Error al listar productos:', err);
        this.error = 'No se pudo cargar la lista de productos.';
      }
    });
  }

  // Registrar o actualizar
  guardar(): void {
    console.log('SE EJECUTÓ guardar()');
    this.mensaje = '';
    this.error = '';

    if (!this.puedeGestionarInventario()) {
      this.error = 'No tienes permisos para gestionar el inventario.';
      return;
    }

    if (!this.formulario.codigo.trim() ||
        !this.formulario.nombre.trim() ||
        this.formulario.precio <= 0 ||
        this.formulario.stock < 0 ||
        !this.formulario.fechaVencimiento ||
        this.formulario.categoriaId <= 0 ||
        this.formulario.proveedorId <= 0) {
      this.error = 'Completa correctamente todos los campos.';
      return;
    }

    const idActual = this.editandoId;

    const operacion = idActual !== null
      ? this.productoService.actualizar(idActual, this.formulario)
      : this.productoService.registrar(this.formulario);

    operacion.subscribe({
      next: () => {
        this.mensaje = idActual !== null
          ? 'Producto actualizado correctamente.'
          : 'Producto registrado correctamente.';

        this.cancelar();
        this.listar();
      },
      error: (err) => {
        console.error('Error al guardar producto:', err);

        if (err.status === 401 || err.status === 403) {
          this.error = 'No tienes autorización. Inicia sesión con una cuenta autorizada.';
        } else {
          this.error = 'No se pudo guardar el producto. Revisa los datos ingresados.';
        }
      }
    });
  }

  // Cargar datos en el formulario
  editar(producto: Producto): void {
    if (!this.puedeGestionarInventario()) {
      this.error = 'No tienes permisos para editar productos.';
      return;
    }

    this.mensaje = '';
    this.error = '';

    this.editandoId = producto.id;

    this.formulario = {
      codigo: producto.codigo,
      nombre: producto.nombre,
      precio: producto.precio,
      stock: producto.stock,
      fechaVencimiento: producto.fechaVencimiento,
      activo: producto.activo,
      categoriaId: producto.categoria.id,
      proveedorId: producto.proveedor.id
    };
  }

  // Eliminar producto
  eliminar(producto: Producto): void {
    this.mensaje = '';
    this.error = '';

    if (!this.puedeEliminar()) {
      this.error = 'Solo el administrador puede eliminar productos.';
      return;
    }

    const confirmado = confirm(
      `¿Deseas eliminar el producto "${producto.nombre}"?`
    );

    if (!confirmado) {
      return;
    }

    this.productoService.eliminar(producto.id).subscribe({
      next: () => {
        this.mensaje = 'Producto eliminado correctamente.';
        this.listar();
      },
      error: (err) => {
        console.error('Error al eliminar producto:', err);

        if (err.status === 401 || err.status === 403) {
          this.error = 'No tienes autorización para eliminar este producto.';
        } else {
          this.error = 'No se pudo eliminar el producto.';
        }
      }
    });
  }

  // Limpiar formulario
  cancelar(): void {
    this.editandoId = null;
    this.formulario = this.nuevoFormulario();
  }

  // Estadísticas
  get totalProductos(): number {
    return this.productos.length;
  }

  get activos(): number {
    return this.productos.filter(
      producto => producto.activo
    ).length;
  }

  get bajoStock(): number {
    return this.productos.filter(
      producto => producto.stock <= 10
    ).length;
  }

  get porVencer(): number {
    return this.productos.filter(
      producto => this.diasParaVencer(producto.fechaVencimiento) >= 0 &&
                  this.diasParaVencer(producto.fechaVencimiento) <= 60
    ).length;
  }

  // Estado del stock
  estadoStock(producto: Producto): string {
    if (producto.stock <= 5) {
      return 'Crítico';
    }

    if (producto.stock <= 10) {
      return 'Bajo';
    }

    return 'Disponible';
  }

  claseStock(producto: Producto): string {
    if (producto.stock <= 5) {
      return 'status-danger';
    }

    if (producto.stock <= 10) {
      return 'status-warning';
    }

    return 'status-ok';
  }

  // Vencimiento
  diasParaVencer(fecha: string): number {
    if (!fecha) {
      return Number.MAX_SAFE_INTEGER;
    }

    const hoy = new Date();
    hoy.setHours(0, 0, 0, 0);

    const vencimiento = new Date(`${fecha}T00:00:00`);
    const milisegundosDia = 1000 * 60 * 60 * 24;

    return Math.ceil(
      (vencimiento.getTime() - hoy.getTime()) / milisegundosDia
    );
  }

  etiquetaVencimiento(producto: Producto): string {
    const dias = this.diasParaVencer(producto.fechaVencimiento);

    if (dias < 0) {
      return 'Vencido';
    }

    if (dias <= 30) {
      return `Vence en ${dias} días`;
    }

    if (dias <= 60) {
      return 'Próximo';
    }

    return 'Vigente';
  }

  // Permisos de la interfaz
 puedeGestionarInventario(): boolean {
  console.log('¿Autenticado?', this.authService.estaAutenticado());
  console.log('Rol actual:', this.authService.rolActual());
  console.log('Token disponible:', !!this.authService.obtenerToken());

  return this.authService.estaAutenticado() &&
    this.authService.tieneRol([
      'ADMINISTRADOR',
      'FARMACEUTICO'
    ]);
}

  puedeEliminar(): boolean {
    return this.authService.estaAutenticado() &&
      this.authService.tieneRol(['ADMINISTRADOR']);
  }

  // Cargar categorías y proveedores
  private cargarCombos(): void {
    this.productoService.listarCategorias().subscribe({
      next: (data) => {
        this.categorias = data;
      },
      error: (err) => {
        console.error('Error al cargar categorías:', err);
      }
    });

    this.productoService.listarProveedores().subscribe({
      next: (data) => {
        this.proveedores = data;
      },
      error: (err) => {
        console.error('Error al cargar proveedores:', err);
      }
    });
  }

  // Formulario vacío
  private nuevoFormulario(): ProductoRequest {
    return {
      codigo: '',
      nombre: '',
      precio: 0,
      stock: 0,
      fechaVencimiento: '',
      activo: true,
      categoriaId: 0,
      proveedorId: 0
    };
  }
}
