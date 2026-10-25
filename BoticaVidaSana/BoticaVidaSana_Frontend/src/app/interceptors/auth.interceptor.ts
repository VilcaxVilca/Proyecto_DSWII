
import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.obtenerToken();

  const esBackendPrincipal =
    req.url.startsWith('http://localhost:8080/');

  const esMicroservicio =
    req.url.startsWith('http://localhost:8081/') ||
    req.url.startsWith('http://localhost:8082/');

  if (token && (esBackendPrincipal || esMicroservicio)) {
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      },
      withCredentials: esBackendPrincipal
    });
  } else if (esBackendPrincipal) {
    req = req.clone({
      withCredentials: true
    });
  }

  return next(req);
};
