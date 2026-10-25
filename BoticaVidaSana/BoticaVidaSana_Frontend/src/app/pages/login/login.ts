import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login implements OnInit {
  username = '';
  password = '';
  mensaje = '';
  error = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    if (this.authService.estaAutenticado()) {
      this.router.navigate(['/panel']);
    }
  }

  ingresar(): void {
    this.mensaje = '';
    this.error = '';

    this.authService.login({ username: this.username, password: this.password }).subscribe({
      next: (response) => {
        this.authService.guardarSesion(response);
        this.router.navigate(['/panel']);
      },
      error: (response) => {
        if (response.status === 0) {
          this.error = 'No se pudo conectar con el servicio de autenticacion. Verifica que auth-service este iniciado en el puerto 8081.';
          return;
        }
        this.error = response.error?.mensaje
          ?? response.error?.message
          ?? 'Usuario o contrasena incorrectos.';
      }
    });
  }
}
