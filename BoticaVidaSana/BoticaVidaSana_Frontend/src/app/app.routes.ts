import { Routes } from '@angular/router';
import { authGuard } from './guards/auth.guard';
import { roleGuard } from './guards/role.guard';
import { Dashboard } from './pages/dashboard/dashboard';
import { Productos } from './pages/productos/productos';
import { Login } from './pages/login/login';
import { Ventas } from './pages/ventas/ventas';
import { ReporteVentasPage } from './pages/reporte-ventas/reporte-ventas';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  },
  {
    path: 'panel',
    component: Dashboard,
    canActivate: [authGuard]
  },
  {
    path: 'login',
    component: Login
  },
  {
    path: 'inventario',
    component: Productos,
    canActivate: [authGuard]
  },
  {
    path: 'ventas',
    component: Ventas,
    canActivate: [authGuard]
  },
  {
    path: 'reporte-ventas',
    component: ReporteVentasPage,
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ADMINISTRADOR', 'FARMACEUTICO'] }
  },
  {
    path: '**',
    redirectTo: 'login'
  }
];
