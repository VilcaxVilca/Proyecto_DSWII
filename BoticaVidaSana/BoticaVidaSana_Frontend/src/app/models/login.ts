export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  username: string;
  mensaje: string;
  rol: string;
  token: string;
  autenticado: boolean;
}