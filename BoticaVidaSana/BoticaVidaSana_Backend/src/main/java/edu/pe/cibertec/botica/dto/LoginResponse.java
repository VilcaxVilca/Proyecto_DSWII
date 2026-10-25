package edu.pe.cibertec.botica.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private boolean autenticado;
    private String username;
    private String rol;
    private String mensaje;
}
