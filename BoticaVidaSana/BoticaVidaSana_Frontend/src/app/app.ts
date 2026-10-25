import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  estaAutenticado(): boolean {
    return this.authService.estaAutenticado();
  }

  usuarioActual(): string {
    return this.authService.obtenerSesion()?.username ?? '';
  }

  rolActual(): string {
    return this.authService.rolActual();
  }

  puedeVerReportes(): boolean {
    return this.authService.tieneRol(['ADMINISTRADOR', 'FARMACEUTICO']);
  }

  cerrarSesion(): void {
    this.authService.cerrarSesion();
    this.router.navigate(['/login']);
  }
}
